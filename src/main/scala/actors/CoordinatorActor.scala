package actors

import actors.CoordinatorActor.{MsgCustomerSubject, MsgRoomTimeSlots, MsgAdvisorTimeSlots, MsgGetTimeSlots}
import akka.actor.{ActorRef, Terminated, Props, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import domain._

object CoordinatorActor{
  case class MsgGetTimeSlots(location : String, subject : String, week : Int)
  case class MsgAdvisorTimeSlots(slots : List[Availability])
  case class MsgRoomTimeSlots(slots : List[Availability])
  case class MsgCustomerSubject(subject: CustomerSubject)
}

class CoordinatorActor(duration : Int)  extends Actor {
  import context.dispatcher
  val availAdvisorsActor = context.actorOf(Props[AvailableAdvisorSlotsActor])
  val availRoomsActor = context.actorOf(Props[AvailableRoomSlotsActor])
  val customerSubjectActor = context.actorOf(Props[CustomerSubjectActor])

  var advAppointments : List[Availability] = Nil
  var roomAppointments : List[Availability] = Nil
  var originator: ActorRef = _

  private def DeliverTimeSlots = {
    if (advAppointments != Nil && roomAppointments != Nil) {
      println(s"1. CoordinatorActor.DeliverTimeSlots all Appointments ${advAppointments ++ roomAppointments} received")
      originator ! advAppointments ++ roomAppointments
      context.system.stop(self)
    }
  }

  def  receive = {
    case Terminated(a) =>
      println(s"1. CoordinatorActor.Terminated entry ($a)")

      println(s"1. CoordinatorActor.Terminated exit ($a)")

    case MsgGetTimeSlots(location, subject , week) =>
      implicit val timeout = Timeout(3 seconds)
      originator = sender()

      println(s"1. CoordinatorActor.MsgGetTimeSlots entry ($location, $subject, $week)")

      availAdvisorsActor ! AvailableAdvisorSlotsActor.MsgCollectSlots(location, subject , week)
      availRoomsActor ! AvailableRoomSlotsActor.MsgCollectSlots(location, subject , week)
      customerSubjectActor ! CustomerSubjectActor.MsgGetCustomerSubject(subject, List(availAdvisorsActor, availRoomsActor))

      println(s"1. CoordinatorActor.MsgGetTimeSlots exit")

    case MsgAdvisorTimeSlots(advApps) =>
      println(s"1. CoordinatorActor.MsgAdvisorTimeSlots entry")

      advAppointments = advApps

      DeliverTimeSlots

      println(s"1. CoordinatorActor.MsgAdvisorTimeSlots exit")

    case MsgRoomTimeSlots(roomApps) =>
      println(s"1. CoordinatorActor.MsgRoomTimeSlots entry - $roomApps")

      roomAppointments = roomApps

      DeliverTimeSlots

      println(s"1. CoordinatorActor.MsgRoomTimeSlots exit")

//    case MsgCustomerSubject(subject) =>
//      println(s"1. CoordinatorActor.MsgRoomTimeSlots entry ($subject)")
//      availAdvisorsActor ! MsgCustomerSubject(subject)
//      availRoomsActor ! MsgCustomerSubject(subject)
//      println(s"1. CoordinatorActor.MsgRoomTimeSlots exit")

    case e @ _ => println(s"1. Invalid message '$e' CoordinatorActor")
  }
}
