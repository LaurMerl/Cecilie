name := "WebCrawler"

version := "0.1"

scalaVersion := "2.11.12"

//ACTOR SYSTEM
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.8" // or whatever the latest version is
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0-RC1"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"



libraryDependencies += "info.folone" %% "poi-scala" % "0.18"

libraryDependencies += "org.apache.poi" % "poi" % "3.9"
libraryDependencies += "com.codeborne" % "phantomjsdriver" % "1.4.4"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"
libraryDependencies += "org.mongodb.scala" % "mongo-scala-driver_2.11" % "2.2.0"
libraryDependencies += "org.mongodb.scala" % "mongo-scala-bson_2.11" % "2.2.0"
libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0-M2"

enablePlugins(sbtdocker.DockerPlugin)

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"
  new Dockerfile {
    from("openjdk:alpine")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}
imageNames in docker := Seq(
  // Sets the latest tag
  ImageName(s"bedux/cecilie:latest"),

  // Sets a name with a tag that contains the project version
  ImageName(
    namespace = Some("bedux"),
    repository = "cecilie",
    tag = Some("v" + version.value)
  )
)