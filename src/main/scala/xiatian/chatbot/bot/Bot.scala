package xiatian.chatbot.bot

import better.files.File
import xiatian.chatbot.aiml.AimlParser
import xiatian.chatbot.conf.Logging
import xiatian.chatbot.graph.GraphMaster

/**
  * Class representing the AIML bot
  */
class Bot(name: String, homeDir: File) extends Logging {
  val properties = new BotProperties()

  val brain: GraphMaster = {
    val g = new GraphMaster(this)
    g.loadAIML(File(homeDir, "aiml"))
  }

  def respond(input: String, that: String, topic: String): Option[String] = {
    val matchResult = brain.locate(input, that, topic)
    matchResult.node.map {
      leaf =>
        leaf.category match {
          case Some(c) =>
            AimlParser.eval(c.template).get
          case None =>
            "对不起，我没有明白~"
        }
    }
  }
}
