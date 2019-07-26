package xiatian.chatbot.http.controller

import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import io.circe.Json
import io.circe.parser.parse
import io.circe.syntax._
import ruc.chatbot.bot.TuringBot
import xiatian.chatbot.ability.faq.{Faq, FaqBot, FaqIndexer}
import xiatian.chatbot.ability.law.LawBot
import xiatian.chatbot.ability.zhishi.ZhishiBot
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.chat.Chat
import xiatian.chatbot.http.support.JsonSupport
import xiatian.chatbot.parse.QuestionInput

object FaqController extends JsonSupport {

  def route: Route =
    (path("api" / "faq" / "add.do") & post & cors(settings)) {
      entity(as[String]) {
        text =>
          LOG.info(s"input: $text")

          parse(text).getOrElse(Json.Null).asArray match {
            case Some(items) =>
              val faqs: Seq[Faq] = items.flatMap {
                item: Json =>
                  val p = item.asObject.get
                  val question: String = p("question").get.as[String].getOrElse("")
                  val answer: String = p("answer").get.as[String].getOrElse("")
                  if (question.nonEmpty && answer.nonEmpty) {
                    Option(Faq(question, answer))
                  } else {
                    LOG.error("JSON文件中的question或answer不存在")
                    None
                  }
              }

              FaqIndexer.index(faqs)
              FaqBot.refresh()
              writeJsonOk("DONE".asJson)
            case None =>
              writeJsonError("""无内容，请传入如下类似的JSON内容： [{"question":"什么是区块链？", "answer":"区块链是..."}]""".asJson)
          }
      }
    }
}