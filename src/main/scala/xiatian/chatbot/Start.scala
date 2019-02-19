package xiatian.chatbot

import better.files.File
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.chat.Chat

/**
  * 启动Bot的入口
  */
object Start extends App {
  val bot = new Bot("Robot", File("./kb/alice2"))

  val chat = Chat("xiatian", bot)

  val reply = chat.chat("hello   world")
  println("REPLY:>>> " + reply)

  println("REPLY: >>> " + chat.chat("你个大傻必备sdfdsfsX"))
}
