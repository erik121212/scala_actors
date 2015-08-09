package actors

import akka.actor.Actor


import scala.concurrent.{ExecutionContext, Promise}

case object GetAdvisors

class AdvisorActor extends Actor {
  val advisors = List("a","b","c")

  def  receive = {
    case GetAdvisors => Thread.sleep(0)
                        sender ! advisors
    case _ => println("Invalid message")
  }
}

