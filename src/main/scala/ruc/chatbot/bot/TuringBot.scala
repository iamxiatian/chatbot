package ruc.chatbot.bot

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import xiatian.chatbot.conf.{Logging, MyConf}

import scala.util.{Failure, Success, Try}

/**
  * 图灵机器人接口
  */
object TuringBot extends Logging {
  val apiKey = MyConf.getString("bot.turing.apikey")
  val userId = MyConf.getString("bot.turing.userid")

  val enabled = MyConf.getBoolean("bot.turing.enabled")

  val servicePoint = "http://openapi.tuling123.com/openapi/api/v2"

  def request(input: String): Option[String] = if (!enabled) None else Try {
    val data =
      s"""
         |{
         |	"reqType":0,
         |    "perception": {
         |        "inputText": {
         |            "text": "$input"
         |        },
         |        "inputImage": {
         |            "url": "imageUrl"
         |        },
         |        "selfInfo": {
         |            "location": {
         |            }
         |        }
         |    },
         |    "userInfo": {
         |        "apiKey": "$apiKey",
         |        "userId": "$userId"
         |    }
         |}
      """.stripMargin

    val response = scalaj.http.Http(servicePoint).postData(data).asString
    val result: JsonObject = parse(response.body).getOrElse(Json.Null)
      .asObject.get("results")
      .asJson
      .asArray.get(0).asObject.get

    LOG.info(s"$input => ${response.body}")
    if (result("resultType").get.asString.get == "text") {
      result("values").asJson.asObject.get("text").get.asString
    } else {
      None
    }
  } match {
    case Success(reply) => reply
    case Failure(e) =>
      LOG.error(s"Turingbot error for $input", e)
      None
  }

  def main(args: Array[String]): Unit = {
    println(request("今天是农历初几？？？"))
  }
}
