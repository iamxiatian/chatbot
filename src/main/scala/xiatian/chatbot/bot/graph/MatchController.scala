package xiatian.chatbot.bot.graph

/**
  * 控制在GraphMaster树上的匹配过程的控制器
  */
object MatchController {
  val defaultMatchers = List(NullMatcher, WordMatcher, PoSMatcher, WildMatcher)

  def locate(step: MatchStep): Option[NodeMapper] = locate(step, defaultMatchers)

  /**
    * 逐个匹配matchers中的每一个匹配器，遇到第一个满足条件的匹配器则返回对应节点
    */
  private def locate(step: MatchStep,
                     matchers: List[GraphMatcher]): Option[NodeMapper] =
    matchers match {
      case matcher :: rest =>
        matcher.`match`(step) match {
          case Some(n) => Some(n)
          case None => locate(step, rest)
        }

      case Nil => None
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
  def locate(path: Option[Path],
             node: NodeMapper,
             inputThatTopic: String,
             starType: StarType,
             starIndex: Int,
             context: MatchContext
            ): Option[NodeMapper] = {
    val step = MatchStep(path, node, inputThatTopic, starType, starIndex, context)
    locate(step)
  }

}