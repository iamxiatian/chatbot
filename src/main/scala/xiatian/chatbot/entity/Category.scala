package xiatian.chatbot.entity

import scala.xml.{Elem, Text}

/**
  * structure representing an AIML category and operations on Category
  */
case class Category(patternElems: Seq[Elem],
                    that: Option[String],
                    topic: Option[String],
                    template: Elem
                   ) {
  def patterns: Seq[String] = {
    patternElems.map(Category.parse(_))
  }
}

object Category {
  /**
    * 解析一个pattern元素
    * @param patternElem
    * @return
    */
  private def parse(patternElem: Elem): String = {
    patternElem.child.map {
      case t: Text =>
        t.text

      case c: Elem if c.label == "term" =>
        //如果是term，则提取其中的词性作为词语， 并把该部分替换为"pos_XX"的形式
        val value = (c \ "@pos").text
        val pos = value.split(",").map(_.trim).mkString("_")
        s"pos_$pos"
      case _ => "ERROR"
    }.mkString("")
  }
}
