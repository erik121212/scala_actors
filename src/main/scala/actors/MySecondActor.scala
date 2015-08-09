package actors


import akka.actor.Actor
import akka.actor.Actor.Receive

class MySecondActor  extends Actor {
  def  receive = {
    case _ => Thread.sleep(2000); println("after 2 seconds sleep")
  }
}
