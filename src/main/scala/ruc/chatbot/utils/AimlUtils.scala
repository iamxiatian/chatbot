package ruc.chatbot.utils

import scala.xml.Node

object AimlUtils {
  def node2string(node: Node): String = {
    node.text.replaceAll("(\r\n|\n\r|\r|\n)", " ")
  }
}
