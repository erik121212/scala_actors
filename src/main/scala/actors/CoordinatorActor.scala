package actors

import akka.actor.{ActorRef, Actor}


case object MsgGetResources
case object MsgGetAdvisors
case object MsgGetRooms

class CoordinatorActor(advisorActor: ActorRef, roomsActor: ActorRef)  extends Actor {
   private var roomsReceived = false
   private var advisorsReceived = false

  def  receive = {
    case MsgGetResources =>
      println("MsgGetResources received")
         if (advisorsReceived && roomsReceived ) {
           sender ! "done"
         }
    case MsgGetRooms =>
      roomsReceived = true
      println("MsgGetRooms received")
     // Thread.sleep(2000)
      roomsReceived = true
      println("MsgGetRooms returned")
    case MsgGetAdvisors =>
      println("MsgGetAdvisors received")
    //  Thread.sleep(3500)
      advisorsReceived = true
      println("MsgGetAdvisors returned")
    case _ => println("Invalid message")
  }
}
