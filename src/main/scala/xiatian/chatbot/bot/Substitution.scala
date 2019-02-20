package xiatian.chatbot.bot

import better.files.File
import xiatian.chatbot.conf.Logging
import xiatian.chatbot.loader.SubstitutionLoader
import xiatian.chatbot.util.Trie

import scala.collection.JavaConverters._
import scala.collection.mutable

case class Substitution(pairs: Map[String, String]) extends Logging {
  val trie = new Trie[String]()
  trie.build(pairs.asJava)

  def normalize(text: String): String = {
    val hits = trie.parseText(text)
    var sb = new mutable.StringBuilder()
    var lastPos = 0

    hits.forEach {
      hit =>
        val part1 = text.drop(lastPos).take(hit.begin - lastPos)
        sb.append(part1)
        sb.append(hit.value)
        lastPos = hit.end
    }

    if (lastPos < text.length) sb.append(text.substring(lastPos))

    sb.toString()
  }
}

object Substitution {
  def apply(dir: File): Substitution =
    new Substitution(SubstitutionLoader.loadFromPath(dir))

//  def main(args: Array[String]): Unit = {
//    val s = Substitution(Map("china" -> "OK", "usa" -> "uuu"))
//    println(s.normalize(""))
//  }
}
