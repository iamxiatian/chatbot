package xiatian.chatbot.bot

import better.files.File
import xiatian.chatbot.aiml.AimlParser
import xiatian.chatbot.conf.{Logging, MyConf}
import xiatian.chatbot.graph.GraphMaster

/**
  * Class representing the AIML bot
  */
class Bot(name: String) extends Logging {
  val botDir: File = File(MyConf.botsLocation, name)

  //加载当前bot的配置信息
  val info = BotInfo(File(botDir, "bot.conf"))

  val brain: GraphMaster = {
    val g = new GraphMaster(this)
    g.loadAIML(File(botDir, "aiml"))
  }

  def reload(): Unit = {
    brain.clearMemory()
    brain.loadAIML(File(botDir, "aiml"))
  }

  def respond(input: String, that: String, topic: String): Option[String] = {
    val matchResult = brain.locate(input, that, topic)
    matchResult.node.map {
      leaf =>
        leaf.category match {
          case Some(c) =>
            AimlParser.parseTemplate(c.template, matchResult.context)
          case None =>
            "对不起，我没有明白~"
        }
    }
  }
}

object Bot {
  def apply(): Bot = new Bot(MyConf.botDefault)

  def apply(name: String): Bot = new Bot(name)
}
