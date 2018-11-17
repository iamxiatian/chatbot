package ruc.chatbot.conf

object AimlConst {
  val AIML_FILE_SUFFIX = ".aiml"

  //private static String root_path = System.getProperty("user.home") + File.separator + ".aiml-bots" + File.separator + "bots";

  //@TODO:
  private var root_path = "/home/xiatian/workspace/github/chatbot/kb"

  val default_bot_name = "alice2"
  val error_bot_response = "Something is wrong with my brain."
  val default_bot_response = "I have no answer for that."
  val default_topic = "unknown"
  val default_that = "unknown"
  val null_input = "#NORESP"
  var debug = false

  def getRootPath: String = root_path

  def setRootPath(newRootPath: String): Unit = {
    root_path = newRootPath
  }
}
