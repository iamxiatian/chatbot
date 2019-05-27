package xiatian.chatbot.http.support

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import ch.megard.akka.http.cors.scaladsl.model.HttpOriginMatcher
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import xiatian.chatbot.conf.Logging

/**
  * 增强路由支持, 简化语法
  */
trait RouteSupport extends Logging {
  val settings = CorsSettings.defaultSettings.withAllowedOrigins(HttpOriginMatcher.*)

  def writeJson(jsonText: String): StandardRoute = complete(
    HttpEntity(ContentTypes.`application/json`, jsonText)
  )

  def writeHtml(html: String): StandardRoute = complete(
    HttpEntity(ContentTypes.`text/html(UTF-8)`, html)
  )

  def writeText(text: String): StandardRoute = complete(
    HttpEntity(ContentTypes.`text/plain(UTF-8)`, text)
  )
}
