package xiatian.chatbot.http.support

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import io.circe._
import io.circe.syntax._

trait JsonSupport extends RouteSupport {
  val CODE_SUCCESS: String = "OK"
  val CODE_ERROR: String = "ERROR"

  private def jsonData(data: Json, code: String): String = Map(
    "code" -> code.asJson,
    "data" -> data).asJson.pretty(Printer.spaces2)

  /**
    * 输出符合layui模板格式的JSON表格数据
    *
    * @return
    */
  def jsonTable(count: Long,
                data: Seq[Json],
                code: String = CODE_SUCCESS,
                msg: String = ""): String =
    Map[String, Json](
      "code" -> code.asJson,
      "msg" -> msg.asJson,
      "count" -> count.asJson,
      "data" -> data.asJson
    ).asJson.pretty(Printer.spaces2)

  /**
    * 输出带有status状态标记的JSON结果
    *
    * @return
    */
  def writeJsonOk(data: Json): StandardRoute = complete(
    HttpEntity(ContentTypes.`application/json`, jsonData(data, CODE_SUCCESS))
  )

  def writeJsonError(data: Json): StandardRoute = complete(
    HttpEntity(ContentTypes.`application/json`, jsonData(data, CODE_ERROR))
  )

  def writeJson(data: Json, code: String): StandardRoute = complete(
    HttpEntity(ContentTypes.`application/json`, jsonData(data, code))
  )

}