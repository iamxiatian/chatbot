package xiatian.chatbot.parse

import java.nio.charset.StandardCharsets

import better.files.File
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.seg.Segment
import com.hankcs.hanlp.seg.common.Term

import scala.collection.JavaConverters._

/**
  * 分词程序包装
  */
object Segment {
  lazy val hanlp: Segment = HanLP.newSegment()

  def seg(text: String): Seq[Term] = hanlp.seg(text).asScala

  def buildCustomDict(in: File, out: File, pos: String, freq: Int): Unit = {
    val writer = out.newPrintWriter(true)
    in.lines(StandardCharsets.UTF_8).filter(_.nonEmpty).foreach {
      term =>
        writer.println(s"$term $pos $freq")
    }
    writer.close()
  }

  def main(args: Array[String]): Unit = {
//    buildCustomDict(File("./data/dict/disease.txt"),
//      File("./data/dictionary/custom/disease.txt"), "bing", 100)
    println(seg("口腔修复和戒毒事情"))
  }

}
