package ruc.chatbot.entity

/**
  * 所有的AIML Element必须拥有`type`属性，标记当前Element的类型
  */
trait AimlElement {
  def `type`: String
}
