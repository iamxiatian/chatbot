package xiatian.chatbot.store.rdb

import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
  * 处理数据的Repository, 目前支持MysqlRepo和PostgresRepo两类.
  *
  * @tparam T
  */
trait Repo[T] {
  //导入执行的上下文
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val LOG = LoggerFactory.getLogger(this.getClass)

  def createSchema: Future[Try[Unit]]

  def dropSchema: Future[Try[Unit]]

  def count: Future[Int]

  def list(page: Int,
           limit: Int,
           query: String, //过滤条件
           sortField: String,
           order: String): Future[Seq[T]]

  def countByQuery(query: String): Future[Int]
}
