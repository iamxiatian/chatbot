package xiatian.chatbot.entity

import scala.xml.{Elem, Text}

/**
  * structure representing an AIML category and operations on Category
  */
case class Category(patternElem: Elem,
                    that: Option[String],
                    topic: Option[String],
                    template: Elem
                   ) {
  def pattern: String = {
    patternElem.child.map {
      case t: Text =>
        t.text

      case c: Elem if c.label == "term" =>
        //如果是term，则提取其中的词性作为词语， 并把该部分替换为"pos_XX"的形式
        val pos = (c \ "@pos").text
        s"pos_$pos"

      case _ => "ERROR"
    }.mkString("")
  }
}
