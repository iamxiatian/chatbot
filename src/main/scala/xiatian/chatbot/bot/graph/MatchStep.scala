package xiatian.chatbot.bot.graph

/**
  * 一个匹配步骤, 记录了当前匹配的父节点，路径，以及通配符所在的位置(pattern, that, topic)
  */
case class MatchStep(path: Option[Path],
                     parentNode: NodeMapper,
                     inputThatTopic: String,
                     starType: StarType,
                     starIndex: Int,
                     context: MatchContext) {
  def word: Option[String] = path.map(_.word)

  def emptyPath(): Boolean = path.isEmpty

  def next(parent: NodeMapper,
           nextStarType: StarType,
           nextStarIndex: Int): MatchStep = MatchStep(path.get.nextPath,
    parent,
    inputThatTopic,
    nextStarType,
    nextStarIndex,
    context)
}
