package domain

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Room (initMailBox:String, initDelay:Int ){
  val mailBox = initMailBox
  val delay = initDelay

  override def toString() = mailBox

}
