package xiatian.chatbot.bot

import better.files.File
import com.typesafe.config.{Config, ConfigFactory}

/**
  * 每个机器人所拥有的基本信息，该信息默认从属性配置问句中读取
  */
class BotInfo(config: Config) {
  def name = value("name", "")

  def gender = value("gender", "")

  def birthday: String = value("birthday", "")

  def value(path: String, defaultValue: String) =
    if (config.hasPath(path)) config.getString(path) else defaultValue
}

object BotInfo {
  /**
    * 从属性配置文件中读取配置信息
    *
    * @param f
    */
  def apply(f: File): BotInfo = {
    val config = if (f.exists) {
      ConfigFactory.parseFile(f.toJava)
    } else {
      ConfigFactory.empty()
    }

    new BotInfo(config)
  }
}
