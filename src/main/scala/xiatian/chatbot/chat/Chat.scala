package xiatian.chatbot.chat

import com.batiaev.aiml.bot.Bot
import xiatian.chatbot.conf.Logging

/**
  * Class encapsulating a chat session between a bot and a client
  */
class Chat(customerId: String, bot: Bot) extends Logging {
  val thatHistory = new History[String]("that")

  val requestHistory = new History[String]("request")

  val responseHistory = new History[String]("response")

  val inputHistory = new History[String]("input")

  val predicates = new Predicates


}
