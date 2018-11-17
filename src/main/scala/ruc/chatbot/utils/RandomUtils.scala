package ruc.chatbot.utils

import scala.util.Random

object RandomUtils {
  def pick[T](values: Seq[T]) = {
    val idx = Random.nextInt(values.length)
    values(idx)
  }
}
