package xiatian.chatbot.parse

import better.files.File
import xiatian.chatbot.bot.Substitution
import xiatian.chatbot.conf.MyConf

/**
  * 对问句进行简化，去除“请问”之类的信息
  */
object InputSimplify {
  val substitution = Substitution(File(File(MyConf.botsLocation, "xiatian"), "substitutions"))

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

  def simplify(text: String): String = {
    val s = substitution.normalize(text)

    //第一个标点符号前面包含你好，且后面长度较长，则只保留后面的问句
    NLP.segment(s).span(p => p.nature.toString == "w")
    val (a, b) = NLP.segment(s).span(p => p.nature.toString != "w")

    if (a.size >= 2 &&
      b.size >= 3 &&
      a.map(_.word).mkString("").contains("你好") &&
      b.dropWhile(p => p.nature.toString == "w").size > 3
    ) {
      b.dropWhile(p => p.nature.toString == "w").mkString("")
    } else {
      // 如果前面没有你好这种问候类的话语，直接进入常规的删除处理
      val input = s.trim
      heads.find {
        h =>
          input.startsWith(h)
      } match {
        case Some(h) => simplify(input.substring(h.length))
        case None => input
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(InputSimplify.simplify(" 请问你知道什么是信息安全吗？"))
    val s = "niha哦，我是机器人"
    val (a, b) = NLP.segment(s).span(p => p.nature.toString != "w")
  }
}
