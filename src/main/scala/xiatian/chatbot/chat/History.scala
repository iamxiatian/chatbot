package xiatian.chatbot.chat

import xiatian.chatbot.conf.Logging

import scala.collection.mutable

/**
  * Chat History
  */
class History[T](name: String) {

  val items = mutable.Queue.empty[T]

  def add(item: T) = {
    if (items.length > History.MAX_ITEMS) items.dequeue()

    items += item
  }

  /**
    * 获取历史最近的第idx个历史，idx为0时，为最近插入的历史记录
    *
    * @param idx
    * @return
    */
  def get(idx: Int): Option[T] = items.reverse.get(idx)

  /**
    * 获取最近插入的历史对象
    * @return
    */
  def last(): Option[T] = items.lastOption
}

object History extends Logging {
  val MAX_ITEMS = 2 //最大保存的历史数量

  def main(args: Array[String]): Unit = {
    LOG.debug("Test history...")
    val history = new History[String]("test")
    history.add("xiatian")
    history.add("summer")
    history.add("friend")
    LOG.info(history.get(0).get)
  }
}
