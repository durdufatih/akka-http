import actor.ExceptionActor
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import model.{ExceptionModel, Item}
import spray.json.DefaultJsonProtocol

import scala.io.StdIn

object HelloApplication extends App with Directives with SprayJsonSupport with DefaultJsonProtocol {

  implicit val system = ActorSystem("exception-system")

  val exceptionActor = system.actorOf(Props[ExceptionActor], name = "exceptionActor")
  implicit val materializer = ActorMaterializer()
  implicit val prettyPrintedItemFormat = jsonFormat2(Item)

  val ex: ExceptionModel = new ExceptionModel("This is test exception", "Deneme", "User Test", "Java Api", "Today", "info", "500")
  val route =
    path("hello") {
      get { ctx =>
        exceptionActor ! "test"
        Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = "http://172.31.23.45:8080/exceptions/insert", entity =
          akka.http.scaladsl.model.FormData
          (Map("message" -> "This is test exception", "innerException" -> "Deneme", "StackTrace" -> "User Test", "SourceApp" -> "Java Api", "SaveDateTime" -> "New Data", "LogLevel" -> "Info", "StatusCode" -> "500"))
            .toEntity(HttpCharsets.`UTF-8`)))
        ctx.complete("complated");
      }
    }


  val (host, port) = ("localhost", 8080)
  val bindingFuture = Http().bindAndHandle(route, host, port)


  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
}
