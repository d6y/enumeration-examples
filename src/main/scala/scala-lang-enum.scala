
/*

http://www.scala-lang.org/files/archive/api/2.11.2/#scala.Enumeration

*/

object ScalaLangEnumeration extends App {

  object WeekDay extends Enumeration(initial=0) {
    type WeekDay = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
  }

  import WeekDay._

  val m = WeekDay.values.find(_.toString == "Mon")
  println(s"Found Mon? $m")
  // -> Found Mon? Some(Mon)

  val mv = WeekDay.Mon.id
  println(s"Monday ordinal: $mv")
  // -> Monday ordinal: 0

  // NB: does not warn on lack of exhaustive match:
  def weekend(d: WeekDay.Value) = d match {
    case WeekDay.Sat | WeekDay.Sun => true
  }

}