package ruc.chatbot.conf

object Wildcard {
  val ZeroMore = "^"
  val OneMore = "*"
  val ZeroMorePriority = "#"
  val OneMorePriority = "_"

  def matchOne(pattern: String): Boolean = {
    OneMore == pattern ||
      OneMorePriority == pattern ||
      ZeroMore == pattern ||
      ZeroMorePriority == pattern
  }
}
