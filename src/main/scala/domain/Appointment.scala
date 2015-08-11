package domain

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Appointment(initStartTime : String, initAdvisor: Advisor, initRoom : Room) {
  val startTime = initStartTime
  val advisor = initAdvisor
  val room = initRoom

  def this(initStartTime : String, initAdvisor: Advisor) {
    this(initStartTime, initAdvisor, null )
  }
  def this(initStartTime : String, initRoom : Room) {
    this(initStartTime, null , initRoom)
  }

  override def toString() =  if (advisor!=null) "Advisor : "+ advisor + " : "+ startTime else "Room : "+ room + " : "+ startTime
}
