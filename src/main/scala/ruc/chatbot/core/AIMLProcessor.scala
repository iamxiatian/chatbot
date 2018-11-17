package ruc.chatbot.core

import java.util.regex.Pattern

import org.slf4j.LoggerFactory.getLogger
import ruc.chatbot.bot.BotInfo
import ruc.chatbot.conf.{AimlConst, AimlTag, Wildcard}
import ruc.chatbot.entity.AimlCategory
import ruc.chatbot.utils.RandomUtils

import scala.collection.mutable
import scala.xml.Node

/**
  * AIML Processor:
  */
class AIMLProcessor(
                     val categories: List[AimlCategory],
                     val topics: Map[String, Map[String, AimlCategory]],
                     predicates: Map[String, String],
                     botInfo: BotInfo
                   ) {
  val log = getLogger(classOf[AIMLProcessor])

  /**
    * convert category(contains topic information) to topics
    */
  def convert(categories: List[AimlCategory]): Map[String, mutable.Map[String, AimlCategory]] = {
    val topics = mutable.Map.empty[String, mutable.Map[String, AimlCategory]]

    categories foreach {
      category =>
        val topicCategories = topics.getOrElseUpdate(category.topic, mutable.Map.empty)
        topicCategories += (category.pattern -> category)
    }

    topics.toMap
  }

  val topicCount: Int = topics.size

  val categoriesCount: Int = categories.size

  def getPatterns(topic: String): Set[String] = topics.get(topic) match {
    case Some(cs) =>
      cs.keySet
    case None => Set.empty[String]
  }

  /**
    * get Aiml Category under specified topic and pattern.
    */
  def getCategory(topic: String, pattern: String): Option[AimlCategory] =
    topics.get(topic).flatMap(_.get(pattern))

  def getTemplateValue(node: Node, stars: List[String]): String = {
    val values = node.child.map {
      child =>
        recurseParse(child, stars)
    }

    if (values.isEmpty)
      AimlConst.default_bot_response
    else
      values.mkString("\n")
  }

  private def recurseParse(node: Node, stars: List[String]): String = {
    node.label match {
      case AimlTag.text =>
        textParse(node)
      case AimlTag.template =>
        getTemplateValue(node, stars)
      case AimlTag.random =>
        randomParse(node)
      case AimlTag.srai =>
        sraiParse(node, stars)
      case AimlTag.set =>
        setParse(node, stars)
      case AimlTag.bot =>
        botInfoParse(node, stars)
      case AimlTag.star =>
        starParse(node, stars)
      case AimlTag.think =>
        log.error("think tag!")
        getTemplateValue(node, stars)
      case _ =>
        log.error(s"Unknown tag ${
          node.label
        }")
        ""
    }
  }

  def starParse(node: Node, stars: List[String]): String =
    if (stars.isEmpty)
      ""
    else {
      node.attribute("index").map(_.text) match {
        case Some(idx) =>
          stars(idx.toInt - 1)
        case None =>
          stars(0)
      }
    }

  def botInfoParse(node: Node, stars: List[String]): String = {
    node.attribute("name").map(_.text) match {
      case Some(param) =>
        botInfo.getValue(param)
      case None =>
        log.error("no name attribute of botInfoParse.")
        ""
    }
  }

  def setParse(node: Node, stars: List[String]) = {
    //@TODO
    println("Not implemented setParse....")
    ""
  }

  def sraiParse(node: Node, stars: List[String]): String = {
    getCategory(AimlConst.default_topic, AimlUtils.node2string(node)) match {
      case Some(c) =>
        getTemplateValue(c.template, stars)
      case None =>
        AimlConst.error_bot_response
    }
  }

  def textParse(node: Node): String =
    node.text.replaceAll("(\r\n|\n\r|\r|\n)", "").replaceAll("  ", " ")

  /**
    * 从一系列li中随机挑选一个结果
    *
    * @param node
    * @return
    */
  def randomParse(node: Node): String = {
    val values = (node \ "li") map (_.text)
    RandomUtils.pick(values)
  }

  /**
    * 匹配输出的问句，返回与问句匹配的Pattern和抽取出的问句中的星号信息
    *
    * @param input
    * @param topic
    * @param that
    * @return 返回一个二元组，第1元素为命中的pattern，第2个元素为通配符对应的字符串
    */
  def `match`(input: String, topic: String, that: String): (String, Seq[String]) = {
    val request = input.toUpperCase()
    val patterns = getPatterns(topic)

    var wildcard: Option[String] = None
    var stars = Seq.empty[String]

    patterns.find {
      pattern =>
        if (Wildcard.matchOne(pattern)) {
          //发现通配符，记录后，继续查找
          wildcard = Option(pattern)
          false
        } else {
          val (matched, stars) = isMatching(request, pattern)
          matched
          //          if (matched) {
          //            //lastStars = stars
          //            true
          //          } else false
        }
    }

    wildcard match {
      case Some(p) => (p, stars)
      case None => (Wildcard.ZeroMore, List.empty[String])
    }
  }

  /**
    * @return 返回是否一个二元组，第一个表示是否匹配成功，第2个表示匹配成功时，
    *         发现的通配符对应的信息
    */
  def isMatching(input: String, pattern: String): (Boolean, Seq[String]) = {
    val regex = pattern.replace(Wildcard.OneMorePriority, "(.+)")
      .replace(Wildcard.OneMore, "(.+)")
      .replace(Wildcard.ZeroMorePriority, "(.*)")
      .replace(Wildcard.ZeroMore, "(.*)")

    val p = Pattern.compile(regex)
    val m = p.matcher(input.trim)
    if (m.matches()) {
      val stars = (1 to m.groupCount()).map(m.group(_).toLowerCase)
      (true, stars)
    } else
      (false, List.empty[String])
  }
}
