package domain

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Appointment(initSubject : String, initStartTime : String, initEndTime : String, initAdvisor: Advisor, initRoom : Room) {
  val subject = initSubject
  val startTime = initStartTime
  val endTime = initEndTime
  val advisor = initAdvisor
  val room = initRoom

  def this(initSubject : String, initStartTime : String, initEndTime : String, initAdvisor: Advisor) {
    this(initSubject, initStartTime, initEndTime, initAdvisor, null )
  }
  def this(initSubject : String, initStartTime : String, initEndTime : String, initRoom : Room) {
    this(initSubject, initStartTime, initEndTime, null , initRoom)
  }

  override def toString() =  if (advisor!=null) s"Advisor : $advisor for '$subject' from $startTime - $endTime" else s"Room : $room for '$subject' from $startTime - $endTime"
}
