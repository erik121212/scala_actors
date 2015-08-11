package actors

import akka.actor.{ActorRef, Actor}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._
import domain._

case object MsgGetTimeSlots
case object MsgGetAdvisors
case object MsgGetRooms

class CoordinatorActor(advisorActor: ActorRef, roomsActor: ActorRef)  extends Actor {
  import context.dispatcher
   private var roomsReceived = false
   private var advisorsReceived = false
  //private val originator ActorRef

  def  receive = {

    case MsgGetTimeSlots =>
      implicit val timeout = Timeout(3 seconds)

      println("1. CoordinatorActor.MsgGetTimeSlots entry")

/*
      val fAdvisorsApps: Future[List[Appointment]] = ask(advisorActor ,GetAdvisorsAppointments).mapTo[List[Appointment]]
      val fRoomsApps: Future[List[Appointment]]  = ask(roomsActor , GetRoomsAppointments).mapTo[List[Appointment]]

      val f = for {
        a <- fAdvisorsApps
        b <- fRoomsApps
      //        c <- ask(MergeAdvAndRoom, a ,b )
      } yield a ::: b
*/
      advisorActor ! GetAdvisorsAppointments
      roomsActor ! GetRoomsAppointments

      println("1. CoordinatorActor.MsgGetTimeSlots step 1")

//      f pipeTo sender
      println("1. CoordinatorActor.MsgGetTimeSlots exit")

    case apps: List[Appointment] =>
      println("1. CoordinatorActor.List[Appointment] entry")

      apps foreach println

      println("1. CoordinatorActor.List[Appointment] exit")

    case MsgGetRooms =>
      println("1. CoordinatorActor.MsgGetRooms entry")
      roomsReceived = true

      if (advisorsReceived && roomsReceived ) {
        //originator ! "done"
      }
      println("1. CoordinatorActor.MsgGetRooms exit")

    case MsgGetAdvisors =>
      println("1. CoordinatorActor.MsgGetAdvisors entry")
      advisorsReceived = true

      if (advisorsReceived && roomsReceived ) {
        //originator ! "done"
      }
      println("1. CoordinatorActor.MsgGetAdvisors exit")

    case e @ _ => println(s"1. Invalid message '$e' CoordinatorActor")
  }
}
