name := """scablo"""
organization := "io.razem"
maintainer := "julian@pieles.digital"

version := "0.0.2"

lazy val scalaV = "2.13.8"

lazy val root = (project in file("."))
  .settings(
    scalaVersion := scalaV,
    // DIST
    Universal / packageName  := "scablo",
    // DOCKER
    dockerBaseImage := "adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.6_10",
    Docker / packageName  := "scablo",
    Docker / version := version.value,
    Docker / dockerUsername := Some("razemio")
  )
  .settings(
    scalaJSProjects := Seq(js),
    Assets / pipelineStages  := Seq(scalaJSPipeline)
  )
  .settings(
    npmDeps.map(t => {
      npmAssets ++= NpmAssets.ofProject(js) {
        nodeModules => (nodeModules / "lib" / t._1 / t._4).allPaths
      }.value
    })
  )
  .enablePlugins(PlayScala, JavaAppPackaging, DockerPlugin, AshScriptPlugin, WebScalaJSBundlerPlugin)

lazy val js = (project in file("js"))
  .settings(
    scalaVersion := scalaV,
    scalaJSUseMainModuleInitializer := false,
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    useYarn := true
  )
  .settings(
    Compile / npmDependencies  ++= npmDeps.map(t => s"lib/${t._1}" -> s"npm:${t._2}@${t._3}"),
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

val npmDeps = Seq(
//NAME                        NPM                               VERSION     PATH
  ("bulma"                    ,"bulma"                          ,"0.9.3"    ,""      ),
)

libraryDependencies ++= Seq(
  "org.webjars" % "highlightjs" % "10.1.2",
  "org.webjars" % "font-awesome" % "4.7.0",
  "org.webjars" % "chartjs" % "2.7.2",
  "org.webjars.bower" % "photoswipe" % "4.1.2",
  "org.webjars" % "jquery" % "3.3.1-1",
  "org.webjars" % "fancybox" % "3.2.5"
)

libraryDependencies ++= Seq(
  "com.vladsch.flexmark" % "flexmark-all" % "0.26.4",
  "com.github.nscala-time" %% "nscala-time" % "2.30.0",
  "com.sksamuel.scrimage" % "scrimage-core" % "4.0.26",
  "com.sksamuel.scrimage" %% "scrimage-scala" % "4.0.26",
  "com.github.pathikrit" %% "better-files" % "3.9.1",
  "org.zeroturnaround" % "zt-zip" % "1.13",
  guice
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.razem.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.razem.binders._"
