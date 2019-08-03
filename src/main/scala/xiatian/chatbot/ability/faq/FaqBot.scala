package xiatian.chatbot.ability.faq

import xiatian.chatbot.conf.{Logging, MyConf}

object FaqBot extends Logging {
  var searcher: FaqSearcher = new FaqSearcher()

  val enabled = MyConf.getBoolean("bot.faq.enabled")

  def refresh(): Unit = {
    searcher.close()
    searcher = new FaqSearcher()
  }

  /**
    * 学习一条FAQ，目前是把FAQ加入到索引库中完成
    *
    * @param faq
    */
  def learn(faq: Faq): Unit = {

  }

  def request(input: String, domain: String): Option[String] = {
    searcher.search(input, Option(domain), 3).flatMap {
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
    println(FaqBot.request("保密工作遵循什么原则？", "all"))

    println(FaqBot.request("安全生产监督管理局的职能是什么", "all"))
  }
}
