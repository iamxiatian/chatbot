package xiatian.chatbot.bot.graph

import xiatian.chatbot.bot.Substitution
import xiatian.chatbot.bot.parse.QuestionInput

/**
  * Representation of Pattern Path and Input Path
  */
case class Path(word: String,
                tag: String, //词性标记
                nextPath: Option[Path],
                length: Int) {
  def hasNext(): Boolean = nextPath.nonEmpty

  def isLast() = nextPath.isEmpty

  def next(): Path = nextPath.get

  def nextOption(): Option[Path] = nextPath
}

object Path {

  /**
    * convert a sentence (a string consisting of words separated by single spaces)
    * into a Path
    */
  def toPath(sentence: String): Option[Path] = {
    arrayToPath(QuestionInput.splitWords(sentence))
  }

  //  def categoryToPath(pattern: String,
  //                     that: String,
  //                     topic: String): Option[Path] = {
  //    arrayToPath(QuestionInput.splitWords(pattern))
  //  }


  /**
    * 把数组转换为一个Path，例如[I, love, China]，变为：
    * Path(I,    (Path(love,   Path(China, None, 1),   2),     3)
    *
    * @param words
    * @return
    */
  def arrayToPath(array: List[(String, String)]): Option[Path] =
    array match {
      case head :: tails =>
        val tailPath = arrayToPath(tails)
        val len = tailPath.map(_.length).getOrElse(0) + 1
        Option(Path(head._1, head._2, tailPath, len))
      case Nil =>
        None
    }

  def toPath(input: String,
             that: String,
             topic: String)
            (implicit substitution: Substitution): Option[Path] = {
    val s = toPathString(input, that, topic)
    toPath(s)
  }

  def toPathString(input: String,
                   that: String,
                   topic: String)(implicit substitution: Substitution) = {
    val s1 = substitution.normalize(input)
    val s2 = substitution.normalize(that)
    val s3 = substitution.normalize(topic)
    s"""$s1 <THAT> $s2 <TOPIC> $s3"""
  }


  //  def toPathString(c: Category)(implicit substitution: Substitution): String = toPathString(c.pattern,
  //    c.that.getOrElse(MagicValues.default_that),
  //    c.topic.getOrElse(MagicValues.default_topic))

}
