package domain

import scala.math.Ordering.String

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Advisor(initCorpKey : String, initDelay:Int ) {
  val corpKey = initCorpKey
  val delay = initDelay

  override def toString() = corpKey
}
