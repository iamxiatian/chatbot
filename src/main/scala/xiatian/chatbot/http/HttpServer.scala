package xiatian.chatbot.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import xiatian.chatbot.conf.MyConf
import xiatian.chatbot.http.controller.BotController
import xiatian.chatbot.http.support.JsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object HttpServer extends JsonSupport {
  implicit val timeout = Timeout(30.seconds)

  def start(implicit system: ActorSystem = ActorSystem("HttpBot")): Future[ServerBinding] = {
    implicit val materializer = ActorMaterializer()

    // needed for the future flatMap/onComplete in the end

    val exceptionHandler = ExceptionHandler {
      case e: Exception =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(InternalServerError, entity = e.toString))
        }
    }

    val route: Route = handleExceptions(exceptionHandler) {
      (path("") & get) {
        //complete(s"Spider ${Settings.version}")
        redirect("index.html", StatusCodes.MovedPermanently)
      } ~
        BotController.route ~
        get {
          getFromDirectory("web") //所有其他请求，都直接访问web目录中的对应内容
        }
    }

    println(s"Server online at http://localhost:${MyConf.httpPort}/")

    //启动服务，并在服务关闭时，解除端口绑定
    val server = Http(system).bindAndHandle(route, "0.0.0.0", MyConf.httpPort)

    sys.addShutdownHook {
      LOG.warn("Shutting down HTTP API server...")
      server.flatMap(_.unbind())
      println("DONE.")
    }

    server
  }

}
