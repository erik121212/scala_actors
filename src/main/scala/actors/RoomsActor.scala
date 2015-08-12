package actors


import akka.actor.{Terminated, Props, ActorRef, Actor}
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}
import domain.{Appointment, Room}


import scala.concurrent.{ExecutionContext, Promise}

case object GetRoomsAppointments

class RoomsActor(appointmentActorPool : ActorRef) extends Actor {

  val rooms = List(new Room("a1@b.c", 20), new Room("a2@b.c", 25))
  var currentNumberOfRooms = 0
  var numberOfRooms = 0

  var router = {
//    println("2b. RoomsActor Create Router")
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[AppointmentActor])
      context watch r
      ActorRefRoutee(r)
    }
//    println("2b. RoomsActor Created Router")
    Router(RoundRobinRoutingLogic(), routees)
  }

  def  receive = {
    case Terminated(a) =>
      println("2b. RoomsActor.Terminated entry")
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[AppointmentActor])
      context watch r
      router = router.addRoutee(r)
      println("2b. RoomsActor.Terminated exit")

    case GetRoomsAppointments =>
      println("2b. RoomsActor.GetRoomsAppointments entry")
      Thread.sleep(1000) // IO ophalen Advisors

      numberOfRooms = rooms.length
      rooms.foreach(room => router.route(room, self))

      println("2b. RoomsActor.GetRoomsAppointments exit")

    case apps: List[Appointment] =>
      println("2b. RoomsActor.List[Appointment] entry")

//      apps foreach println
      currentNumberOfRooms += 1
      if (currentNumberOfRooms == numberOfRooms) {
        println("2b. RoomsActor.List[Appointment] Last req received")
      }

      println("2b. RoomsActor.List[Appointment] exit")

    case e @ _ => println(s"2b. Invalid message '$e' at RoomActor")
  }
}

