package future

import actors.CoordinatorActor.MsgGetTimeSlots
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


    val coordinatorActor = system.actorOf(Props(new CoordinatorActor(60)), name = "Coordinator")
    println("0. Main started")

    println("0. Sending msg to get Time Slots")

    val sTime = System.nanoTime()
    val future1 = coordinatorActor ? MsgGetTimeSlots("aLocatie", "aSubject", 20)
    val result1 : List[Availability] = Await.result(future1, timeout.duration).asInstanceOf[List[Availability]]
    val eTime = System.nanoTime()

    println(s"0. Resources received in ${eTime-sTime}ns: ")
    result1.foreach(avail  => println(s" $avail"))

    println("0. Shutdown")
    system.shutdown()
  }
}
