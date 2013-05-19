package org.scalatra

import org.scalatra.commands._
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}
import org.json4s.JsonAST._
import org.json4s.JsonAST.JString

class ValidationExample extends ScalatraServlet with ClientSideValidationSupport with ParamsOnlyCommandSupport with JacksonJsonSupport {

  get("/"){
    <html>
      <script src={url("/assets/js/jquery.min.js", includeServletPath = false)}></script>
      <script src={url("/assets/js/validation.js", includeServletPath = false)}></script>
      <body>
        <form method="POST" action={contextPath + "/validation"} validate="true">
          <p>
            <label for="id">Name:</label>
            <input type="text" name="name" id="name"/>
            <span id="error-name" class="error"></span>
          </p>
          <p>
            <input type="submit" value="Click!"/>
          </p>
        </form>
      </body>
    </html>
  }

  postWithValidation("/", command[HelloWorldForm]){ model: HelloWorldModel =>
    <ul>
      <li>{model.name}</li>
    </ul>
  }

}

case class HelloWorldModel(name: String)

trait ValidationCommand[T] extends ParamsOnlyCommand {
  def toModel: T
}

class HelloWorldForm extends ValidationCommand[HelloWorldModel] {
  val name: Field[String] = asString("name").notBlank

  def toModel = new HelloWorldModel(name.value.get)
}

object HelloWorldForm {
  implicit def toModel(form: HelloWorldForm): HelloWorldModel = new HelloWorldModel(form.name.value.get)
}

trait ClientSideValidationSupport { self: ScalatraServlet with ParamsOnlyCommandSupport with JacksonJsonSupport =>

  protected implicit val jsonFormats: Formats = DefaultFormats

  def postWithValidation[T](path: String, factory: => ValidationCommand[T])(action: T => Any)(implicit mf: Manifest[T]){
    validateFor(path, factory)

    post(path){
      val form = factory
      form.bindTo(params)
      if(form.isValid){
        action(form.toModel)
      } else BadRequest()
    }
  }

  private def validateFor(path: String, factory: => ValidationCommand[_]){
    post(path + (if(path.endsWith("/")) "validate" else "/validate")){
      contentType = "application/json"
      val form = factory
      form.bindTo(params)
      if(form.isValid){
        JObject(Nil)
      } else {
        JObject(form.errors.map { error =>
          JField(error.field.name, JString(error.error.get.message))
        }.toList)
      }
    }
  }
}

