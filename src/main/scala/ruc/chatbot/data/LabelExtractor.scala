package ruc.chatbot.data

import java.nio.charset.StandardCharsets
import java.util

import better.files.File
import org.apache.poi.ooxml.POIXMLDocument
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import xiatian.chatbot.util.Trie

import scala.collection.JavaConverters._

object LabelExtractor {
  def makeTrie(): Trie[String] = {
    val trie = new Trie[String]()

    val map: util.HashMap[String, String] = new util.HashMap[String, String]()
    File("./data/dict/disease.txt")
      .lineIterator(StandardCharsets.UTF_8)
      .foreach {
        w => map.put(w, w)
      }

    File("./data/dict/drug.txt")
      .lineIterator(StandardCharsets.UTF_8)
      .foreach {
        w => map.put(w, w)
      }

    File("./data/dict/symptom.txt")
      .lineIterator(StandardCharsets.UTF_8)
      .foreach {
        w => map.put(w, w)
      }

    trie.build(map)
    trie
  }

  val trie = makeTrie()

  def parse(docxFile: File) = {
    val pack = POIXMLDocument.openPackage(docxFile.toString())
    val doc = new XWPFDocument(pack)
    val extractor = new XWPFWordExtractor(doc)
    val text = extractor.getText
    doc.close()

    val labels = findLabels(text)
    println(s"parse word document: ${docxFile.name} ... ")
    println(labels.mkString(", "))
    println()
  }

  def findLabels(text: String): Set[String] = {
    val hits = trie.parseText(text)
    hits.asScala.map {
      hit =>
        hit.value
    }.toSet
  }


  def main(args: Array[String]): Unit = {
    val docFiles = File("./data/病历20190102").list.filter(_.extension.exists(_ == ".docx")).toSeq
    docFiles.foreach(
      parse(_)
    )

    //parse(docFiles.head)
  }

}
