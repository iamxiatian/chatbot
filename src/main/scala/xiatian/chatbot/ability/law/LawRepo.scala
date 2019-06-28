package xiatian.chatbot.ability.law

import better.files.File
import slick.jdbc.MySQLProfile
import xiatian.chatbot.conf.{Logging, MyConf}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object LawRepo extends Logging {

  import slick.jdbc.MySQLProfile.api._

  val profile = MySQLProfile

  val db: Database = Database.forConfig("db.mysql.default", MyConf.config)

  def close(): Unit = {
    if (db != null)
      db.close()
  }

  /**
    * 获取法lawId下的number法条
    *
    * @param lawId
    * @param number
    * @return
    */
  def getLawArticle(lawId: Int, number: Int): Option[String] = {
    LOG.info(s"get law article: pid=$lawId, statute_number=$number")
    Await.result(db run {
      sql"""select statute_content from table_regulations where pid=$lawId AND statute_number = $number"""
        .as[String].headOption
    }, Duration.Inf)
  }

  //  def export(): Future[Seq[(Int, String, String)]] = db run {
  //    sql"""select id, 法规名称, 法规简称及别称, 发文字号, 时效性, 发布时间, 实施时间, 发布部门, 效率级别, 适用范围,
  //          法规类别, 法规标签 from regulation"""
  //      .as[(String, String)]
  //  }

  def exportIdNameMapping() = {
    val records = Await.result(db run {
      sql"""select id, 法规名称, 法规简称及别称, 法规标签 from regulation"""
        .as[(Int, String, String, String)]
    }, Duration.Inf)

    val f = File("./data/regulation/id_name.txt")
    f.parent.toJava.mkdirs()
    val writer = f.newPrintWriter(false)
    records map {
      case (id, name, jiancheng, tags) =>
        writer.println(s"$id, $name, $jiancheng")
    }
    writer.close()
  }

  def exportLawArticle() = {
    val records = Await.result(db run {
      sql"""select pid, statute_content, statute_number from table_regulations"""
        .as[(Int, String, Int)]
    }, Duration.Inf)

    val f = File("./data/regulation/law_article.txt")
    f.parent.toJava.mkdirs()
    val writer = f.newPrintWriter(false)
    records.foreach {
      case (pid, content, number) =>
        writer.println(s"${pid}-${number} ${
          content.replaceAll("\n", "")
            .replaceAll("\r", "")
        }")
    }
    writer.close()
  }

  def main(args: Array[String]): Unit = {
    exportLawArticle()
  }
}
