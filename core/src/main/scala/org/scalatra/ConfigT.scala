package org.scalatra

import javax.servlet.{ FilterConfig, ServletConfig, ServletContext }

private[scalatra] trait ConfigT[A] {
  def getServletContext(self: A): ServletContext
  def getInitParameter(self: A, name: String): String
  def getInitParameterNames(self: A): java.util.Enumeration[String]
}

private[scalatra] object ConfigT {

  implicit class ConfigTOps[A](private val self: A) extends AnyVal {
    def getServletContext()(implicit c: ConfigT[A]): ServletContext =
      c.getServletContext(self)
    def getInitParameter(name: String)(implicit c: ConfigT[A]): String =
      c.getInitParameter(self, name)
    def getInitParameterNames()(implicit c: ConfigT[A]): java.util.Enumeration[String] =
      c.getInitParameterNames(self)
  }

  implicit val servletConfigT: ConfigT[ServletConfig] =
    new ConfigT[ServletConfig] {
      def getInitParameter(self: ServletConfig, name: String) =
        self.getInitParameter(name)
      def getInitParameterNames(self: ServletConfig) =
        self.getInitParameterNames()
      def getServletContext(self: ServletConfig) =
        self.getServletContext()
    }

  implicit val filtertConfigT: ConfigT[FilterConfig] =
    new ConfigT[FilterConfig] {
      def getInitParameter(self: FilterConfig, name: String) =
        self.getInitParameter(name)
      def getInitParameterNames(self: FilterConfig) =
        self.getInitParameterNames()
      def getServletContext(self: FilterConfig) =
        self.getServletContext()
    }
}
