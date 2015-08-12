package actors

import actors.AppointmentsActor.AdvisorAppointments
import actors.AvailableAdvisorSlotsActor.CollectSlots
import actors.CoordinatorActor.AdvisorSlots
import akka.actor.{ActorRef, Terminated, Props, Actor}

import domain.Advisor
import domain.Appointment

object AvailableAdvisorSlotsActor {
 case class CollectSlots(location: String, question: String, week :Int)
}

class AvailableAdvisorSlotsActor(duration: Int) extends Actor {
  val advisors = List(new Advisor("AA11CC", 20), new Advisor("BB11CC", 15), new Advisor("CC11CC", 25), new Advisor("DD11CC", 10))
  var currentNumberOfAdvisors = 0
  var numberOfAdvisors = 0

  var numberOfAppointments = 0
  var appointments : List[Appointment] = Nil

  var originator: ActorRef = _

  def receive = {
    case Terminated(a) =>
      println(s"2a. AvailableAdvisorSlotsActor.Terminated entry ($a)")

      println(s"2a. AvailableAdvisorSlotsActor.Terminated exit ($a)")

    case CollectSlots(loc, q, week) =>
      println(s"2a. AvailableAdvisorSlotsActor.CollectSlots entry ($loc, $q, $week)")
      originator = sender()

      Thread.sleep(1000) // IO ophalen Advisors

      numberOfAdvisors = advisors.length
      advisors.foreach(adv => context.actorOf(Props(new AppointmentsActor(duration))) ! AdvisorAppointments(adv, week))

      println(s"2a. AvailableAdvisorSlotsActor.CollectSlots exit")

    case apps : List[Appointment] =>
      println("2a. AvailableAdvisorSlotsActor.List[Appointment] entry")

//      apps.foreach(app => println(s"2a. AvailableAdvisorSlotsActor.List[Appointment] $app"))

      numberOfAppointments += apps.length
      appointments = appointments ++ apps

      currentNumberOfAdvisors += 1
      println(s"2b. AvailableAdvisorSlotsActor.List[Appointment] $currentNumberOfAdvisors of $numberOfAdvisors Terminates received")
      if (currentNumberOfAdvisors == numberOfAdvisors) {
        println(s"2a. AvailableAdvisorSlotsActor.List[Appointment] Last Terminate received ($numberOfAppointments appointments)")
        originator ! AdvisorSlots(appointments)
        println(s"2a. AvailableAdvisorSlotsActor.List[Appointment] Send $appointments ($numberOfAppointments) to $originator")
        context.system.stop(self)
      }

      println("2a. AvailableAdvisorSlotsActor.List[Appointment] exit")

    case e @ _ => println(s"2b. AvailableAdvisorSlotsActor received invalid message '$e'")
  }
}
