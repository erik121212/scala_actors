package domain

/**
 * Created by m07h817 on 13/Aug/2015.
 */

class CustomerSubject(initSubject: String, initDuration:Int ) {
  val subject = initSubject
  val duration = initDuration

  override def toString() = s"$subject ($duration)"
}
