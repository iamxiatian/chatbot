package xiatian.chatbot.loader

import xiatian.chatbot.conf.Logging
import xiatian.chatbot.entity.Category

import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, XML}

object AimlLoader extends Logging {

  /**
    * 从AIML格式的XML文件中加载知识元(Category)
    *
    * @param aimlFile
    * @return
    */
  def loadFromFile(aimlFile: java.io.File): List[Category] = Try {
    val doc = XML.loadFile(aimlFile)
    val filename = Option(aimlFile.getName)

    val disabled = (doc \ "@enabled").headOption.map(_.text).getOrElse("true") == "false"

    if (disabled) {
      LOG.info(s"disalbed aiml: $aimlFile")
      List.empty[Category]
    } else {
      val categoryInTopics: List[Category] =
        (doc \ "topic")
          .map(_.asInstanceOf[Elem]).toList
          .flatMap {
            topic =>
              (topic \ "category").flatMap {
                c =>
                  parseCategoryNode(c.asInstanceOf[Elem],
                    (topic \ "@name").map(_.text).headOption,
                    filename)
              }
          }

      val categories: List[Category] =
        (doc \ "category")
          .map(_.asInstanceOf[Elem]).toList
          .flatMap(c => parseCategoryNode(c.asInstanceOf[Elem], None, filename))

      categoryInTopics ::: categories
    }
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
  def parseCategoryNode(categoryElem: Elem,
                        topic: Option[String],
                        filename: Option[String]): Option[Category] = Try {
    val patterns: Seq[Elem] = (categoryElem \ "pattern").map(_.asInstanceOf[Elem])
    val template = (categoryElem \ "template").head.asInstanceOf[Elem]
    val that: Option[String] = (categoryElem \ "that")
      .headOption
      .map(_.asInstanceOf[Elem])
      .map(_.text.trim)

    Category(patterns, that, topic, template)
  } match {
    case Success(c) => Some(c)
    case Failure(e) =>
      LOG.error(s"parse error for $categoryElem", e)
      Option.empty[Category]
  }
}
