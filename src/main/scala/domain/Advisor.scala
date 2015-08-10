package domain

import scala.math.Ordering.String

/**
 * Created by m07h817 on 10/Aug/2015.
 */
class Advisor(ck : String ) {
  val corpKey = ck

  override def toString() = corpKey
}
