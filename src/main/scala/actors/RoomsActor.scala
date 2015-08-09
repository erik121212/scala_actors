package actors

import akka.actor.Actor


import scala.concurrent.{ExecutionContext, Promise}

case object GetRooms

class RoomsActor extends Actor {
  val rooms = List("r1","r2","r3", "r4")

  def  receive = {
    case GetRooms => Thread.sleep(0)
                        sender ! rooms
    case _ => println("Invalid message")
  }
}

