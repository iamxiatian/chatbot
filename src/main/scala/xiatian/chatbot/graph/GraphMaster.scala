package xiatian.chatbot.graph

import java.util.concurrent.atomic.AtomicInteger

import xiatian.chatbot.bot.Bot
import xiatian.chatbot.conf.{Logging, MagicValues}
import xiatian.chatbot.entity.Category

/**
  * AIML构成的知识图
  */
class GraphMaster(bot: Bot) extends Logging {
  val categoryCnt = new AtomicInteger(0)

  val root = new NodeMapper()

  private def toPathString(input: String,
                           that: String,
                           topic: String): String =
    s"""$input <THAT> $that <TOPIC> $topic"""

  private def toPathString(c: Category): String = toPathString(c.pattern,
    c.that.getOrElse(MagicValues.default_that),
    c.topic.getOrElse(MagicValues.default_topic))

  def addCategory(category: Category): Unit = {
    Path.sentenceToPath(toPathString(category))
      .foreach {
        path =>
          addPath(root, path, category)

          categoryCnt.intValue()
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

    path.next match {
      case Some(p) =>
        addPath(node, p, category)
      case None =>
        //说明当前path是最后一个节点，直接把对应的category记录到节点中
        node.category = Option(category)
    }
  }

  def `match`(input: String, that: String, topic: String): Option[NodeMapper] = {
    val pathString = toPathString(input, that, topic)
    LOG.debug(s"matching $pathString")
    val path = Path.sentenceToPath(pathString)
    //    `match`(path, pathString)
    None
  }

  /**
    * Find the matching leaf node given a path of the form
    * "{@code input <THAT> that <TOPIC> topic}"
    */
  def `match`(path: Path, inputThatTopic: String): Option[NodeMapper] = {
    None
  }

  /**
    * Depth-first search of the graph for a matching leaf node.
    * At each node, the order of search is
    * 1. $WORD  (high priority exact word match)
    * 2. # wildcard  (zero or more word match)
    * 3. _ wildcard (one or more words match)
    * 4. WORD (exact word match)
    * 5. {@code <set></set>} (AIML Set match)
    * 6. shortcut (graph shortcut when that pattern = * and topic pattern = *)
    * 7. ^ wildcard  (zero or more words match)
    * 8. * wildcard (one or more words match)
    *
    *
    * @param path           remaining path to be matched
    * @param node           current search node
    * @param inputThatTopic original input, that and topic string
    * @param starType       tells whether wildcards are in input pattern, that pattern or topic pattern
    * @param starIndex      index of wildcard
    * @param inputStars     array of input pattern wildcard matches
    * @param thatStars      array of that pattern wildcard matches
    * @param topicStars     array of topic pattern wildcard matches
    * @param matchTrace     trace of match path for debugging purposes
    * @return matching leaf node or null if no match is found
    */
  def matching(path: Option[Path],
               node: NodeMapper,
               inputThatTopic: String,
               starType: String,
               starIndex: Int,
               inputStars: Array[String],
               thatStars: Array[String],
               topicStars: Array[String],
               matchTrace: StringBuilder): Option[NodeMapper] = {
    if (path.isEmpty) Some(node) else List(wordMatching)

    {
      val n = wordMatching(path.get, node, inputThatTopic, starType, starIndex, inputStars, thatStars, topicStars, matchTrace)
      if (n.nonEmpty) {
        n
      } else None
    }

    //    Nodemapper matchedNode;
    //    //log.info("Match: Height="+node.height+" Length="+path.length+" Path="+Path.pathToSentence(path));
    //    matchCount++;
    //    if ((matchedNode = nullMatch(path, node, matchTrace)) != null) return matchedNode;
    //    else if (path.length < node.height) {
    //      return null;}
    //
    //    else if ((matchedNode = dollarMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = sharpMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = underMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = wordMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = setMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = shortCutMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = caretMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else if ((matchedNode = starMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
    //    else {
    //      return null;
    //    }

    None
  }

  /**
    * 按照词语在树上进行比较
    */
  def wordMatching(path: Path,
                   node: NodeMapper,
                   inputThatTopic: String,
                   starType: String,
                   starIndex: Int,
                   inputStars: Array[String],
                   thatStars: Array[String],
                   topicStars: Array[String],
                   matchTrace: StringBuilder): Option[NodeMapper] = {
    val word = path.word
    val (nextStarType: String, nextStarIndex: Int) =
      if (word == "<THAT>") ("thatStar", 0)
      else if (word == "<TOPIC") ("topicStar", 0)
      else (starType, starIndex)

    matchTrace.append(s"[$word, $word]")
    NodeMapper.locateChild(node, word).flatMap {
      child =>
        matching(path.next, child, inputThatTopic,
          nextStarType, nextStarIndex,
          inputStars, thatStars, topicStars,
          matchTrace)
    }

  }
}
