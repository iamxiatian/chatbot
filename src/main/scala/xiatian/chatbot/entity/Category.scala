package xiatian.chatbot.entity

/**
  * structure representing an AIML category and operations on Category
  */
case class Category(pattern: String,
                    that: String,
                    topic: String,
                    template: String,
                    filename: Option[String] = None
                   ) {

}
