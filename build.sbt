name := "WebCrawler"

version := "0.1"

scalaVersion := "2.11.12"

//ACTOR SYSTEM
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.8" // or whatever the latest version is
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0-RC1"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"

libraryDependencies +="org.seleniumhq.webdriver" % "webdriver-selenium" % "0.9.7376"
libraryDependencies += "org.seleniumhq.webdriver" % "webdriver-htmlunit" % "0.9.7376"


libraryDependencies += "info.folone" %% "poi-scala" % "0.18"

libraryDependencies += "org.apache.poi" % "poi" % "3.9"
libraryDependencies += "com.codeborne" % "phantomjsdriver" % "1.4.4"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"
