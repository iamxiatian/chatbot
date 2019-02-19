package xiatian.chatbot.entity

import scala.xml.Elem

/**
  * structure representing an AIML category and operations on Category
  */
case class Category(pattern: String,
                    that: Option[String],
                    topic: Option[String],
                    template: Elem,
                    filename: Option[String] = None
                   ) {

}
