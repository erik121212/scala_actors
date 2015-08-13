package actors


import actors.AppointmentsActor.{MsgAvailability, MsgDurationDetermined, MsgRoomAppointments}
import actors.AvailableRoomSlotsActor.MsgCollectSlots
import actors.CoordinatorActor.MsgRoomTimeSlots
import actors.CustomerSubjectActor.MsgCustomerSubject
import akka.actor.{Terminated, Props, ActorRef, Actor}
import domain.{Availability, Room}


object AvailableRoomSlotsActor {
  case class MsgCollectSlots(location: String, question: String, week :Int)
}
class AvailableRoomSlotsActor extends Actor {

  val rooms = List(new Room("a1@b.c", 20), new Room("a2@b.c", 25))

  var currentNumberOfRooms = 0
  var numberOfRooms = 0
  var numberOfTimeSlots = 0

  var originator: ActorRef = _

  var timeslots : List[Availability] = Nil
  var appointmentActors: List[ActorRef] = Nil

  def  receive = {
    case Terminated(a) =>
      println(s"2b. AvailableRoomSlotsActor.Terminated entry ($a)")

      println(s"2b. AvailableRoomSlotsActor.Terminated exit ($a)")

    case MsgCollectSlots(loc, q, week) =>
      println(s"2b. AvailableRoomSlotsActor.MsgCollectSlots entry ($loc, $q, $week)")
      originator = sender()

      Thread.sleep(1000) // IO ophalen Rooms

      numberOfRooms = rooms.length
      rooms.foreach(room => {
        var actor = context.actorOf(Props[AppointmentsActor])
        actor ! MsgRoomAppointments(room, week)
        appointmentActors = actor :: appointmentActors
      })

      println(s"2b. AvailableRoomSlotsActor.MsgCollectSlots exit ($loc, $q)")

    case MsgAvailability(availability) =>
      println("2b. AvailableRoomSlotsActor.MsgAppointments entry")

//      apps.foreach(app => println(s"2a. AvailableRoomSlotsActor.List[Appointment] $app"))

      numberOfTimeSlots += availability.timeSlots.length
      timeslots = availability :: timeslots 

      currentNumberOfRooms += 1
      println(s"2b. AvailableRoomSlotsActor.MsgAppointments $currentNumberOfRooms of $numberOfRooms Terminates received")

      if (currentNumberOfRooms == numberOfRooms) {
        println(s"2b. AvailableRoomSlotsActor.MsgAppointments Last Terminate received ($numberOfTimeSlots timeslots)")
        originator ! MsgRoomTimeSlots(timeslots)
        println(s"2a. AvailableAdvisorSlotsActor.MsgAppointments Send $timeslots ($numberOfTimeSlots) to $originator")
        context.system.stop(self)
      }

      println("2b. AvailableRoomSlotsActor.MsgAppointments exit")

    case MsgCustomerSubject(subject) =>
      println(s"2b. AvailableRoomSlotsActor.MsgCustomerSubject entry ($subject)")
      appointmentActors.foreach(actor => actor ! MsgDurationDetermined(subject) )
      println(s"2b. AvailableRoomSlotsActor.MsgCustomerSubject exit")

    case e @ _ => println(s"2b. AvailableRoomSlotsActor received invalid message '$e'")
  }
}
