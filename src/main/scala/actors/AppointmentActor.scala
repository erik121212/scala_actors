package actors

import domain._

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive

case object GetAppointments

class AppointmentActor extends Actor {

  def  receive = {
    case adv : Advisor =>
      println("   AppointmentActor.Advisor Entry" + adv)
      Thread.sleep(200)
      sender ! List(new Appointment("09:00", adv), new Appointment("09:10", adv), new Appointment("09:20", adv))
      println("   AppointmentActor.Advisor Exit" + adv)

    case room : Room =>
      println("   AppointmentActor.Advisor Entry" + room)
      Thread.sleep(10)
      sender ! List(new Appointment("09:00", room), new Appointment("09:10", room), new Appointment("09:20", room))
      println("   AppointmentActor.Advisor Exit" + room)

    case _ => Thread.sleep(2000); println("after 2 seconds sleep")
  }
}
