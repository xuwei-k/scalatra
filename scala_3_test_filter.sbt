val excludeTests = Set(
  "org.scalatra.test.specs2.ScalatraSpecSpec",
  "org.scalatra.NotFoundSpec",
  "org.scalatra.swagger.ModelSpec",
  "org.scalatra.swagger.SwaggerSpec2",
  "org.scalatra.swagger.ModelCollectionSpec",
)

ThisBuild / Test / testOptions ++= {
  if (scalaBinaryVersion.value == "3") {
    Seq(
      Tests.Exclude(excludeTests),
      Tests.Filter { testName =>
        // scalate does not work with Scala 3
        !testName.startsWith("org.scalatra.scalate.")
      },
    )
  } else {
    Nil
  }
}
