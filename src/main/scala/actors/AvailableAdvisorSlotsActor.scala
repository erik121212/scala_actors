package actors

import actors.AppointmentsActor.{MsgAvailability, MsgDurationDetermined, MsgAdvisorAppointments}
import actors.AvailableAdvisorSlotsActor.MsgCollectSlots
import actors.CoordinatorActor.MsgAdvisorTimeSlots
import actors.CustomerSubjectActor.MsgCustomerSubject
import akka.actor.{ActorRef, Terminated, Props, Actor}

import domain.{Availability, Advisor, Appointment}

object AvailableAdvisorSlotsActor {
 case class MsgCollectSlots(location: String, question: String, week :Int)
}

class AvailableAdvisorSlotsActor extends Actor {
  val advisors = List(new Advisor("AA11CC", 20), new Advisor("BB11CC", 15), new Advisor("CC11CC", 25), new Advisor("DD11CC", 10))
  var currentNumberOfAdvisors = 0
  var numberOfAdvisors = 0

  var numberOfTimeSlots = 0
//  var timeslots : List[Appointment] = Nil
  var timeslots : List[Availability] = Nil

  var originator: ActorRef = _
  var appointmentActors: List[ActorRef] = Nil

  def receive = {
    case Terminated(a) =>
      println(s"2a. AvailableAdvisorSlotsActor.Terminated entry ($a)")

      println(s"2a. AvailableAdvisorSlotsActor.Terminated exit ($a)")

    case MsgCollectSlots(loc, q, week) =>
      println(s"2a. AvailableAdvisorSlotsActor.MsgCollectSlots entry ($loc, $q, $week)")
      originator = sender()

      Thread.sleep(1000) // IO ophalen Advisors

      numberOfAdvisors = advisors.length
      advisors.foreach(adv => {
        var actor = context.actorOf(Props[AppointmentsActor])
        actor ! MsgAdvisorAppointments(adv, week)
        appointmentActors = actor :: appointmentActors
      })

      println(s"2a. AvailableAdvisorSlotsActor.MsgCollectSlots exit")

    case MsgAvailability(availability) =>
      println("2a. AvailableAdvisorSlotsActor.MsgAppointments entry")

//      apps.foreach(app => println(s"2a. AvailableAdvisorSlotsActor.List[Appointment] $app"))

      numberOfTimeSlots += availability.timeSlots.length
      timeslots =  availability :: timeslots

      currentNumberOfAdvisors += 1
      println(s"2b. AvailableAdvisorSlotsActor.MsgAppointments $currentNumberOfAdvisors of $numberOfAdvisors Terminates received")
      if (currentNumberOfAdvisors == numberOfAdvisors) {
        println(s"2a. AvailableAdvisorSlotsActor.MsgAppointments Last Terminate received ($numberOfTimeSlots timeslots)")
        originator ! MsgAdvisorTimeSlots(timeslots)
        println(s"2a. AvailableAdvisorSlotsActor.MsgAppointments Send $timeslots ($numberOfTimeSlots) to $originator")
        context.system.stop(self)
      }

      println("2a. AvailableAdvisorSlotsActor.MsgAppointments exit")

    case MsgCustomerSubject(subject) =>
      println(s"2a. AvailableAdvisorSlotsActor.MsgCustomerSubject entry ($subject) : $appointmentActors")
      appointmentActors.foreach(actor => {
//        println(s"*** sending MsgDurationDetermined($subject) to $actor")
        actor ! MsgDurationDetermined(subject)
      } )
      println(s"2a. AvailableAdvisorSlotsActor.MsgCustomerSubject exit")

    case e @ _ => println(s"2b. AvailableAdvisorSlotsActor received invalid message '$e'")
  }
}
