package xiatian.chatbot.ability.faq

import java.nio.charset.StandardCharsets

import better.files.File
import io.circe.{Json, parser}
import xiatian.chatbot.bot.parse.NLP

import scala.collection.mutable
import scala.util.{Random, Try}

case class Faq(question: String,
               answer: String,
               answerType: String,
               domain: String) {
  def termString(): String = Faq.termString(question)

  def reply(): String = {
    //如果是answerType=json，则按照JSON格式解析，此时，JSON格式里面是一个数组
    if (answerType == Faq.TYPE_JSON) {
      parseJson(answer)
    } else answer
  }

  /**
    * 从JSON字符串中随机选择一个作为答案输出
    *
    * @param jsonString
    * @return
    */
  private def parseJson(jsonString: String): String = Try {
    val items: Vector[Json] = parser.parse(jsonString).toOption.get.asArray.get
    items(Random.nextInt(items.size)).asString.getOrElse("啥情况啊？")
  }.toOption.getOrElse("哎吆，系统出现错误了，问一下管理员啥情况呀（貌似回复的语法有问题）。")
}

object Faq {
  val TYPE_PLAIN_TEXT = "text"
  val TYPE_JSON = "json" //JSON格式的字符串，可以从JSON中随机选择一个答案输出

  def termString(s: String): String = termArray(s).mkString(" ")

  //  def termArray(s: String): Seq[String] =
  //    NLP.segment(s)
  //    .map(_.word.trim)
  //    .filter(_.nonEmpty)

  def termArray(s: String): Seq[String] =
    NLP.segByCnChars(s).filter(!NLP.isBiaodian(_)).toSeq

  def load(filename: String): Seq[Faq] = {
    val faqs = mutable.Set.empty[Faq]
    val lines = File(filename).lines(StandardCharsets.UTF_8).toArray

    var lineno = 0
    while (lineno < lines.size) {
      val current = lines(lineno)
      if (current.startsWith("=====") && lineno < lines.size - 1) {
        //下一行读出问题
        lineno += 1
        val question = lines(lineno)

        //读取后续的答案，直到遇到=====为止
        lineno += 1
        val sb = new StringBuilder
        while ((lineno < lines.size) &&
          !lines(lineno).startsWith("=====")) {
          sb.append(lines(lineno))
          lineno += 1
        }

        faqs += Faq(question, sb.toString, Faq.TYPE_PLAIN_TEXT, "wangxin")
      } else lineno += 1
    }

    faqs.toSeq
  }
}



