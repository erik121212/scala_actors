package future

import actors._
import akka.actor
import akka.actor.ActorSystem
import akka.actor.Props


import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

import scala.concurrent.Await
import akka.pattern.{Patterns, ask}
import akka.util.Timeout
import scala.concurrent.Future


import scala.concurrent.{ExecutionContext, Promise}




object Future {

  def main(args: Array[String]): Unit = {
    //http://alvinalexander.com/scala/akka-actor-how-to-send-message-wait-for-reply-ask
    // create the system and actor

    implicit val timeout = Timeout(3 seconds)

    val system = ActorSystem("AskTestSystem")
    val advisorActor = system.actorOf(Props[AdvisorActor], name = "advisorActor")
    val roomsActor = system.actorOf(Props[RoomsActor], name = "roomsActor")

    val coordinatorActor = system.actorOf(Props(new CoordinatorActor(advisorActor, roomsActor)), name = "Coordinator")
    println("Main started")

    println("Sending msg to get advisors")
    coordinatorActor ! MsgGetAdvisors

    println("Sending msg to get rooms")
    coordinatorActor ! MsgGetRooms

    println("Sending msg to get resources")
    val future1 = coordinatorActor ? MsgGetResources
    val result1 = Await.result(future1, timeout.duration).asInstanceOf[String]
    println("Resources received: " + result1)



 /*   implicit val timeout = Timeout(3 seconds)
    val future1 = advisorActor ? GetAdvisors
    val result1 = Await.result(future1, timeout.duration).asInstanceOf[List[String]]
    println("List of advisors: " + result1)


    val future2: Future[List[String]] = ask(roomsActor, GetRooms).mapTo[List[String]]
    val result2 = Await.result(future2, 3 second)
    println("List of rooms" + result2)*/

    system.shutdown()
  }
}
