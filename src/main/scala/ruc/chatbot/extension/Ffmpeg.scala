package ruc.chatbot.extension

import java.io.File

/**
  * 分割mp3:
  *
  * ffmpeg -i "1.mp3" -f segment -segment_time 30 -c copy part-%03d.mp3
  * ffmpeg -i part-001.mp3 -f s16le -ar 16000 part-1.pcm
  *
  * MP3转换为PCM：
  *
  * ffmpeg -i "part-001.mp3" -f s16le -ar 16000 -ac 1 -acodec pcm_s16le "part-1.pcm"
  */
object Ffmpeg {
  def split(mp3File: File, outDir: File): Unit = {
    import sys.process._

//    val command = s"ffmpeg -i \"${mp3File.getCanonicalPath}\" -f segment -segment_time 30 -c copy \"${outDir.getCanonicalPath}/part-%03d.mp3\""
//    val result = command !!
//    println(result)
  }

}
