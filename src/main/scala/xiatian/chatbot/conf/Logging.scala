package xiatian.chatbot.conf

import org.slf4j.LoggerFactory

/**
  * 统一的日志接口
  */
trait Logging {
  val LOG = LoggerFactory.getLogger(this.getClass)

}
