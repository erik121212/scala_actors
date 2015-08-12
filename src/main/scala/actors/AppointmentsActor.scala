package actors

import actors.AppointmentsActor.{RoomAppointments, AdvisorAppointments}
import domain._

import akka.actor.Actor

object AppointmentsActor {
  case class AdvisorAppointments(advisor: Advisor, week : Int)
  case class RoomAppointments(room: Room, week : Int)
}

class AppointmentsActor(duration:Int) extends Actor {

  def  receive = {
    case AdvisorAppointments(adv, week) =>
      println(s"3. AppointmentsActor.Advisor Entry ($adv)")
      Thread.sleep(adv.delay)
      println(s"3. AppointmentsActor.Advisor step 1 ($adv)")
      Thread.sleep(adv.delay)
      println(s"3. AppointmentsActor.Advisor step 2 ($adv)")
      Thread.sleep(adv.delay)
      println(s"3. AppointmentsActor.Advisor Exit ($adv)")
      sender ! List(new Appointment("09:00", adv), new Appointment("09:10", adv), new Appointment("09:20", adv))
      context.system.stop(self)

    case RoomAppointments(room, week) =>
      println(s"3. AppointmentsActor.Room Entry ($room)")
      Thread.sleep(10)
      println(s"3. AppointmentsActor.Room Exit ($room)")
      sender ! List(new Appointment("09:00", room), new Appointment("09:10", room), new Appointment("09:20", room))
      context.system.stop(self)

    case e @ _ => println(s"3. AppointmentsActor received invalid message '$e'")
  }
}
