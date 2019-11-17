package xiatian.chatbot.bot.parse

import java.nio.charset.StandardCharsets

import better.files.File
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.seg.Segment
import com.hankcs.hanlp.seg.common.Term

import scala.collection.mutable.Seq

/**
  * 分词程序包装
  */
object NLP {

  import scala.collection.JavaConverters._

  //HanLP.Config.CustomDictionaryPath = Array("./data/dictionary/custom/CustomDictionary.txt")

  //println("CustomDictionary:" + HanLP.Config.CustomDictionaryPath(0))
  val hanlp: Segment = HanLP.newSegment().enableCustomDictionary(true)

  def segment(text: String): Seq[Term] = hanlp.seg(text).asScala

  /**
    * 按照汉字进行切分，例如： 我爱China => 我 / 爱 / China
    *
    * @param text
    * @return
    */
  def segByCnChars(text: String): Seq[String] = {
    val sb = new StringBuilder

    text foreach {
      ch =>
        if (isChinese(ch)) {
          sb.append(s" $ch ")
        } else sb.append(ch)
    }

    sb.toString().split(" ").filter(_.nonEmpty)
  }

  def isChinese(c: Char): Boolean = {
    val ub = Character.UnicodeBlock.of(c)

    ((ub eq Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) ||
      (ub eq Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) ||
      (ub eq Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) ||
      (ub eq Character.UnicodeBlock.GENERAL_PUNCTUATION) ||
      (ub eq Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) ||
      (ub eq Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS))
  }

  val biaodian = Set('.', '。', ',', '，', '?', '？', '!', '！', '\'', '‘', '’',
    '\"', '“', '”', '[', ']', '…')

  def isBiaodian(ch: String): Boolean = {
    ch.forall(biaodian.contains(_))
  }

  def buildCustomDict(in: File, out: File, pos: String, freq: Int): Unit = {
    val writer = out.newPrintWriter(true)
    in.lines(StandardCharsets.UTF_8).filter(_.nonEmpty).foreach {
      term =>
        writer.println(s"$term $pos $freq")
    }
    writer.close()
  }

  def main(args: Array[String]): Unit = {
    val s = segment("新疆维吾尔自治区信息化促进条例第二十八条的内容?")
    s.foreach(println)
  }
}
