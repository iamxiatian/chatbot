package ruc.chatbot.bot

import java.io.File

import com.batiaev.aiml.bot.{Bot, BotImpl}
import com.batiaev.aiml.chat.ChatContextStorage
import com.batiaev.aiml.consts.AimlConst

class BotRepository(val chatContextStorage: ChatContextStorage) {
  val rootDir = AimlConst.getRootPath

  def get: Bot = get(AimlConst.default_bot_name)

  def get(name: String) = new BotImpl(name, getBotPath(name), chatContextStorage)

  private def getBotPath(name: String) = rootDir + File.separator + name + File.separator
}
