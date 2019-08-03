package xiatian.chatbot

import com.hankcs.hanlp.HanLP
import xiatian.chatbot.bot.Bot
import xiatian.chatbot.chat.Chat
import xiatian.chatbot.conf.{Logging, MyConf}
import xiatian.chatbot.http.HttpServer

import scala.io.StdIn

/**
  * 启动Bot的入口
  */
object Start extends App {

  val parser = new scopt.OptionParser[Config]("bin/chatbot") {
    head(s"${BuildInfo.name}", s"${BuildInfo.version}")

    import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

    val format: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

    opt[Unit]("version").action((_, c) =>
      c.copy(version = true)).text("Current version and build info.")

    opt[Unit]("http").action((_, c) =>
      c.copy(http = true)).text("Start http server.")

    opt[Unit]("console").action((_, c) =>
      c.copy(console = true)).text("Start console server.")

    help("help").text("prints this usage text")

    note("\n xiatian, xia@ruc.edu.cn.")
  }

  println("configure LOG...")
  MyConf.configLog()

  parser.parse(args, Config()) match {
    case Some(config) =>

      if (config.http) {
        HttpServer.start()
      } else if (config.console) {
        ConsoleBot.start()
      }

      if (config.version) {
        MyConf.outputConfig()

        println("Build information:")
        BuildInfo.toMap.toSeq.sortBy(_._1).foreach {
          case (k, v) =>
            println(s"\t${k}: \t $v")
        }
      }

      ConsoleBot.start()
      // HttpServer.start()
    case None =>
      println("Wrong parameters :(")
  }

  case class Config(http: Boolean = false,
                    console: Boolean = false,
                    version: Boolean = false)


  object ConsoleBot {
    val bot = Bot("xiatian")

    val chat = Chat("human", bot)

    def test(input: String): Unit = {
      println("---------------------")
      println(s"Input>>>$input")
      println(s"Reply==>${chat.chat(input)}")
    }

    def start() {
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
          Logging.println(s"语言分析结果：${HanLP.segment(line)}")
          val reply = chat.chat(line)
          println(s"Reply==>$reply\n")
          request()
        }
      }

      request()
    }
  }

}
