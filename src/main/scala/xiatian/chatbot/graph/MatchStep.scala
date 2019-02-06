package xiatian.chatbot.graph

/**
  * 一个匹配步骤
  */
case class MatchStep(path: Path,
                     node: NodeMapper,
                     inputThatTopic: String,
                     starType: StarType,
                     starIndex: Int,
                     inputStars: Array[String],
                     thatStars: Array[String],
                     topicStars: Array[String],
                     matchTrace: StringBuilder) {
  def word = path.word

  def trace(msg: String) = matchTrace.append(msg)
}
