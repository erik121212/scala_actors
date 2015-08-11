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

    implicit val timeout = Timeout(3 seconds)

    val system = ActorSystem("AskTestSystem")
    val appsActor = system.actorOf(Props[AppointmentActor], name = "appsActor")

    val advisorActor = system.actorOf(Props(new AdvisorActor(appsActor)), name = "advisorActor")
    val roomsActor = system.actorOf(Props( new RoomsActor(appsActor)), name = "roomsActor")

    val coordinatorActor = system.actorOf(Props(new CoordinatorActor(advisorActor, roomsActor)), name = "Coordinator")
    println("0. Main started")

    println("0. Sending msg to get Time Slots")

    val future1 = coordinatorActor ? MsgGetTimeSlots
    val result1 : List[Appointment] = Await.result(future1, timeout.duration).asInstanceOf[List[Appointment]]

    println("0. Resources received: ")
    result1.foreach(app  => println(s" ${app.startTime} ${app.room} ${app.advisor}"))

    println("0. Shutdown")
    system.shutdown()
  }
}
