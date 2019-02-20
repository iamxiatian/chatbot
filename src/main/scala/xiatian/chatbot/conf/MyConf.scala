package xiatian.chatbot.conf

import java.io.{File, FileInputStream}

import com.typesafe.config.impl.Parseable
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions}
import xiatian.chatbot.BuildInfo

/**
  *
  * @author Tian Xia
  *         Dec 02, 2016 13:20
  */
object MyConf {

  //先采用my.conf中的配置，再使用application.conf中的默认配置
  lazy val config: Config = {
    val confFile = new File("./conf/my.conf")

    //先采用conf/my.conf中的配置，再使用application.conf中的默认配置
    if (confFile.exists()) {
      println(s"启用配置文件${confFile.getCanonicalPath}")
      val unresolvedResources = Parseable
        .newResources("application.conf", ConfigParseOptions.defaults())
        .parse()
        .toConfig()

      ConfigFactory.parseFile(confFile).withFallback(unresolvedResources).resolve()
    } else {
      ConfigFactory.load()
    }
  }

  val version = BuildInfo.version

  //所有Robot位于的主目录
  val botsLocation = getString("bots.location", "./bots")

  val screenConfigText: String = {

    def lineToString(lines: List[String], lastPart: Boolean = false): String = {
      val text = lines.zipWithIndex.map {
        case (line, idx) =>
          if (idx == lines.size - 1) {
            if (lastPart) s"    └── ${line}" else s"│   └── ${line}"
          } else {
            if (lastPart) s"    ├── ${line}" else s"│   ├── ${line}"
          }
      }.mkString("\n")

      if (text.isEmpty) {
        if (lastPart) "" else "│"
      } else text
    }

    s"""
       |My configuration(build: ${BuildInfo.builtAtString}):
       |├── bots config:
       |│   ├── botsLocation ==> $botsLocation
       |│   ├──
       |│   └── botsLocation ==> ${botsLocation}
       |
       |""".stripMargin
  }

  def getString(path: String) = config.getString(path)

  def getString(path: String, defaultValue: String) =
    if (config.hasPath(path)) config.getString(path) else defaultValue

  def getInt(path: String) = config.getInt(path)

  def getBoolean(path: String) = config.getBoolean(path)

  def outputConfig() = println(screenConfigText)

  def configLog(): Unit = {
    val f = new File("./conf/logback.xml")
    if (f.exists()) {
      import ch.qos.logback.classic.LoggerContext
      import ch.qos.logback.classic.joran.JoranConfigurator
      import org.slf4j.LoggerFactory
      val loggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
      loggerContext.reset()
      val configurator = new JoranConfigurator
      val configStream = new FileInputStream(f)
      configurator.setContext(loggerContext)
      configurator.doConfigure(configStream) // loads logback file
      configStream.close()
      println("finished to config logback.")
    }
  }
}