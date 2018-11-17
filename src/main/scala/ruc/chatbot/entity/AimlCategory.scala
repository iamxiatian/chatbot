package ruc.chatbot.entity

import java.util.Objects

import ruc.chatbot.conf.AimlTag

import scala.xml.Node

case class AimlCategory(
                         val topic: String,
                         val pattern: String,
                         val template: Node,
                         val templateData: List[AimlToken],
                         val that: String) extends AimlElement {

  override def getType: String = AimlTag.category

  override def equals(o: Any): Boolean = if (o == this) true
  else if (!o.isInstanceOf[AimlCategory]) false
  else {
    val other = o.asInstanceOf[AimlCategory]
    Objects.equals(topic, other.topic) &&
      Objects.equals(pattern, other.pattern) &&
      Objects.equals(template, other.template) &&
      Objects.equals(that, other.that)
  }

  override def hashCode: Int = {
    var result = 1
    result = 31 * result + (if (topic == null) 0
    else topic.hashCode)

    result = 31 * result + (if (pattern == null) 0
    else pattern.hashCode)

    result = 31 * result + (if (template == null) 0
    else template.hashCode)

    result = 31 * result + (if (that == null) 0
    else that.hashCode)

    result
  }

}
