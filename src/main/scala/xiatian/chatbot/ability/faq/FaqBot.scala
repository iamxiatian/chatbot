package xiatian.chatbot.ability.faq

import xiatian.chatbot.conf.{Logging, MyConf}

object FaqBot extends Logging {
  val searcher: FaqSearcher = new FaqSearcher()

  val enabled = MyConf.getBoolean("bot.faq.enabled")

  /**
    * 学习一条FAQ，目前是把FAQ加入到索引库中完成
    *
    * @param faq
    */
  def learn(faq: Faq): Unit = {

  }

  def request(input: String): Option[String] = {
    searcher.search(input, 3).flatMap {
      faq =>
        val sim = similarity(input, faq.question)
        println(s"$input -- ${faq.question}, $sim")
        if (sim > 0.4) {
          LOG.info(s"$input ==> ${faq.question}: ${faq.answer}")
          Some(faq.answer)
        } else None
    }.headOption
  }

  private def similarity(input: String, candidate: String): Float = {
    val ch1 = input.toCharArray.toSet
    val ch2 = candidate.toCharArray.toSet
    ch1.intersect(ch2).size * 1.0f / Math.max(ch1.size, ch2.size)
  }

  def main(args: Array[String]): Unit = {
    println(FaqBot.request("保密工作遵循什么原则？"))

    println(FaqBot.request("安全生产监督管理局的职能是什么"))
  }
}
