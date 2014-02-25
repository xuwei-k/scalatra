package org.scalatra.macros

import scala.reflect.macros.Context
import scala.language.experimental.macros
import org.scalatra._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.reflect.internal.annotations.compileTimeOnly

/**
 * @author Bryce Anderson
 *         Created on 2/23/14
 */
object DSLMacros {

  type DSLContext = Context { type PrefixType = MacroDSL }

  private def actionBuilder(c: DSLContext)(funname: c.TermName, block: c.Expr[Any]): c.Expr[Route] = {
    import c.universe._

    val reqName = newTermName("request")
    val respName = newTermName("response")

    val transformer = new Transformer {
      override def transform(tree: Tree): Tree = tree match {
        case q"$a.this.request" => Ident(reqName)
        case q"$a.this.response" => Ident(respName)
        case t => super.transform(t)
      }
    }

    val newBlock = c.resetAllAttrs(transformer.transform(block.tree))

    val defexpr = c.Expr[Route](q"""def $funname($reqName: ${c.weakTypeOf[HttpServletRequest]},
                                 $respName: ${c.weakTypeOf[HttpServletResponse]}): Any = ${newBlock}""")

    defexpr
  }

  def routeBuilder(c: DSLContext)(method: c.Expr[HttpMethod],
                                transformers: Seq[c.Expr[RouteTransformer]],
                                block: c.Expr[Any]): c.Expr[Route] = {
    import c.universe._

    val texpr = c.Expr[Seq[RouteTransformer]](q"Seq(..$transformers)")

    val funName = newTermName(c.fresh("routeAction"))
    val actionName = c.Expr[Action](Ident(funName))

    val action = actionBuilder(c)(funName, block)

    val result = reify {
      action.splice
      c.prefix.splice.addRoute(method.splice, texpr.splice, actionName.splice)
    }

//    println("-------------------------------------------------\n" +
//      result.tree +
//      "\n-------------------------------------------------")

    result
  }

  def getImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Get), transformers, block)
  }

  def postImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Post), transformers, block)
  }

  def putImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Put), transformers, block)
  }

  def deleteImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Delete), transformers, block)
  }

  def optionsImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Options), transformers, block)
  }

  def headImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Head), transformers, block)
  }

  def patchImpl(c: DSLContext)(transformers: c.Expr[RouteTransformer]*)(block: c.Expr[Any]): c.Expr[Route] = {
    routeBuilder(c)(c.universe.reify(Patch), transformers, block)
  }

  def notFoundImpl(c: DSLContext)(block: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    //def notFound(fun: Any) { doNotFound = fun }
    val actionName = newTermName("notFoundAction")
    val action = actionBuilder(c)(actionName, block)

    reify {
      c.prefix.splice.notFoundAction {
        action.splice
        c.Expr[Action](Ident(actionName)).splice
      }
    }
  }

}

trait MacroDSL {

  import DSLMacros._

  @compileTimeOnly("Http Request cannot be called outside of a http method builder")
  implicit def request: HttpServletRequest = sys.error("Shouldn't get here.")

  @compileTimeOnly("Http Response cannot be called outside of a http method builder")
  implicit def response: HttpServletResponse = sys.error("Shouldn't get here.")

  def addRoute(method: HttpMethod,
               transformers: Seq[RouteTransformer],
               action: (HttpServletRequest, HttpServletResponse) => Any): Route

  final def get(transformers: RouteTransformer*)(block: Any): Route = macro getImpl

  /**
   * @see get
   */
  final def post(transformers: RouteTransformer*)(block: Any): Route = macro postImpl

  /**
   * @see get
   */
  def put(transformers: RouteTransformer*)(block: Any): Route = macro putImpl

  /**
   * @see get
   */
  def delete(transformers: RouteTransformer*)(block: Any): Route = macro deleteImpl

  /**
   * @see get
   */
  def options(transformers: RouteTransformer*)(block: Any): Route = macro optionsImpl

  /**
   * @see head
   */
  def head(transformers: RouteTransformer*)(block: Any): Route = macro headImpl

  /**
   * @see patch
   */
  def patch(transformers: RouteTransformer*)(block: Any): Route = macro patchImpl


  //////////////// Non-Http method helpers /////////////////////////////////////

  def notFound(block: Any): Unit = macro notFoundImpl

  def notFoundAction(block: Action): Unit
}
