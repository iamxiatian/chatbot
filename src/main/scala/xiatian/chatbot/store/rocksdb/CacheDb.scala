package xiatian.chatbot.store.rocksdb

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

import xiatian.chatbot.conf.MyConf

/**
  * 存放缓存信息的数据库
  */
object CacheDb extends BaseDb("cache", new File("./db/cache")) {
  def cache(key: String, value: String): Unit = {
    db.put(defaultHandler, key.getBytes(UTF_8), value.getBytes(UTF_8))
  }

  def delete(key: String) = deleteKey(key)

  def getString(key: String): Option[String] = {
    val value = db.get(defaultHandler, key.getBytes(UTF_8))
    if (value == null) None else Some(new String(value, UTF_8))
  }

}
