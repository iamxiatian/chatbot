package xiatian.chatbot.bot

import better.files.File
import xiatian.chatbot.aiml.AIMLProcessor
import xiatian.chatbot.conf.Logging
import xiatian.chatbot.graph.GraphMaster

/**
  * Class representing the AIML bot
  */
class Bot(name: String, homeDir: File) extends Logging {
  val properties = new BotProperties()

  val brain = initBrain()

  def initBrain(): GraphMaster = {
    val g = new GraphMaster(this)
    loadAIML(g)
    g
  }

  /**
    * 加载AIML
    */
  def loadAIML(g: GraphMaster) = {
    val path = File(homeDir, "aiml")
    LOG.info(s"Loading aiml from path ${path}")
    path.listRecursively.foreach {
      f =>
        if (f.isRegularFile && f.toString().toLowerCase.endsWith(".aiml")) {
          LOG.info(s"Loading aiml $f ... ")
          //读取AIML文件内容
          val categories = AIMLProcessor.loadFromFile(f.toJava)
          categories.foreach {
            c =>
              g.addCategory(c)
          }
        }
    }
    LOG.info("AIML Loaded completely.")
  }
}

object Bot {

  def main(args: Array[String]): Unit = {
    val bot = new Bot("Robot", File("./kb/alice2"))


  }
}
