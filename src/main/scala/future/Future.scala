package future

import actors._


import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import scala.concurrent.duration._

import scala.concurrent.Await
import akka.pattern.{Patterns, ask}
import akka.util.Timeout

import domain._
import scala.concurrent.{ExecutionContext, Promise}




object Future {

  def main(args: Array[String]): Unit = {
    //http://alvinalexander.com/scala/akka-actor-how-to-send-message-wait-for-reply-ask
    // create the system and actor

    implicit val timeout = Timeout(19 seconds)

    val system = ActorSystem("AskTestSystem")
    val appsActor = system.actorOf(Props[AppointmentActor], name = "appsActor")

    val advisorActor = system.actorOf(Props(new AdvisorActor(appsActor)), name = "advisorActor")
    val roomsActor = system.actorOf(Props( new RoomsActor(appsActor)), name = "roomsActor")

    val coordinatorActor = system.actorOf(Props(new CoordinatorActor(advisorActor, roomsActor)), name = "Coordinator")
    println("Main started")

    println("Sending msg to get resources")

    val future1 = coordinatorActor ? MsgGetTimeSlots
    val result1 : List[Advisor] = Await.result(future1, timeout.duration).asInstanceOf[List[Advisor]]

    println("Resources received: ")
    result1.foreach(adv  => println(s" ${adv.corpKey}"))

    system.shutdown()
  }
}
