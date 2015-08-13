package actors

import actors.AppointmentsActor._
import domain._

import akka.actor.{ActorRef, Actor}

object AppointmentsActor {
  //Input Messages
  case class MsgAdvisorAppointments(advisor: Advisor, week : Int)
  case class MsgRoomAppointments(room: Room, week : Int)
  case class MsgDurationDetermined(subject : CustomerSubject)
  //Output Messages
  case class MsgAvailability(availability: Availability)
}

class AppointmentsActor extends Actor {

  var appointments : List[Appointment] = Nil
  var duration : Int = 0
//  var advisor : Advisor = _
//  var room : Room = _
  var resource : Resource = _

  private def CalculateTimeSlots = {

    if (appointments != Nil && duration != 0) {
//      sender ! MsgAppointments( timeslots )
      Thread.sleep(5) // calculate Free Timeslots

      val availability = resource match {
        case adv: Advisor =>
          new AvailabilityAdvisor(
            adv.asInstanceOf[Advisor],
            List(
              new TimeSlot("08:00", "09:00"),
              new TimeSlot("10:00", "11:00"),
              new TimeSlot("10:15", "11:15"),
              new TimeSlot("10:30", "11:30"),
              new TimeSlot("10:45", "11:45"),
              new TimeSlot("11:00", "12:00"),
              new TimeSlot("13:00", "13:00"),
              new TimeSlot("13:15", "13:15"),
              new TimeSlot("13:30", "13:30"),
              new TimeSlot("13:45", "13:45"),
              new TimeSlot("14:30", "15:30"),
              new TimeSlot("14:45", "15:45"),
              new TimeSlot("15:00", "16:00"),
              new TimeSlot("15:15", "16:15"),
              new TimeSlot("15:30", "16:30"),
              new TimeSlot("15:45", "16:45"),
              new TimeSlot("16:00", "17:00")
            )
          )
        case rm : Room =>
          new AvailabilityRoom(
            rm.asInstanceOf[Room],
            List(
              new TimeSlot("08:30", "09:30"),
              new TimeSlot("08:45", "09:45"),
              new TimeSlot("09:00", "10:00"),
              new TimeSlot("09:15", "10:15"),
              new TimeSlot("09:30", "10:30"),
              new TimeSlot("09:45", "10:45"),
              new TimeSlot("10:00", "11:00"),
              new TimeSlot("10:15", "11:15"),
              new TimeSlot("10:30", "11:30"),
              new TimeSlot("10:45", "11:45"),
              new TimeSlot("11:00", "12:00"),
              new TimeSlot("13:00", "13:00"),
              new TimeSlot("13:15", "13:15"),
              new TimeSlot("13:30", "13:30"),
              new TimeSlot("13:45", "13:45"),
              new TimeSlot("14:30", "15:30"),
              new TimeSlot("14:45", "15:45"),
              new TimeSlot("15:00", "16:00"),
              new TimeSlot("15:15", "16:15"),
              new TimeSlot("15:30", "16:30"),
              new TimeSlot("15:45", "16:45"),
              new TimeSlot("16:00", "17:00")
            )
          )
//        case None => Nil
        }

      sender ! MsgAvailability(availability.asInstanceOf[Availability])
      context.system.stop(self)
    }
  }

  def  receive = {
    case MsgAdvisorAppointments(adv, week) =>
      resource = adv
      println(s"3. AppointmentsActor.Advisor Entry ($adv)")
      Thread.sleep(adv.delay)
      println(s"3. AppointmentsActor.Advisor step 1 ($adv)")
      Thread.sleep(adv.delay)
      println(s"3. AppointmentsActor.Advisor step 2 ($adv)")
      Thread.sleep(adv.delay)
      appointments = List(new Appointment("aSubject 1","09:00", "10:00", adv), new Appointment("aLunch", "12:00", "13:00" , adv), new Appointment("aSubject 2", "14:00", "14:30", adv))
      CalculateTimeSlots
      println(s"3. AppointmentsActor.Advisor Exit ($adv)")

    case MsgRoomAppointments(room, week) =>
      resource = room
      println(s"3. AppointmentsActor.Room Entry ($room)")
      Thread.sleep(10)
      appointments = List(new Appointment("aClosed 1", "07:00", "08:30", room), new Appointment("aClosed 2", "12:00", "13:30" , room))
      CalculateTimeSlots
      println(s"3. AppointmentsActor.Room Exit ($room)")

    case MsgDurationDetermined(subject) =>
      println(s"3. AppointmentsActor.MsgDurationDetermined entry ($subject)")
      duration = subject.duration
      CalculateTimeSlots
      println(s"3. AppointmentsActor.MsgDurationDetermined exit")

    case e @ _ => println(s"3. AppointmentsActor received invalid message '$e'")
  }
}
