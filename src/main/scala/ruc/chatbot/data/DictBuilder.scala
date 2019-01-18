package ruc.chatbot.data

import java.nio.charset.StandardCharsets

import better.files.File

import scala.collection.mutable

object DictBuilder {
  val DRUG_SUFFIX = Set("片", "膜", "囊", "液", "膏", "素", "钠", "剂", "酮", "醛", "酊", "酚",
    "肽", "氟", "酸", "碱", "丸", "粒", "醇", "胶", "乳", "浆", "丹", "呤", "茶", "栓",
    "汤", "酯", "氮", "粉", "钾", "安", "啶", "韦", "酶", "唑", "素", "铵", "肼", "唑",
    "烷", "散", "精", "草", "钙", "霜", "露")

  def parseDrug(f: File) = {
    val cache = mutable.Map.empty[String, Int]

    f.lineIterator(StandardCharsets.UTF_8)
      .filter {
        line =>
          line.startsWith("<Drug_name>") || line.startsWith("<Catalog>")
      }
      .map {
        line =>
          line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")).trim
      }
      .filter {
        line =>
          val suffix = line.substring(line.size - 1)
          DRUG_SUFFIX.contains(suffix)
      }
      .toSet[String]
      .foreach {
        line =>
          println(line)
      }
  }

  def parseDisease(f: File) = {
    val cache = mutable.Map.empty[String, Int]

    f.lineIterator(StandardCharsets.UTF_8)
      .filter {
        line =>
          line.startsWith("<Name_of_disease>") || line.startsWith("<Disease_category>")
      }
      .map {
        line =>
          line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")).trim
      }
//      .filter {
//        line =>
//          val suffix = line.substring(line.size - 1)
//          DRUG_SUFFIX.contains(suffix)
//      }
      .toSet[String]
      .foreach {
        line =>
          println(line)
      }

  }

  def parse(f: File) = {
    val cache = mutable.Map.empty[String, Int]

    f.lineIterator(StandardCharsets.UTF_8)
      .filter {
        line =>
          line.startsWith("<Symptom_name>")
      }
      .map {
        line =>
          line.substring(line.indexOf(">") + 1, line.lastIndexOf("<")).trim
      }
      //      .filter {
      //        line =>
      //          val suffix = line.substring(line.size - 1)
      //          DRUG_SUFFIX.contains(suffix)
      //      }
      .toSet[String]
      .foreach {
        line =>
          println(line)
      }

  }

  def main(args: Array[String]): Unit = {
    parse(File("/home/xiatian/workspace/github/chatbot/data/symptom.xml"))
  }

}
