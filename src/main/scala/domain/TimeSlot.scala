package domain

/**
 * Created by m07h817 on 13/Aug/2015.
 */
abstract class Availability(initTimeSlots:List[TimeSlot])
{
  val timeSlots = initTimeSlots
}
class AvailabilityAdvisor(initAdvisor:Advisor, initTimeSlots:List[TimeSlot]) extends Availability(initTimeSlots)
{
  val advisor = initAdvisor

  override def toString() =  s"Advisor $advisor free on $timeSlots"
}
class AvailabilityRoom(initRoom:Room, initTimeSlots:List[TimeSlot]) extends Availability(initTimeSlots)
{
  val room = initRoom

  override def toString() =  s"Room $room free on $timeSlots"
}

class TimeSlot(initStartTime : String, initEndTime : String) {
    val startTime = initStartTime
    val endTime = initEndTime

    override def toString() =  s"$startTime - $endTime"
}
