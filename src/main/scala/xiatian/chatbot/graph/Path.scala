package xiatian.chatbot.graph

/**
  * Representation of Pattern Path and Input Path
  */
case class Path(word: String, next: Option[Path], length: Int)

object Path {

  /**
    * convert a sentence (a string consisting of words separated by single spaces)
    * into a Path
    */
  def sentenceToPath(sentence: String): Option[Path] =
    arrayToPath(sentence.trim.split(" ").toList)

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
