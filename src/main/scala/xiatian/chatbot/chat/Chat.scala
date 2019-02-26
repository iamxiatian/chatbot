package xiatian.chatbot.chat

import xiatian.chatbot.bot.Bot
import xiatian.chatbot.conf.{Logging, MagicValues}
import xiatian.chatbot.parse.QuestionInput

/**
  * Class encapsulating a chat session between a bot and a client
  */
case class Chat(customerId: String, bot: Bot) extends Logging {
  val thatHistory = new History[String]("that")

  val requestHistory = new History[String]("request")

  val responseHistory = new History[String]("response")

  val inputHistory = new History[String]("input")

  val predicates = new Predicates


  /**
    * return a compound response to a multiple-sentence request.
    * "Multiple" means one or more.
    *
    * @param request
    */
  def chat(request: String): String = {
    val contextThatHistory = new History[String]("contextThat")
    val sentences = QuestionInput.splitSentence(request)
    val response = sentences.flatMap {
      input =>
        respond(input, contextThatHistory)
    }.mkString("\n")

    requestHistory.add(request)
    responseHistory.add(response)

    response
  }


  /**
    * Return bot response given an input and a history of "that" for
    * the current conversational interaction
    */
  private def respond(input: String,
                      contextThatHistory: History[String]): Option[String] = {
    val that = thatHistory.last().getOrElse(MagicValues.default_that)
    val topic = predicates("topic").getOrElse(MagicValues.default_topic)

    respond(input, that, topic, contextThatHistory)
  }

  /**
    * Return bot response to a single sentence input given conversation context
    */
  private def respond(input: String, that: String, topic: String,
                      contextThatHistory: History[String]): Option[String] = {
    inputHistory.add(input)
    //val response = AIMLProcessor.respond(input, that, topic, this)
    //    bot.brain.`match`(input, that, topic)
    bot.respond(input, that, topic)
  }

}
