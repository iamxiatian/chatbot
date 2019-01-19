package xiatian.chatbot.aiml

import xiatian.chatbot.conf.Logging
import xiatian.chatbot.entity.Category

import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, XML}

/**
  * he core AIML parser and interpreter.
  * Implements the AIML 2.0 specification as described in
  * AIML 2.0 Working Draft document
  */
object AIMLProcessor extends Logging {

  /**
    * generate a bot response to a single sentence input.
    *
    * @param input       input statement.
    * @param that        bot's last reply.
    * @param topic       current topic.
    * @param chatSession current client chat session.
    * @param srCnt       number of <srai> activations.
    * @return bot's reply.
    */
  def respond(input: String,
              that: String,
              topic: String,
              srCnt: Int = 0
             ): Option[String] = {

    None
  }

  /**
    * 从AIML格式的XML文件中加载知识元(Category)
    *
    * @param aimlFile
    * @return
    */
  def loadFromFile(aimlFile: java.io.File): List[Category] = Try {
    val doc = XML.loadFile(aimlFile)
    val filename = Option(aimlFile.getName)

    val categoryInTopics: List[Category] =
      (doc \ "topic")
        .map(_.asInstanceOf[Elem]).toList
        .flatMap {
          topic =>
            (topic \ "category").map {
              c =>
                parseCategory(c.asInstanceOf[Elem],
                  (topic \ "@name").map(_.text).headOption,
                  filename)
            }
        }

    val categories: List[Category] =
      (doc \ "category")
        .map(_.asInstanceOf[Elem]).toList
        .map(c => parseCategory(c.asInstanceOf[Elem], None, filename))

    categoryInTopics ::: categories
  } match {
    case Success(r) => r
    case Failure(e) =>
      LOG.error(s"加载aiml错误$aimlFile", e)
      List.empty[Category]
  }


  /**
    * 解析XML中的category节点
    *
    * @param categoryElem 当前处理的category
    * @param topic        当前cateogry所隶属的topic
    * @param filename     当前category所隶属的文件
    */
  def parseCategory(categoryElem: Elem,
                    topic: Option[String],
                    filename: Option[String]): Category = {
    val pattern = (categoryElem \ "pattern").head.asInstanceOf[Elem].text.trim
    val template = (categoryElem \ "template").head.asInstanceOf[Elem].text.trim
    val that: Option[String] = (categoryElem \ "pattern").headOption
      .map(_.asInstanceOf[Elem]).map(_.text.trim)

    new Category(pattern, that, topic, template, filename)
  }
}
