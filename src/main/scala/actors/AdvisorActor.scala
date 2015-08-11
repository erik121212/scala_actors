package actors

import akka.actor.{Terminated, Props, ActorRef, Actor}
import akka.routing.{Router, RoundRobinRoutingLogic, ActorRefRoutee}

import domain.Advisor
import domain.Appointment

case object GetAdvisorsAppointments

class AdvisorActor(appointmentActorPool : ActorRef) extends Actor {
  val advisors = List(new Advisor("AA11CC", 20), new Advisor("BB11CC", 15), new Advisor("CC11CC", 25), new Advisor("DD11CC", 10))
  var currentNumberOfAdvisors = 0
  var numberOfAdvisors = 0

  var router = {
//    println("2a. AdvisorActor Create Router")
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[AppointmentActor])
      context watch r
      ActorRefRoutee(r)
    }
//    println("2a. AdvisorActor Created Router")
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case Terminated(a) =>
      println("2a. AdvisorActor.Terminated entry")
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[AppointmentActor])
      context watch r
      router = router.addRoutee(r)
      println("2a. AdvisorActor.Terminated exit")

    case GetAdvisorsAppointments =>
      println("2a. AdvisorActor.GetAdvisorsAppointments entry")
      Thread.sleep(1000) // IO ophalen Advisors

      numberOfAdvisors = advisors.length
      advisors.foreach(adv => router.route(adv, self))

      println("2a. AdvisorActor.GetAdvisorsAppointments exit")

    case apps : List[Appointment] =>
      println("2a. AdvisorActor.List[Appointment] entry")

//      apps foreach println
      currentNumberOfAdvisors  += 1
      if (currentNumberOfAdvisors == numberOfAdvisors) {

        println("2a. AdvisorActor.List[Appointment] Last req received")
      }

      println("2a. AdvisorActor.List[Appointment] exit")

    case e @ _ => println(s"2a. Invalid message '$e' at AdvisorActor")

  }
}

