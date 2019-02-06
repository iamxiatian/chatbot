package xiatian.chatbot.chat

import com.batiaev.aiml.bot.Bot
import xiatian.chatbot.aiml.AIMLProcessor
import xiatian.chatbot.conf.{Logging, MagicValues}

/**
  * Class encapsulating a chat session between a bot and a client
  */
class Chat(customerId: String, bot: Bot) extends Logging {
  val thatHistory = new History[String]("that")

  val requestHistory = new History[String]("request")

  val responseHistory = new History[String]("response")

  val inputHistory = new History[String]("input")

  val predicates = new Predicates


  /**
    * return a compound response to a multiple-sentence request.
    * "Multiple" means one or more.
    *
    * @param input
    */
  def chat(input: String): String = {
    val contextThatHistory = new History[String]("contextThat")
    val sentences = PreProcessor.sentenceSplit(input)
    sentences.foreach {
      sentence =>
        //respond(sentences, contextThatHistory)
    }


    ""
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
    val response = AIMLProcessor.respond(input, that, topic, this)
    None
  }

}
