name := """scablo"""
organization := "io.razem"
maintainer := "julian@pieles.digital"

version := "0.0.1"

lazy val root = (project in file("."))
  .settings(
    // DIST
    packageName in Universal := "scablo",
    // DOCKER
    dockerBaseImage := "adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.6_10",
    packageName in Docker := "scablo",
    version in Docker := version.value,
    dockerUsername in Docker := Some("razemio")
  )
  .enablePlugins(PlayScala, JavaAppPackaging, DockerPlugin, AshScriptPlugin)

scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "org.webjars.npm" % "bulma" % "0.9.1",
  "org.webjars" % "font-awesome" % "4.7.0",
  "org.webjars" % "chartjs" % "2.7.2",
  "org.webjars.bower" % "highlightjs" % "9.12.0",
  "org.webjars.bower" % "photoswipe" % "4.1.2",
  "org.webjars" % "jquery" % "3.3.1-1",
  "org.webjars" % "fancybox" % "3.2.5"
)

libraryDependencies ++= Seq(
  "com.vladsch.flexmark" % "flexmark-all" % "0.26.4",
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8",
  "com.github.pathikrit" %% "better-files" % "3.6.0",
  "org.zeroturnaround" % "zt-zip" % "1.13",
  guice
)

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.razem.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.razem.binders._"
