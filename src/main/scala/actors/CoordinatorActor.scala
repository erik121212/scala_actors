package actors

import actors.CoordinatorActor.{RoomSlots, AdvisorSlots, MsgGetTimeSlots}
import akka.actor.{ActorRef, Terminated, Props, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import domain._

object CoordinatorActor{
  case object MsgGetTimeSlots
  case class AdvisorSlots(slots : List[Appointment])
  case class RoomSlots(slots : List[Appointment])
}

class CoordinatorActor(duration : Int)  extends Actor {
  import context.dispatcher

  var advAppointments : List[Appointment] = Nil
  var roomAppointments : List[Appointment] = Nil
  var allAppointments : List[Appointment] = Nil
  var originator: ActorRef = _

  def  receive = {
    case Terminated(a) =>
      println(s"1. CoordinatorActor.Terminated entry ($a)")

      println(s"1. CoordinatorActor.Terminated exit ($a)")

    case MsgGetTimeSlots =>
      implicit val timeout = Timeout(3 seconds)
      originator = sender()

      println(s"1. CoordinatorActor.MsgGetTimeSlots entry ($self)")

      val availAdvisorsActor = context.actorOf(Props(new AvailableAdvisorSlotsActor(duration)))
      val availRoomsActor = context.actorOf(Props(new AvailableRoomSlotsActor(duration)))

      availAdvisorsActor ! AvailableAdvisorSlotsActor.CollectSlots("Amsterdam", "ik wil iets", 20)
      availRoomsActor ! AvailableRoomSlotsActor.CollectSlots("Amsterdam", "ik wil iets", 20)

      println(s"1. CoordinatorActor.MsgGetTimeSlots exit")

    case AdvisorSlots(advApps) =>
      println(s"1. CoordinatorActor.AdvisorSlots entry")

      advAppointments = advApps

      if (advAppointments != Nil && roomAppointments != Nil) {
        println(s"1. CoordinatorActor.AdvisorSlots all Appointments ${advAppointments ++ roomAppointments} received")
        originator ! advAppointments ++ roomAppointments
      }

      println(s"1. CoordinatorActor.AdvisorSlots exit")

    case RoomSlots(roomApps) =>
      println(s"1. CoordinatorActor.RoomSlots entry - $roomApps")

      roomAppointments = roomApps

      if (advAppointments != Nil && roomAppointments != Nil) {
        println(s"1. CoordinatorActor.RoomSlots all Appointments ${advAppointments ++ roomAppointments} received")
        originator ! advAppointments ++ roomAppointments
      }
      println(s"1. CoordinatorActor.RoomSlots exit")

    case e @ _ => println(s"1. Invalid message '$e' CoordinatorActor")
  }
}
