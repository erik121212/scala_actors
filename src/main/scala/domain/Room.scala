package domain

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Room (initMailBox:String, initDelay:Int ) extends Resource(initDelay){
  val mailBox = initMailBox

  override def toString() = mailBox

}
