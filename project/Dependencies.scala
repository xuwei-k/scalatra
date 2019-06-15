import sbt._
import Keys._

object Dependencies {
  lazy val parserCombinators        =  Def.setting("org.scala-lang.modules"  %% "scala-parser-combinators" % parserCombinatorsVersion.value)
  lazy val xml                      =  Def.setting("org.scala-lang.modules"  %% "scala-xml"      % xmlVersion.value)
  lazy val akkaActor                =  Def.setting("com.typesafe.akka" %% "akka-actor"           % akkaVersion.value)
  lazy val akkaTestkit              =  Def.setting("com.typesafe.akka" %% "akka-testkit"         % akkaVersion.value)
  lazy val atmosphereRuntime        =  "org.atmosphere"          %  "atmosphere-runtime"         % "2.4.13"
  lazy val atmosphereJQuery         =  "org.atmosphere.client"   %  "jquery"                     % "2.2.13" artifacts(Artifact("jquery", "war", "war"))
  lazy val atmosphereClient         =  "org.atmosphere"          %  "wasync"                     % "2.1.2"
  lazy val atmosphereRedis          =  "org.atmosphere"          %  "atmosphere-redis"           % "2.4.4"
  lazy val atmosphereCompatJbossweb =  "org.atmosphere"          %  "atmosphere-compat-jbossweb" % atmosphereCompatVersion
  lazy val atmosphereCompatTomcat   =  "org.atmosphere"          %  "atmosphere-compat-tomcat"   % atmosphereCompatVersion
  lazy val atmosphereCompatTomcat7  =  "org.atmosphere"          %  "atmosphere-compat-tomcat7"  % atmosphereCompatVersion
  lazy val commonsFileupload        =  "commons-fileupload"      %  "commons-fileupload"         % "1.3.3"
  lazy val commonsIo                =  "commons-io"              %  "commons-io"                 % "2.5"
  lazy val commonsLang3             =  "org.apache.commons"      %  "commons-lang3"              % "3.6"
  lazy val httpclient               =  "org.apache.httpcomponents" % "httpclient"                % httpcomponentsVersion
  lazy val httpmime                 =  "org.apache.httpcomponents" % "httpmime"                  % httpcomponentsVersion
  lazy val jettyServer              =  "org.eclipse.jetty"       %  "jetty-server"               % jettyVersion
  lazy val jettyPlus                =  "org.eclipse.jetty"       %  "jetty-plus"                 % jettyVersion
  lazy val jettyServlet             =  "org.eclipse.jetty"       %  "jetty-servlet"              % jettyVersion
  lazy val jettyWebsocket           =  "org.eclipse.jetty.websocket" %"websocket-server"         % jettyVersion
  lazy val jettyWebapp              =  "org.eclipse.jetty"       %  "jetty-webapp"               % jettyVersion
  lazy val jodaConvert              =  "org.joda"                %  "joda-convert"               % "1.8.2"
  lazy val jodaTime                 =  "joda-time"               %  "joda-time"                  % "2.9.9"
  lazy val json4sCore               =  "org.json4s"              %% "json4s-core"                % json4sVersion
  lazy val json4sExt                =  "org.json4s"              %% "json4s-ext"                 % json4sVersion
  lazy val json4sJackson            =  "org.json4s"              %% "json4s-jackson"             % json4sVersion
  lazy val json4sNative             =  "org.json4s"              %% "json4s-native"              % json4sVersion
  lazy val json4sXml                =  "org.json4s"              %% "json4s-xml"                 % json4sVersion
  lazy val junit                    =  "junit"                   %  "junit"                      % "4.12"
  lazy val jUniversalChardet        =  "com.googlecode.juniversalchardet" % "juniversalchardet"  % "1.0.3"
  lazy val logbackClassic           =  "ch.qos.logback"          %  "logback-classic"            % "1.2.3"
  lazy val mimeUtil                 =  "eu.medsea.mimeutil"      %  "mime-util"                  % "2.1.3" exclude("org.slf4j", "slf4j-log4j12") exclude("log4j", "log4j")
  lazy val mockitoAll               =  "org.mockito"             %  "mockito-core"               % "2.7.22"
  lazy val scalate                  =  Def.setting("org.scalatra.scalate" %% "scalate-core"      % scalateVersion.value)
  lazy val scalatest                =  Def.setting("org.scalatest" %% "scalatest"                % scalatestVersion.value)
  lazy val scalaz                   =  "org.scalaz"              %% "scalaz-core"                % "7.2.27"
  lazy val servletApi               =  "javax.servlet"           %  "javax.servlet-api"          % "3.1.0"
  lazy val springWeb                =  "org.springframework"     %  "spring-web"                 % "4.3.9.RELEASE"
  lazy val slf4jApi                 =  "org.slf4j"               %  "slf4j-api"                  % "1.7.25"
  lazy val slf4jSimple              =  "org.slf4j"               %  "slf4j-simple"               % "1.7.25"
  lazy val specs2                   =  Def.setting(Seq(
                                       "org.specs2"              %% "specs2-core",
                                       "org.specs2"              %% "specs2-mock",
                                       "org.specs2"              %% "specs2-matcher-extra"
                                                                                  ).map(_        % specs2Version.value))
  lazy val testJettyServlet         =  "org.eclipse.jetty"       %  "test-jetty-servlet"         % jettyVersion
  lazy val testng                   =  "org.testng"              %  "testng"                     % "6.11" exclude("junit", "junit")
  lazy val metricsScala             =  Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "nl.grons" %% "metrics-scala" % "3.5.9"
      case _ =>
        "nl.grons" %% "metrics4-scala" % "4.0.7"
    }
  )
  lazy val metricsServlets          =  "io.dropwizard.metrics"   %  "metrics-servlets"           % "3.2.3"
  lazy val metricsServlet           =  "io.dropwizard.metrics"   %  "metrics-servlet"            % "3.2.3"
  lazy val googleGuava              =  "com.google.guava"        % "guava"                       % "22.0"
  lazy val googleFindBugs           = "com.google.code.findbugs" % "jsr305"                      % "3.0.2"

  private val akkaVersion             = Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "2.5.3"
      case _ =>
        "2.5.23"
    }
  )
  private val atmosphereCompatVersion = "2.0.1"
  private val httpcomponentsVersion   = "4.5.3"
  private val jettyVersion            = "9.4.6.v20170531"
  private val json4sVersion           = "3.6.6"
  private val scalateVersion          = Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "1.8.0"
      case _ =>
        "1.9.4"
    }
  )
  private val scalatestVersion        = Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "3.0.3"
      case _ =>
        "3.0.8"
    }
  )
  private val specs2Version           = Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "4.0.1"
      case _ =>
        "4.5.1"
    }
  )
  private val xmlVersion              = Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "1.0.6"
      case _ =>
        "1.2.0"
    }
  )
  private val parserCombinatorsVersion = Def.setting(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        "1.0.6"
      case _ =>
        "1.1.2"
    }
  )
}
