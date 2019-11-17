package xiatian.chatbot.learn

import better.files.File
import xiatian.chatbot.ability.faq.{Faq, FaqIndexer}
import xiatian.chatbot.store.rdb.MedicineIndexer

/**
  * 机器人学习数据，增强能力
  */
object BotLearning {
  /**
    * 学习医药知识
    */
  def learnMedicine(): Unit = {
    MedicineIndexer.learning()
  }

  /**
    * 学习法规知识
    */
  def learnLaw() = {
    val faqs = Faq.load("./data/faq/fagui.txt")
    val indexer = new FaqIndexer(File("./data/faq/index").path)
    faqs.foreach(faq => indexer.index(faq))
    indexer.close()
    println("DONE.")
  }

  def learnAll() = {
    println("Learn law ...")
    learnLaw()

    println("Learn medicine ...")
    learnMedicine()

    println("ALL DONE!")
  }

  def main(args: Array[String]): Unit = {
    learnAll()
  }
}
