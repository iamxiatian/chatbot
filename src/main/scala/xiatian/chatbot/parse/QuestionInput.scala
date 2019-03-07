package xiatian.chatbot.parse

import xiatian.chatbot.conf.Logging

/**
  * 对bot的聊天输入进行预处理
  *
  * @param bot
  */
object QuestionInput {
  /**
    * @TODO 把输入的文字，进行句子切分。
    * @param input
    * @return
    */
  def splitSentence(input: String): List[String] = List(input)

  /**
    * 把句子转换为词语的集合, 转换过程中，做了如下处理：
    *
    * 1. 不再区分标点符号的差异
    * 2. 一个句子最后的标点符号会被删除
    *
    * @param sentence
    * @return
    */
  def splitWords(sentence: String): List[(String, String)] = {
    val terms = Segment.seg(sentence).filter {
      term =>
        val word = term.word
        val nature = term.nature.toString

        if (word.trim.isEmpty ||
          word.length == 1 && (nature == "uj" || nature == "y")
        ) false else true
    }

    Logging.println(s"$sentence: $terms")
    val words = terms.toList.map {
      term =>
        val nature = term.nature.toString
        if (nature == "w" && term.word != "*") {
          ("<BIAODIAN>", nature)
        } else {
          (term.word, nature)
        }
    }

    //把原始句子抽取出来，即THAT之前的内容
    val (head, tail) = words.span(_._1 != "<THAT>")

    //去除一个句子最后的标点符号
    val results = head.reverse.dropWhile(_._1 == "<BIAODIAN>").reverse ::: tail
    //results.foreach(println)
    results
  }
}
