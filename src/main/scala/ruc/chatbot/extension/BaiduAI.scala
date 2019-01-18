package ruc.chatbot.extension

import java.util

import com.baidu.aip.speech.AipSpeech

/**
  * 百度AI接口
  */
object BaiduAI {
  val APP_ID = "15337184"
  val API_KEY = "5OR80GxgcNVZt1SUNuLoIKqi"
  val SECRET_KEY = "BIW7z0jsAm6aqnOVbGSNH3ofP4B1qLTH"

  /**
    * 语音识别接口
    */
  def asr(): Unit = {

    val client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY)
    client.setConnectionTimeoutInMillis(2000)
    client.setSocketTimeoutInMillis(60000)
    val params = new util.HashMap[String, Object]()
    //params.put("dev_pid", new Integer(1536))
    val res = client.asr("/home/xiatian/Music/part-1.pcm", "pcm", 16000, params)
    //val res = client.asr("/home/xiatian/Music/16k.pcm", "pcm", 16000, params)
    System.out.println(res.toString(2))
  }

  def main(args: Array[String]): Unit = {
    asr()
  }
}
