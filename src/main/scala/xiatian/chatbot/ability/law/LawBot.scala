package xiatian.chatbot.ability.law

import java.nio.charset.StandardCharsets

import better.files.File
import xiatian.chatbot.conf.{Logging, MyConf}
import xiatian.chatbot.util.Trie
import xiatian.chatbot.util.Trie.Hit

/**
  * 法规问答
  */
object LawBot extends Logging {
  val enabled = MyConf.getBoolean("bot.law.enabled")

  private def load(): Trie[Int] = {
    val trie = new Trie[Int]()

    //读取名字到id的映射到Map中
    val pairs = new java.util.HashMap[String, Int]()
    File("./data/regulation/id_name.txt").lines(StandardCharsets.UTF_8)
      .filter(_.nonEmpty)
      .foreach {
        line =>
          val items = line.split(",").map(_.trim).filter(_.nonEmpty)
          val id = items.head.toInt
          items.tail.foreach {
            name =>
              pairs.put(name, id)
          }
      }

    trie.build(pairs)

    trie
  }

  //法规名称到法规ID的映射
  def trie = load()

  val articles: Map[String, String] = File("./data/regulation/law_article.txt")
    .lines(StandardCharsets.UTF_8)
    .filter(_.nonEmpty)
    .map {
      line =>
        val pos = line.indexOf(" ")
        val key = line.substring(0, pos).trim
        val value = line.substring(pos + 1).trim
        (key, value)
    }.toMap

  def getLawArticle(lawId: Int, number: Int): Option[String] = {
    val key = s"${lawId}-${number}"
    articles.get(key)
  }

  def request(input: String): Option[String] = if (!enabled) None else {
    val hit: Hit[Int] = trie.findFirst(input)
    if (hit != null) {
      val lawId = hit.value
      //查看后面有没有数字，有的话，认为是问法条
      val rest = input.substring(hit.end)
      val lawName = input.substring(hit.begin, hit.end)

      "(\\d+)条".r.findFirstMatchIn(rest) match {
        case Some(m) =>
          //找到法条了
          val number = m.group(1).toInt
          Some(getLawArticle(lawId, number).getOrElse(s"抱歉，${lawName}第${number}条暂时没有内容。"))
        case None =>
          None
      }
    } else None
  }

  def main(args: Array[String]): Unit = {
    request("刑法第288条") foreach println
  }
}
