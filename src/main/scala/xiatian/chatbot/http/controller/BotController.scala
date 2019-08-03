package xiatian.chatbot.http.controller

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import io.circe.syntax._
import ruc.chatbot.bot.TuringBot
import xiatian.chatbot.ability.faq.FaqBot
import xiatian.chatbot.ability.law.LawBot
import xiatian.chatbot.ability.zhishi.ZhishiBot
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.chat.Chat
import xiatian.chatbot.http.support.JsonSupport
import xiatian.chatbot.parse.QuestionInput

object BotController extends JsonSupport {
  val bot = Bot("xiatian")
  val chat = Chat("human", bot)

  def writeResult(input: String, output: String, code: String) = writeJson(Map(
    "input" -> input.asJson,
    "reply" -> output.asJson,
    "keywords" -> QuestionInput.extractTopics(input).asJson
  ).asJson, code)

  def route: Route =
    (path("api" / "input") & post & cors(settings)) {
      parameters('domain.as[String] ? "") {
        domain =>
          entity(as[String]) {
            text =>
              process(text, domain)
          }
      }
    } ~ (path("api" / "input") & get & cors(settings)) {
      parameters('q.as[String], 'domain.as[String] ? "") {
        case (q, domain) =>
          process(q, domain)
      }
    }

  private def process(text: String, domain: String) = {
    LOG.info(s"input: $text")

    // 优先调用法规库
    val chatReply = LawBot.request(text) match {
      case Some(msg) => msg
      case None => chat.chat(text.trim)
    }

    if (chatReply.isEmpty) {
      //进一步测试FaqBot
      FaqBot.request(text.trim) match {
        case Some(faq) =>
          writeResult(text, faq, "FAQ")
        case None =>
          //访问zhishi.me
          val baike = ZhishiBot.request(text)
          if (baike.nonEmpty) {
            writeResult(text, baike.get, "ZHISHI")
          } else TuringBot.request(text) match { //FAQ之后继续测试TuringBot
            case Some(r) =>
              writeResult(text, r, "TURING")
            case None =>
              writeResult(text, "对不起，我没有理解您的意思。", CODE_ERROR)
          }
      }
    } else writeResult(text, chatReply, CODE_SUCCESS)
  }
}