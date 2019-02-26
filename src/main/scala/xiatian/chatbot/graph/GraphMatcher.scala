package xiatian.chatbot.graph

/**
  * 匹配处理的特质，包括：
  * 基于字完整匹配的WordMatcher
  *
  */
trait GraphMatcher {
  def `match`(step: MatchStep): Option[NodeMapper]
}

object NullMatcher extends GraphMatcher {
  override def `match`(step: MatchStep): Option[NodeMapper] =
    if (step.emptyPath()) {
      Some(step.parentNode)
    } else None
}

object WordMatcher extends GraphMatcher {
  /**
    * 按照词语在树上进行比较
    */
  override def `match`(step: MatchStep): Option[NodeMapper] =
    step.path match {
      case Some(path) =>
        val word = path.word
        val (nextStarType: StarType, nextStarIndex: Int) =
          if (word == "<THAT>") (ThatStar, 0)
          else if (word == "<TOPIC>") (TopicStar, 0)
          else (step.starType, step.starIndex)

        //定位当前词语word所在的节点，然后进行匹配
        NodeMapper.locateChild(step.parentNode, word).flatMap {
          child =>
            val nextStep = step.next(child, nextStarType, nextStarIndex)
            MatchController.locate(nextStep)
        }
      case None =>
        //到达结尾，说明之前的节点是目标节点，直接返回
        Some(step.parentNode)
    }
}


/**
  * 根据词性标签进行匹配
  */
object PoSMatcher extends GraphMatcher {
  /**
    * 按照词语在树上进行比较
    */
  override def `match`(step: MatchStep): Option[NodeMapper] =
    step.path match {
      case Some(path) =>
        val word = path.word
        val pos = path.tag

        val (nextStarType: StarType, nextStarIndex: Int) =
          if (word == "<THAT>") (ThatStar, 0)
          else if (word == "<TOPIC") (TopicStar, 0)
          else (step.starType, step.starIndex)

        //定位当前词语word所在的节点，然后进行匹配
        NodeMapper.locateChild(step.parentNode, s"pos_$pos").flatMap {
          child =>
            val context = step.context
            context.appendStarWord(step.starIndex, path.word, step.starType)

            val nextStep = MatchStep(path.nextPath, child, step.inputThatTopic,
              step.starType, step.starIndex + 1, context)
            
            MatchController.locate(nextStep)
        }
      case None =>
        //到达结尾，说明之前的节点是目标节点，直接返回
        Some(step.parentNode)
    }
}

/**
  * 通配符匹配
  */
object WildMatcher extends GraphMatcher {
  /**
    * 按照通配符在树上进行比较
    */
  override def `match`(step: MatchStep): Option[NodeMapper] =
    step.path match {
      case Some(path) =>
        val word = path.word
        if (word == "<THAT>" || word == "<TOPIC>") {
          None
        } else {
          NodeMapper.locateChild(step.parentNode, "*").flatMap {
            child =>
              // 可以匹配Path中的所有word，直到遇到that或者topic为止
              //或者star的下一个节点中，存在和path中的下一个单词相同的词语
              //说明star命中了问句中的一部分。
              //例如： pattern: what's your * name?
              val context = step.context
              context.appendStarWord(step.starIndex, path.word, step.starType)

              //持续扫描path的后续内容，直到遇到新匹配节点为止
              var p = path.nextPath
              var matchedNode = Option.empty[NodeMapper]

              var stop = false
              while (!stop && p.nonEmpty) {
                val nextStep = MatchStep(p, child, step.inputThatTopic, step.starType, step.starIndex + 1, context)
                matchedNode = MatchController.locate(nextStep)
                if (matchedNode.nonEmpty) {
                  stop = true
                } else {
                  context.appendStarWord(step.starIndex, p.get.word, step.starType)
                  p = p.get.nextPath
                }
              }

              matchedNode
          }
        }
      case None =>
        //到达结尾，说明之前的节点是目标节点，直接返回
        Some(step.parentNode)
    }
}