package xiatian.chatbot.graph

/**
  * 控制在GraphMaster树上的匹配过程的控制器
  */
object MatchController {
  val defaultMatchers = List(WordMatcher)

  def locate(step: MatchStep): Option[NodeMapper] = locate(step, defaultMatchers)

  /**
    * 逐个匹配matchers中的每一个匹配器，遇到第一个满足条件的匹配器则返回对应节点
    */
  def locate(step: MatchStep,
             matchers: List[GraphMatcher]): Option[NodeMapper] =
    matchers match {
      case matcher :: rest =>
        matcher.matches(step) match {
          case Some(n) => Some(n)
          case None => locate(step, rest)
        }

      case Nil => None
    }
}