package actors

import akka.actor.{ActorRef, Actor}
import domain.Advisor
import domain.Appointment
import akka.pattern.ask

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.Await

import scala.concurrent.{ExecutionContext, Promise}

case object GetAdvisorsAppointments

class AdvisorActor(appointmentActorPool : ActorRef) extends Actor {
  val advisors = List(new Advisor("AA11CC"), new Advisor("BB11CC"), new Advisor("CC11CC"), new Advisor("DD11CC"))
  var currentNumberOfAdvisors = 0
  var numberOfAdvisors = 0

  def  receive = {
    case GetAdvisorsAppointments =>
      println(" AdvisorActor.receive entry")
      Thread.sleep(1000) // IO ophalen Advisors

      numberOfAdvisors = advisors.length

      advisors.foreach(adv =>  appointmentActorPool ! adv)

/*val a:List[Future] = advisors.foreach(adv => ask(appointmentActorPool, adv))
Thread.sleep(1000) // IO ophalen Afspraken
sender ! advisors*/
      println(" AdvisorActor.receive exit")

    case apps : List[Appointment] =>
      println(" AdvisorActor.List[Appointment] entry")

      apps foreach println
      currentNumberOfAdvisors  += 1

      if (currentNumberOfAdvisors == numberOfAdvisors) {
        println("AdvisorActor.List[Appointment] Last req received")
      }

      println(" AdvisorActor.List[Appointment] exit")

    case _ => println("Invalid message Advisor")

  }
}

