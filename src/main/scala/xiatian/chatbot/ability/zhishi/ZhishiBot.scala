package xiatian.chatbot.ability.zhishi

import java.net.URLEncoder

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import scalaj.http.{Http, HttpResponse}
import xiatian.chatbot.conf.Logging
import xiatian.chatbot.bot.parse.{InputSimplify, NLP}
import xiatian.chatbot.store.rocksdb.CacheDb

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * 集成zhishi.me的图谱数据，利用该数据进行问答的机器人
  */
object ZhishiBot extends Logging {
  def fetch(entity: String): Option[JSONObject] = Try {
    val url = s"http://zhishi.me/api/entity/${URLEncoder.encode(entity, "utf-8")}"
    val md5 = Hashing.md5().newHasher()
      .putString(url, Charsets.UTF_8)
      .hash().toString

    CacheDb.getString(md5) match {
      case Some(jsonString) =>
        if (jsonString.nonEmpty) {
          val json = JSON.parseObject(jsonString)
          Option(json)
        } else None
      case None =>
        val response: HttpResponse[String] = Http(url)
          .timeout(connTimeoutMs = 2000, readTimeoutMs = 5000)
          .asString

        if (response.code == 200) {
          val body = response.body
          CacheDb.cache(md5, body)
          val json = JSON.parseObject(body)
          Option(json)
        } else if (response.code == 404) {
          //不存在
          CacheDb.cache(md5, "")
          None
        } else {
          //ERROR
          None
        }
    }
  } match {
    case Success(r) => r
    case Failure(e) =>
      LOG.error(s"fetch $entity error", e)
      None
  }

  /**
    * 获取知识.me的摘要
    *
    * @return
    */
  private def getAbstract(json: JSONObject): Option[String] = {
    val baike = getItemString(json, "baidubaike", "abstracts")

    if (baike.isEmpty) {
      val zhwiki = getItemString(json, "zhwiki", "abstracts")
      if (zhwiki.isEmpty) {
        getItemString(json, "hudongbaike", "abstracts")
      } else zhwiki
    } else baike
  }

  private def getItemString(jsonObject: JSONObject,
                            key: String,
                            attrName: String): Option[String] = {
    if (jsonObject.containsKey(key)) {
      val obj = jsonObject.getJSONObject(key)
      if (obj.containsKey(attrName)) {
        Some(obj.getString(attrName))
      } else None
    } else None
  }

  def getInfoBox(json: JSONObject, source: String): Map[String, String] = {
    val pairs = mutable.Map.empty[String, String]
    if (json.containsKey(source)) {
      val sourceJson = json.getJSONObject(source)
      if (sourceJson.containsKey("infobox")) {
        val infoboxJson = sourceJson.getJSONObject("infobox")
        infoboxJson.entrySet().forEach {
          entry =>
            val key = entry.getKey
            val values = entry.getValue.asInstanceOf[JSONArray]

            if (values.size() > 0) {
              pairs(key) += values.asScala.mkString(", ")
            }
        }
      }
    }

    pairs.toMap
  }

  def request(input: String): Option[String] = {
    val s = InputSimplify.simplify(input)
    val s2 = NLP.segment(s).filter(_.nature.toString != "w").map(_.word)
      .mkString("")
    if (s2.length > 15 || s2.length < 4) {
      None
    } else if (s2.startsWith("什么是")) {
      fetch(s2.substring(3)) flatMap (getAbstract(_))
    } else if (s2.endsWith("是什么")) {
      fetch(s2.substring(0, s2.length - 3)) flatMap (getAbstract(_))
    } else if (s2.startsWith("谁是")) {
      fetch(s2.substring(2)) flatMap (getAbstract(_))
    } else if (s2.endsWith("是谁")) {
      fetch(s2.substring(0, s2.length - 2)) flatMap (getAbstract(_))
    } else None
  }

  def main(args: Array[String]): Unit = {
    request("谁是习近平") foreach println
  }

}
