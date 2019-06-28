package xiatian.chatbot.store.rdb

import slick.jdbc.MySQLProfile
import xiatian.chatbot.conf.MyConf

/**
  * 采用Mysql保存的数据库
  *
  * @tparam T
  */
abstract class MysqlRepo[T] extends Repo[T] {

  import slick.jdbc.MySQLProfile.api._

  val profile = MySQLProfile

  val db: Database = Database.forConfig("db.mysql.default", MyConf.config)

  def close(): Unit = {
    if (db != null)
      db.close()
  }
}
