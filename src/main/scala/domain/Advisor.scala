package domain

import scala.math.Ordering.String

/**
 * Created by m07h817 on 10/Aug/2015.
 */
abstract class Resource(initDelay:Int ) {
  val delay = initDelay
}
class Advisor(initCorpKey : String, initDelay:Int) extends Resource(initDelay) {
  val corpKey = initCorpKey

  override def toString() = corpKey
}
