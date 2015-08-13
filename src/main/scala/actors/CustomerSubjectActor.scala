package actors

import actors.CustomerSubjectActor.{MsgCustomerSubject, MsgGetCustomerSubject}
import akka.actor.{ActorRef, Actor, Terminated}
import domain.CustomerSubject

/**
 * Created by m07h817 on 13/Aug/2015.
 */
object CustomerSubjectActor{
  case class MsgGetCustomerSubject(subject : String, notifyActors : List[ActorRef])
  case class MsgCustomerSubject(subject : CustomerSubject)
}

class CustomerSubjectActor extends Actor {

  def  receive = {
    case Terminated(a) =>
      println(s"2c. CustomerSubjectActor.Terminated entry ($a)")

      println(s"2c. CustomerSubjectActor.Terminated exit ($a)")

    case MsgGetCustomerSubject(subject, notifyActors) =>
      println(s"2c. CustomerSubjectActor.MsgGetCustomerSubject entry ($subject)")

      Thread.sleep(1150) // IO ophalen CustomerSubject

      notifyActors.foreach(actor => actor ! MsgCustomerSubject(new CustomerSubject(subject, 60)))

      println(s"2c. CustomerSubjectActor.MsgGetCustomerSubject exit ($subject)")
      context.system.stop(self)

    case e@_ => println(s"1. Invalid message '$e' CustomerSubjectActor")
  }
}
