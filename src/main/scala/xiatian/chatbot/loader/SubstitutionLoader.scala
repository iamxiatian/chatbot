package xiatian.chatbot.loader

import better.files.File
import xiatian.chatbot.conf.Logging

/**
  * 读取bot目录下的substitutions目录下的文件，问句中匹配的词语会依据该规则被替换掉
  */
object SubstitutionLoader extends Logging {
  def loadFromFile(f: File): Map[String, String] = {
    f.lines.filter(_.contains("="))
      .map(_.split("=").map(_.trim).filter(_.nonEmpty))
      .filter(_.size == 2)
      .map {
        pair => (pair(0), pair(1))
      }.toMap
  }

  /**
    * 扫描目录下的所有替换文件，合并为一个映射对结果
    * @param dir
    * @return
    */
  def loadFromPath(dir: File): Map[String, String] =
    dir.list.filter {
      f =>
        val ext = f.extension.getOrElse("")
        ext.startsWith(".sub")
    }
      .flatMap(loadFromFile(_))
      .toMap
}
