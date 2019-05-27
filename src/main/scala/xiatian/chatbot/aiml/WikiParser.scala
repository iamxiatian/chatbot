package xiatian.chatbot.aiml

import xiatian.chatbot.aiml.AimlParser.recurseParse
import xiatian.chatbot.db.MedicineDb
import xiatian.chatbot.graph.MatchContext

import scala.xml.Elem

object WikiParser {
  /**
    * ```xml
    * <category>
    * <pattern><term pos="yao"/></pattern>
    * <template><wiki type="default">六味地黄丸</wiki></template>
    * </category>
    * ```
    *
    * @param node
    * @return
    */
  def parse(node: Elem, context: MatchContext): String = {
    val content = recurseParse(node, context).trim
    val `type` = (node \ "@type").headOption.map(_.text).getOrElse("default")
    `type` match {
      case "yao" =>
        MedicineDb.getYao(content).getOrElse(s"尚无解释:$content")
      case _ =>
        content
    }

  }

}
