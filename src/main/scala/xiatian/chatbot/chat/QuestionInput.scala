package xiatian.chatbot.chat

import com.hankcs.hanlp.HanLP

import scala.collection.JavaConverters._

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
  def splitWords(sentence: String): List[String] = {
    val terms = HanLP.segment(sentence).asScala.filter(_.word.trim.nonEmpty)
    //println(s"$sentence: $terms")
    val words = terms.toList.map {
      term =>
        if (term.nature.toString == "w" && term.word != "*") {
          "<BIAODIAN>"
        } else {
          term.word
        }
    }

    //把原始句子抽取出来，即THAT之前的内容
    val (head, tail) = words.span(_ != "<THAT>")

    //去除一个句子最后的标点符号
    head.reverse.dropWhile(_ == "<BIAODIAN>").reverse ::: tail
  }
}
