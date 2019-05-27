package xiatian.chatbot.parse

/**
  * 对问句进行简化，去除“请问”之类的信息
  */
object InputSimplify {
  val heads = List(
    "我想问一下",
    "我问一下",
    "请问一下",
    "问一下",
    "你知道",
    "你是否知道",
    "你知不知道",
    "你好，请问一下",
    "你好，我问一下",
    "你好，问一下",
    "请问",
  )

  def simplify(s: String): String = {
    val input = s.trim
    heads.find {
      h =>
        input.startsWith(h)
    } match {
      case Some(h) => simplify(input.substring(h.length))
      case None => input
    }
  }

  def main(args: Array[String]): Unit = {
    println(InputSimplify.simplify(" 请问你知道什么是信息安全吗？"))
  }
}
