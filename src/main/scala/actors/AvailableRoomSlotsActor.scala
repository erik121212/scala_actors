package actors


import actors.AppointmentsActor.RoomAppointments
import actors.AvailableRoomSlotsActor.CollectSlots
import actors.CoordinatorActor.RoomSlots
import akka.actor.{Terminated, Props, ActorRef, Actor}
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}
import domain.{Appointment, Room}


object AvailableRoomSlotsActor {
  case class CollectSlots(location: String, question: String, week :Int)
}
class AvailableRoomSlotsActor(duration: Int) extends Actor {

  val rooms = List(new Room("a1@b.c", 20), new Room("a2@b.c", 25))
  var currentNumberOfRooms = 0
  var numberOfRooms = 0
  var numberOfAppointments = 0

  var appointments : List[Appointment] = Nil
  var originator: ActorRef = _

  def  receive = {
    case Terminated(a) =>
      println(s"2b. AvailableRoomSlotsActor.Terminated entry ($a)")

      println(s"2b. AvailableRoomSlotsActor.Terminated exit ($a)")

    case CollectSlots(loc, q, week) =>
      println(s"2b. AvailableRoomSlotsActor.CollectSlots entry ($loc, $q, $week)")
      originator = sender()

      Thread.sleep(1000) // IO ophalen Rooms

      numberOfRooms = rooms.length
      rooms.foreach(room => context.actorOf(Props(new AppointmentsActor(duration))) ! RoomAppointments(room, week))

      println(s"2b. AvailableRoomSlotsActor.CollectSlots exit ($loc, $q)")

    case apps: List[Appointment] =>
      println("2b. AvailableRoomSlotsActor.List[Appointment] entry")

//      apps.foreach(app => println(s"2a. AvailableRoomSlotsActor.List[Appointment] $app"))

      numberOfAppointments += apps.length
      appointments = appointments ++ apps

      currentNumberOfRooms += 1
      println(s"2b. AvailableRoomSlotsActor.List[Appointment] $currentNumberOfRooms of $numberOfRooms Terminates received")

      if (currentNumberOfRooms == numberOfRooms) {
        println(s"2b. AvailableRoomSlotsActor.List[Appointment] Last Terminate received ($numberOfAppointments appointments)")
        originator ! RoomSlots(appointments)
        println(s"2a. AvailableAdvisorSlotsActor.List[Appointment] Send $appointments ($numberOfAppointments) to $originator")
        context.system.stop(self)
      }

      println("2b. AvailableRoomSlotsActor.List[Appointment] exit")

    case e @ _ => println(s"2b. AvailableRoomSlotsActor received invalid message '$e'")
  }
}
