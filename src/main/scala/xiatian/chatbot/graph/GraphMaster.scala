package xiatian.chatbot.graph

import java.util.concurrent.atomic.AtomicInteger

import better.files.File
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.conf.{Logging, MagicValues}
import xiatian.chatbot.entity.Category
import xiatian.chatbot.loader.AimlLoader

/**
  * AIML构成的知识图, 即聊天机器人的大脑
  */
class GraphMaster(bot: Bot) extends Logging {
  val categoryCnt = new AtomicInteger(0)

  private val root = new NodeMapper()

  /**
    * 清楚掉大脑之前的记忆，重新加载知识时需要该操作
    */
  def clearMemory() = {
    root.branches.clear()
  }

  /**
    * 加载AIML
    */
  def loadAIML(path: File): GraphMaster = {
    LOG.info(s"Loading aiml from path ${path}")
    path.listRecursively.foreach {
      f =>
        if (f.isRegularFile && f.toString().toLowerCase.endsWith(".aiml")) {
          LOG.info(s"Loading aiml $f ... ")
          //读取AIML文件内容
          val categories = AimlLoader.loadFromFile(f.toJava)
          categories.foreach {
            c =>
              addCategory(c)
          }
        }
    }
    LOG.info("AIML Loaded completely.")
    this
  }

  private def toPathString(input: String,
                           that: String,
                           topic: String): String =
    s"""$input <THAT> $that <TOPIC> $topic"""

  private def toPathString(c: Category): String = toPathString(c.pattern,
    c.that.getOrElse(MagicValues.default_that),
    c.topic.getOrElse(MagicValues.default_topic))

  /**
    * 将一个知识类别加入到内存知识树之中
    *
    * @param category
    */
  def addCategory(category: Category): Unit = {
    Path.sentenceToPath(toPathString(category))
      .foreach {
        path =>
          addPath(root, path, category)

          categoryCnt.incrementAndGet()
      }
  }

  /**
    * 把一个匹配模式加入到推理图中
    *
    * @param parent
    * @param path
    * @param category
    */
  private def addPath(parent: NodeMapper, path: Path, category: Category): Unit = {
    val node = parent.getOrInsert(path.word)

    path.nextPath match {
      case Some(p) =>
        addPath(node, p, category)
      case None =>
        //说明当前path是最后一个节点，直接把对应的category记录到节点中
        node.category = Option(category)
    }
  }

  def locate(input: String, that: String, topic: String): MatchResult = {
    val pathString: String = toPathString(input, that, topic)
    LOG.debug(s"matching $pathString")
    val path: Option[Path] = Path.sentenceToPath(pathString)
    //    `match`(path, pathString)

    val context = MatchContext()

    val node = MatchController.locate(path, root, pathString,
      InputStar, 0, context)

    MatchResult(node, context)
  }
}
