package org.scalatra

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import test.scalatest.ScalatraWordSpec

class RouteConcurrencyServlet extends ScalatraServlet {
  val getRoutes: Future[Seq[Route]] =
    Future.sequence((0 until 250).map(i => Future { get(false) { "/"} }))

  val postRoutes: Future[Seq[Route]] =
    Future.sequence((0 until 250).map(i => Future { post(false) { "/"} }))

  val action = for {
    route <- postRoutes
    result <- Future.sequence(for {
      _route <- route.take(250)
      x = Future { post(false) {}; post(false) {}} // add some more routes while we're removing
      y = Future { removeRoute("POST", _route) }
    } yield x zip y)
  } yield result

  Await.result(getRoutes zip action, 30.seconds)

  get("/count/:method") {
    routes(HttpMethod(params("method"))).size.toString
  }
}

class RouteConcurrencySpec extends ScalatraWordSpec {
  addServlet(classOf[RouteConcurrencyServlet], "/*")

  "A scalatra kernel " should {
    "support adding routes concurrently" in {
      get("/count/get") {
        body should equal ("251") // the 500 we added in the future, plus this count route
      }
    }

    "support removing routes concurrently with adding routes" in {
      get("/count/post") {
        body should equal ("500")
      }
    }
  }
}
