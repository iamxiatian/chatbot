package ruc.chatbot

import com.batiaev.aiml.bot.BotImpl
import com.batiaev.aiml.channels.ConsoleChannel
import com.batiaev.aiml.chat.{Chat, InMemoryChatContextStorage}
import com.batiaev.aiml.consts.AimlConst
import ruc.chatbot.bot.BotRepository

/**
  * 启动Bot的入口
  */
object Start {
  private val botRepository = new BotRepository(new InMemoryChatContextStorage)

  def main(args: Array[String]): Unit = {
    val botName =
      if (args.length > 0) args(0)
      else
        AimlConst.default_bot_name

    init(botName)
  }

  def init(botName: String): Unit = {
    val bot = botRepository.get(botName).asInstanceOf[BotImpl]
    val consoleChannel = new ConsoleChannel(bot)
    consoleChannel.startChat("Tony")
    if (!bot.wakeUp) return
    val chat = new Chat(bot, consoleChannel)
    chat.start()
  }
}
