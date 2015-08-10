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
      implicit val timeout = Timeout(9 seconds)

      println("CoordinatorActor.MsgGetResources entry")

      val fAdvisorsApps: Future[List[Appointment]] = ask(advisorActor ,GetAdvisorsAppointments).mapTo[List[Appointment]]
      val fRoomsApps: Future[List[Appointment]]  = ask(roomsActor , GetRoomsAppointments).mapTo[List[Appointment]]

      val f = for {
        a <- fAdvisorsApps
        b <- fRoomsApps
      //        c <- ask(MergeAdvAndRoom, a ,b )
      } yield a
      println("CoordinatorActor.MsgGetResources exit")

      f pipeTo sender

    case MsgGetRooms =>
      println("CoordinatorActor.MsgGetRooms entry")
      roomsReceived = true

      if (advisorsReceived && roomsReceived ) {
        //originator ! "done"
      }
      println("CoordinatorActor.MsgGetRooms exit")

    case MsgGetAdvisors =>
      println("CoordinatorActor.MsgGetAdvisors entry")
      advisorsReceived = true

      if (advisorsReceived && roomsReceived ) {
        //originator ! "done"
      }
      println("CoordinatorActor.MsgGetAdvisors exit")

    case _ => println("Invalid message Coordinator")
  }
}
