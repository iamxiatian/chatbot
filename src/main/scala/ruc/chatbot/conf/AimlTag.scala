package ruc.chatbot.conf

object AimlTag {
  val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
  val aiml = "aiml"
  val category = "category"
  val topic = "topic"
  val pattern = "pattern"
  val template = "template"
  val random = "random"
  val li = "li"
  val star = "star"
  val index = "index"
  val bot = "bot"
  val set = "set"
  val get = "get"
  val think = "think"
  val srai = "srai"
  val sraix = "sraix"
  val map = "map"
  val that = "that"
  val condition = "condition"
  val loop = "loop"
  val learn = "learn"
  val learnf = "learnf"
  val eval = "eval"
  val text = "#PCDATA"
  val comment = "#comment"
  //Attributes
  val name = "name"

  def getCloseTag(name: String): String = "</" + name + ">"

  def getOpenTag(name: String): String = "<" + name + ">"
}
