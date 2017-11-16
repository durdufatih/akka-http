package actor

import akka.actor.Actor
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._

class ExceptionActor extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case "test" => {
      log.info("Test")
    }
    case _ => log.info("unknown message")
  }

}
