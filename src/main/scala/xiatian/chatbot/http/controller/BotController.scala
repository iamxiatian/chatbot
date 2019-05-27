package xiatian.chatbot.http.controller

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import io.circe.syntax._
import ruc.chatbot.bot.TuringBot
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.chat.Chat
import xiatian.chatbot.http.support.JsonSupport
import xiatian.chatbot.parse.QuestionInput

object BotController extends JsonSupport {
  val bot = Bot("xiatian")
  val chat = Chat("human", bot)

  def route: Route =
    (path("api" / "input") & post & cors(settings)) {
      entity(as[String]) {
        text =>
          LOG.info(s"input: $text")

          val chatReply = chat.chat(text.trim)
          val (reply: String, code: String) = if (chatReply.isEmpty) {
            TuringBot.request(text) match {
              case Some(r) => (r, "GENERAL")
              case None => ("对不起，我没有理解您的意思。", CODE_ERROR)
            }
            "TURING"
          } else (chatReply, CODE_SUCCESS)

          val keywords = QuestionInput.extractTopics(text)

          writeJson(Map(
            "input" -> text.asJson,
            "reply" -> reply.asJson,
            "keywords" -> keywords.asJson
          ).asJson, code)
      }
    }
}