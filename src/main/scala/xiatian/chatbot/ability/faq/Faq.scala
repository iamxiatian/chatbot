package xiatian.chatbot.ability.faq

import java.nio.charset.StandardCharsets

import better.files.File
import xiatian.chatbot.parse.NLP

import scala.collection.mutable

case class Faq(question: String,
               answer: String,
               answerType: String,
               domain: String) {
  def termString(): String = Faq.termString(question)

}

object Faq {
  val TYPE_PLAIN_TEXT = "text"

  def termString(s: String): String = termArray(s).mkString(" ")

  //  def termArray(s: String): Seq[String] =
  //    NLP.segment(s)
  //    .map(_.word.trim)
  //    .filter(_.nonEmpty)

  def termArray(s: String): Seq[String] =
    NLP.segByCnChars(s).filter(!NLP.isBiaodian(_))

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



