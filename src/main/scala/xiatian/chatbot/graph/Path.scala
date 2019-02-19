package xiatian.chatbot.graph

import xiatian.chatbot.chat.QuestionInput

/**
  * Representation of Pattern Path and Input Path
  */
case class Path(word: String, nextPath: Option[Path], length: Int) {
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
  def sentenceToPath(sentence: String): Option[Path] =
    arrayToPath(QuestionInput.splitWords(sentence))

  /**
    * 把数组转换为一个Path，例如[I, love, China]，变为：
    * Path(I,    (Path(love,   Path(China, None, 1),   2),     3)
    *
    * @param words
    * @return
    */
  def arrayToPath(array: List[String]): Option[Path] =
    array match {
      case head :: tails =>
        val tailPath = arrayToPath(tails)
        val len = tailPath.map(_.length).getOrElse(0) + 1
        Option(Path(head, tailPath, len))
      case Nil =>
        None
    }

}
