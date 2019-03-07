package xiatian.chatbot

import better.files.File
import com.hankcs.hanlp.HanLP
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.chat.Chat

import scala.io.StdIn

/**
  * 启动Bot的入口
  */
object Start extends App {
  val bot = Bot("xiatian")

  val chat = Chat("human", bot)

  def test(input: String): Unit = {
    println("---------------------")
    println(s"Input>>>$input")
    println(s"Reply==>${chat.chat(input)}")
  }

//  test("你个大傻必备sdfdsfsX")
//
  test("我最喜欢的水果是橘子")
//
//  test("现在几点")
//
//  test("昨天几号")
//
//  test("明天几号")
//
//  test("今天星期几")
//
//  test("10天后是星期几")

  test("心律不齐有什么症状？")

  def request(): Unit = {
    print("INPUT> ")
    val line = StdIn.readLine().trim
    if (line != "exit") {
      val reply = chat.chat(line)
      println(reply)
      println(HanLP.segment(line))
      request()
    }
  }

  request()
}
