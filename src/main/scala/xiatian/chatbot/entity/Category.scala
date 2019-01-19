package xiatian.chatbot.entity

/**
  * structure representing an AIML category and operations on Category
  */
case class Category(pattern: String,
                    that: Option[String],
                    topic: Option[String],
                    template: String,
                    filename: Option[String] = None
                   ) {

}
