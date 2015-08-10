package domain

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Appointment(st : String, adv: Advisor, rm : Room) {
  val startTime = st
  val advisor = adv
  val room = rm

  def this(st : String, adv: Advisor) {
    this(st, adv, null )
  }
  def this(st : String, rm : Room) {
    this(st, null , rm)
  }

  override def toString() =  if (advisor!=null) "Advisor : "+ advisor + " : "+ startTime else "Room : "+ room + " : "+ startTime
}
