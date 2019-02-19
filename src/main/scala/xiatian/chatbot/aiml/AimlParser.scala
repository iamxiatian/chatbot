package xiatian.chatbot.aiml

import xiatian.chatbot.conf.Logging
import xiatian.chatbot.graph.MatchContext

import scala.util.Random
import scala.xml.{Elem, Node, Text}

/**
  * he core AIML parser and interpreter.
  * Implements the AIML 2.0 specification as described in
  * AIML 2.0 Working Draft document
  */
object AimlParser extends Logging {
  def parseTemplate(templateDoc: Elem, context: MatchContext): String = {
    recurseParse(templateDoc, context).trim
  }

  /**
    * 递归解析模板元素
    *
    * @param doc
    * @return
    */
  def recurseParse(doc: Node, context: MatchContext): String = {
    doc.child.map {
      case t: Text =>
        t.text.trim

      case c: Elem if c.label == "random" =>
        parseRandom(c, context)

      case c: Elem if c.label == "star" =>
        parseStar(c, context)

      case c: Elem if c.label == "date" =>
        DateParser.parse(c, context)

      case c: Elem =>
        println(s"unknown tag: ${c.label}")
        //递归解析每一个子节点
        c.child.map(recurseParse(_, context)).mkString(" ")

      case _ => ""
    }.mkString(" ")
  }

  /**
    * 解析random和li元素，例如：
    * <random>
    * <li>Hello!</li>
    * <li>Well hello there.</li>
    * <li>Howdy.</li>
    * <li>Good day.</li>
    * <li>Hi, friend.</li>
    * </random>
    *
    * @param randomNode
    * @return
    */
  def parseRandom(randomNode: Elem, context: MatchContext): String = {
    val items = randomNode \ "li"
    if (items.size == 0) {
      ""
    } else {
      val item = items(Random.nextInt(items.size))
      recurseParse(item.asInstanceOf[Elem], context)
    }
  }

  /**
    * <category>
    * <pattern>MY FAVORITE * IS *</pattern>
    * <template>Your favorite <star /> is <star index="2" /></template>
    * </category>
    *
    * <!--
    *
    * Input: My favorite color is blue
    * Output: Your favorite color is blue
    *
    * -->
    *
    * @param node
    * @return
    */
  def parseStar(node: Elem, context: MatchContext): String = {
    val index = (node \ "@index").headOption.map(_.text).getOrElse("1").toInt
    context.inputStars(index - 1)
  }
}
