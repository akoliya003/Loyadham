name := """loyadham-kirtan-service"""

version := "1.0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, LauncherJarPlugin, BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion,
      "jenkinsBuildNumber" -> 0,
      "jenkinsBuildTag" -> "unknown",
      "jenkinsBuildUrl" -> "unknown",
      "jenkinsGitCommit" -> "unknown",
      "jenkinsBuildTimeChicago" -> "unknown",
      "jenkinsBuildTimeUTC" -> "unknown"
    ),
    buildInfoPackage := "buildinfo",
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToMap,
    buildInfoOptions += BuildInfoOption.ToJson
  )

scalaVersion := "2.12.6"

evictionWarningOptions in update := EvictionWarningOptions.default
  .withWarnTransitiveEvictions(false)
  .withWarnDirectEvictions(false)

resolvers += "eaio.com" at "http://repo.eaio.com/maven2"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies += guice

libraryDependencies += ws

libraryDependencies += ehcache

libraryDependencies += javaJdbc

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.23.1"

libraryDependencies += "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5"

libraryDependencies += "com.github.danielwegener" % "logback-kafka-appender" % "0.2.0-RC1"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2"

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

parallelExecution in Test := false

javaOptions in Universal ++= Seq(
  "-J-server",
  "-J-Xms64M",
  "-J-XX:+UnlockExperimentalVMOptions",
  "-J-XX:+UseCGroupMemoryLimitForHeap",
  "-J-XX:MaxRAMFraction=1",
  "-Dcom.sun.management.jmxremote=true",
  "-Dcom.sun.management.jmxremote.authenticate=false",
  "-Dcom.sun.management.jmxremote.ssl=false",
  "-Dcom.sun.management.jmxremote.port=9999",
  "-Dcom.sun.management.jmxremote.rmi.port=9999",
  "-Djava.rmi.server.hostname=127.0.0.1"
)

dockerBaseImage := "openjdk:8-jre-alpine"

import com.typesafe.sbt.packager.docker._

dockerCommands ++= Seq(
  Cmd("USER", "root"),
  Cmd("RUN", "apk", "--no-cache", "update"),
  Cmd("RUN", "apk", "--no-cache", "add", "bash"),
  Cmd("USER", "daemon"))

// https://tpolecat.github.io/2017/04/25/scalac-flags.html
scalacOptions ++= Seq(
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates")
