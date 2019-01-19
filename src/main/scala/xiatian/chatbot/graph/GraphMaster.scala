package xiatian.chatbot.graph

import java.util.concurrent.atomic.AtomicInteger

import xiatian.chatbot.bot.Bot
import xiatian.chatbot.conf.Logging
import xiatian.chatbot.entity.Category

/**
  * AIML构成的知识图
  */
class GraphMaster(bot: Bot) extends Logging {
  val categoryCnt = new AtomicInteger(0)

  val root = new NodeMapper()

  private def inputThatTopic(c: Category): String =
    s"""${c.pattern} <THAT> ${c.that.getOrElse("NULL")} <TOPIC> ${c.topic.getOrElse("NULL")}"""

  def addCategory(category: Category): Unit = {
    Path.sentenceToPath(inputThatTopic(category))
      .foreach {
        path =>
          addPath(root, path, category)

          categoryCnt.intValue()
      }
  }

  /**
    * 把一个匹配模式加入到推理图中
    * @param parent
    * @param path
    * @param category
    */
  private def addPath(parent: NodeMapper, path: Path, category: Category): Unit = {
    val node = parent.getOrInsert(path.word)

    path.next match {
      case Some(p) =>
        addPath(node, p, category)
      case None =>
        //说明当前path是最后一个节点，直接把对应的category记录到节点中
        node.category = Option(category)
    }
  }


}
