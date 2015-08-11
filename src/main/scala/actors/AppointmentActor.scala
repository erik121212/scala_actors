package actors

import domain._

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive

case object GetAppointments

class AppointmentActor extends Actor {

  def  receive = {
    case adv : Advisor =>
      println("3. AppointmentActor.Advisor Entry " + adv)
      Thread.sleep(adv.delay)
      println("3. AppointmentActor.Advisor step 1" + adv)
      Thread.sleep(adv.delay)
      println("3. AppointmentActor.Advisor step 2" + adv)
      Thread.sleep(adv.delay)
      println("3. AppointmentActor.Advisor Exit" + adv)
      sender ! List(new Appointment("09:00", adv), new Appointment("09:10", adv), new Appointment("09:20", adv))

    case room : Room =>
      println("3. AppointmentActor.Room Entry " + room)
      Thread.sleep(10)
      println("3. AppointmentActor.Room Exit" + room)
      sender ! List(new Appointment("09:00", room), new Appointment("09:10", room), new Appointment("09:20", room))

    case  e @ _ => println("3. Invalid message '$e' at AppointmentActor")
  }
}
