package xiatian.chatbot.chat

import scala.collection.mutable

/**
  * 聊天机器人的属性信息，保存了属性值对
  */
class Predicates {
  val params = mutable.Map.empty[String, String]

  def put(key: String, value: String): Predicates = {
    params.put(key, value)
    this
  }

  def get(key: String): Option[String] = params.get(key)
}
