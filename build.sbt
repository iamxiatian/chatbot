organization := "xiatian"
name := "chatbot"
version := "1.6"

scalaVersion := "2.12.3"
sbtVersion := "1.2.1"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := BuildInfoKey.ofN(name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "xiatian.chatbot"
  )

buildInfoKeys ++= Seq[BuildInfoKey](
  "author" -> "XiaTian"
)

buildInfoKeys += buildInfoBuildNumber
buildInfoOptions += BuildInfoOption.BuildTime
buildInfoOptions += BuildInfoOption.ToJson

libraryDependencies += "com.typesafe" % "config" % "1.3.3"

//XML support
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"

//command line parser
libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.0"

//Scala wrapper for Joda Time.
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.16.0"

//Scala better file
libraryDependencies += "com.github.pathikrit" %% "better-files-akka" % "3.0.0"

//Java mail
libraryDependencies += "javax.mail" % "mail" % "1.4.7"

libraryDependencies += "org.apache.poi" % "poi" % "4.0.1"
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "4.0.1"

libraryDependencies += "org.docx4j" % "docx4j" % "6.1.0"

libraryDependencies += "com.hankcs" % "hanlp" % "portable-1.7.1"

libraryDependencies += "org.rocksdb" % "rocksdbjni" % "5.7.2"

//Baidu AI library
libraryDependencies += "com.baidu.aip" % "java-sdk" % "4.10.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "ch.qos.logback" % "logback-core" % "1.2.3"
libraryDependencies += "com.google.guava" % "guava" % "24.0-jre"

//Scala Test library
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "junit" % "junit" % "4.12"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions in Test ++= Seq("-Yrangepos")


import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._
//enablePlugins(JavaServerAppPackaging)
enablePlugins(JavaAppPackaging)

mainClass in Compile := Some("xiatian.chatbot.Start")

mappings in(Compile, packageDoc) := Seq()

//把运行时需要的配置文件拷贝到打包后的主目录下
//mappings in Universal += file("my.conf") -> "my.conf"
mappings in Universal ++= directory("web")
mappings in Universal ++= directory("conf")

javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xms4G",
  "-J-Xmx8G"
)

//解决windows的line too long问题
scriptClasspath := Seq("*")

initialCommands in console +=
  """
    |import java.io._
    |import java.util.Date
    |
    |import xiatian.chatbot.entity._
    |import xiatian.chatbot.chat._
    |import xiatian.chatbot.bot._
    |import xiatian.chatbot.core._
    |
    |import scala.collection.JavaConverters._
    |import scala.util.{Failure, Success, Try}
    |import scala.xml.{Elem, XML}
    |
    |val aimlFile = new java.io.File("/home/xiatian/workspace/github/chatbot/kb/alice2/aiml/dialog.aiml")
    |val doc = XML.loadFile(aimlFile)
  """.stripMargin
