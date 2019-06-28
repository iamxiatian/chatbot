package xiatian.chatbot.store.rdb

import better.files.File
import slick.jdbc.MySQLProfile
import xiatian.chatbot.conf.{Logging, MyConf}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * 直接执行MySQL语法的便捷工具
  *
  * @tparam T
  */
object MysqlShell extends Logging {

  import slick.jdbc.MySQLProfile.api._

  val profile = MySQLProfile

  val db: Database = Database.forConfig("db.mysql.baidu", MyConf.config)

  def close(): Unit = {
    if (db != null)
      db.close()
  }

  def export(): Future[Seq[(String, String)]] = db run {
    sql"""select question, answer from view_zewen"""
      .as[(String, String)]
  }

  def main(args: Array[String]): Unit = {
    //导出法规内容
    val faqs = Await.result(export(), Duration.Inf)

    val text = faqs.map {
      case (q, a) =>
        s"===============\n$q\n$a"
    }.mkString("\n")

    File("./data/faq/fagui.txt").writeText(text)
  }
}
