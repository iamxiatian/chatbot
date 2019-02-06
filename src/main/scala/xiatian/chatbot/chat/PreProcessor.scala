package xiatian.chatbot.chat

/**
  * 对bot的聊天输入进行预处理
  *
  * @param bot
  */
object PreProcessor {
  /**
    * @TODO 把输入的文字，进行句子切分。
    * @param input
    * @return
    */
  def sentenceSplit(input: String): List[String] = List(input)

}
