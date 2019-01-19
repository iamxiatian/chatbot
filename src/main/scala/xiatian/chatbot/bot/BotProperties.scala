package xiatian.chatbot.bot

import scala.collection.mutable

/**
  * Bot Properties
  */
class BotProperties {
  val params = mutable.Map.empty[String, String]

  def put(key: String, value: String): BotProperties = {
    params.put(key, value)
    this
  }

  def get(key: String): Option[String] = params.get(key)
}
