package xiatian.chatbot.graph

/**
  * 匹配处理的特质，包括：
  * 基于字完整匹配的WordMatcher
  *
  */
trait GraphMatcher {
  def `match`(step: MatchStep): Option[NodeMapper]
}


object WordMatcher extends GraphMatcher {
  /**
    * 按照词语在树上进行比较
    */
  def `match`(step: MatchStep): Option[NodeMapper] = {
    val word = step.word
    val (nextStarType: StarType, nextStarIndex: Int) =
      if (word == "<THAT>") (ThatStarType, 0)
      else if (word == "<TOPIC") (TopicStarType, 0)
      else (step.starType, step.starIndex)

    step.trace(s"[$word, $word]")

    val nextStep = step
    //定位当前词语word所在的节点，然后进行匹配
    NodeMapper.locateChild(step.node, word).flatMap {
      child =>
        MatchController.locate(nextStep)
      //        MatchController.locate(step.path.next, child, inputThatTopic,
      //          nextStarType, nextStarIndex,
      //          inputStars, thatStars, topicStars,
      //          matchTrace)
    }

  }
}