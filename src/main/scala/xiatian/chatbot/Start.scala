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

  def test(input: String): Unit = {
    println("---------------------")
    println(s"Input>>>$input")
    println(s"Reply==>${chat.chat(input)}")
  }

  test("你个大傻必备sdfdsfsX")

  test("我最喜欢的水果是橘子")

  test("现在几点")

  test("昨天几号")

  test("明天几号")

  test("今天星期几")

  test("10天后是星期几")
}
