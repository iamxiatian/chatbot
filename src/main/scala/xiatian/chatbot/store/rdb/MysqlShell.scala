package xiatian.chatbot.store.rdb

import better.files.File
import slick.jdbc.MySQLProfile
import xiatian.chatbot.ability.faq.{Faq, FaqIndexer}
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

  /**
    * 临时性从特定数据库导出问答对
    *
    * @return
    */
  def indexMedicine(lastId: Int = 0): Unit = {
    val items = processOneBatch(lastId)
    if (items.size > 0) {
      println(s"process $lastId ...")
      val faqs = items.map {
        case (_, q, a) =>
          Faq(q, a, Faq.TYPE_PLAIN_TEXT, "medicine")
      }
      FaqIndexer.index(faqs)
      indexMedicine(items.last._1 + 1)
    } else {
      println("DONE!")
    }
  }


  private def processOneBatch(lastId: Int = 0): Seq[(Int, String, String)] = {
    val f: Future[Seq[(Int, String, String)]] = db run (
      sql"""SELECT Id, Title, Answer FROM questions_and_answers_on_diseases WHERE Id > $lastId LIMIT 1000;"""
        .as[(Int, String, String)])

    Await.result(f, Duration.Inf)
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
