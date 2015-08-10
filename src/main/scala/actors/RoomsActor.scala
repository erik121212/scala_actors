package actors


import akka.actor.{ActorRef, Actor}
import domain.{Appointment, Room}


import scala.concurrent.{ExecutionContext, Promise}

case object GetRoomsAppointments

class RoomsActor(appointmentActorPool : ActorRef) extends Actor {

  val rooms = List(new Room("a1@b.c"), new Room("a2@b.c"))
  var currentNumberOfRooms = 0
  var numberOfRooms = 0

  def  receive = {
    case GetRoomsAppointments =>
      println(" RoomsActor.receive entry")
      Thread.sleep(1000) // IO ophalen Advisors

      numberOfRooms = rooms.length

      rooms.foreach(room => appointmentActorPool ! room)

      /*val a:List[Future] = advisors.foreach(adv => ask(appointmentActorPool, adv))
      Thread.sleep(1000) // IO ophalen Afspraken
      sender ! advisors*/
      println(" AdvisorActor.receive exit")

    case apps: List[Appointment] =>
      println(" RoomsActor.List[Appointment] entry")

      apps foreach println
      currentNumberOfRooms += 1

      if (currentNumberOfRooms == numberOfRooms) {
        println("RoomsActor.List[Appointment] Last req received")
      }

      println(" RoomsActor.List[Appointment] exit")

    case _ => println("Invalid message Room")
  }
}

