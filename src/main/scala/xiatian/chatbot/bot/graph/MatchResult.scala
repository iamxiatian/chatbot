package xiatian.chatbot.bot.graph

import scala.collection.mutable

/**
  * 匹配结果的上下文，包含匹配命中的通配符对应的具体信息
  */
case class MatchContext() {
  val inputStars = mutable.Map.empty[Int, String]

  val thatStars = mutable.Map.empty[Int, String]

  val topicStars = mutable.Map.empty[Int, String]

  private def appendStarWord(stars: mutable.Map[Int, String], idx: Int, word: String): Unit = {
    val old = stars.get(idx).map(_ + " ").getOrElse("")
    stars.put(idx, s"$old$word")
  }

  /**
    * 把第idx个星号统配的单词，加入到对应的变量中
    * @param idx
    * @param word
    * @param starType
    * @return
    */
  def appendStarWord(idx: Int, word: String, starType: StarType): MatchContext = {
    starType match {
      case InputStar =>
          appendStarWord(inputStars, idx, word)
      case ThatStar =>
        appendStarWord(thatStars, idx, word)
      case TopicStar =>
        appendStarWord(topicStars, idx, word)
    }
    this
  }

}

case class MatchResult(node: Option[NodeMapper],
                       context: MatchContext) {
  def found() = node.nonEmpty
}