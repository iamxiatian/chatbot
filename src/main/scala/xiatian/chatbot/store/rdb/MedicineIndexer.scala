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
object MedicineIndexer extends Logging {

  import slick.jdbc.MySQLProfile.api._

  val profile = MySQLProfile

  val db: Database = Database.forConfig("db.mysql.medicine", MyConf.config)

  def close(): Unit = {
    if (db != null)
      db.close()
  }

  /**
    * 从医药数据库导出问答对进行Faq学习
    *
    * @return
    */
  def learning(lastId: Int = 0, domain: String = "medicine"): Unit = {
    val items = readOneBatch(lastId)
    if (items.size > 0) {
      println(s"process $lastId ...")
      val faqs = items.map {
        case (_, q, a) =>
          Faq(q, a, domain)
      }
      FaqIndexer.index(faqs)
      learning(items.last._1, domain)
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
    learning(0)
  }
}
