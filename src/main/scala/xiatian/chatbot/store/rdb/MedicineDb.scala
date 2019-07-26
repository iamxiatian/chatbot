package xiatian.chatbot.store.rdb

import better.files.File
import slick.jdbc.MySQLProfile
import xiatian.chatbot.ability.faq.{Faq, FaqIndexer}
import xiatian.chatbot.conf.{Logging, MyConf}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * 处理医药数据
  *
  */
object MedicineDb extends Logging {

  import slick.jdbc.MySQLProfile.api._

  val profile = MySQLProfile

  val db: Database = Database.forConfig("db.mysql.medicine", MyConf.config)

  def close(): Unit = {
    if (db != null)
      db.close()
  }

  /**
    * 临时性从特定数据库导出问答对
    *
    * @return
    */
  def indexMedicine(lastId: Int = 0): Unit = {
    val items = readOneBatch(lastId)
    if (items.size > 0) {
      println(s"process $lastId ...")
      val faqs = items.map {
        case (_, q, a) =>
          Faq(q, a, domain = "Medicine")
      }
      FaqIndexer.index(faqs)
      indexMedicine(items.last._1 + 1)
    } else {
      println("DONE!")
    }
  }


  private def readOneBatch(lastId: Int = 0): Seq[(Int, String, String)] = {
    val f: Future[Seq[(Int, String, String)]] = db run (
      sql"""SELECT Id, Title, Answer FROM questions_and_answers_on_diseases WHERE Id > $lastId LIMIT 1000;"""
        .as[(Int, String, String)])

    Await.result(f, Duration.Inf)
  }

  def main(args: Array[String]): Unit = {
    indexMedicine(0)
  }
}
