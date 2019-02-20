package xiatian.chatbot.bot

import better.files.File

/**
  * 每个机器人所拥有的基本信息，该信息默认从属性配置问句中读取
  */
case class BotInfo(name: String,
                   email: String,
                   birthday: String,
                   gender: String,
                   params: Map[String, String]) {
  def value(key: String) = ???
}

object BotInfo {
  /**
    * 从属性配置文件中读取配置信息
    * @param f
    */
  def apply(f: File): Unit = {

  }
}
