object SealedEnum extends App {

// NB: note how this can be built up. E.g., if you don't care about a name, ID or order, remove it
 object WeekDay {
    sealed abstract class EnumVal(val id: Int, val name: String) extends Ordered[EnumVal] {
      def compare(that: EnumVal) = this.id - that.id
    }
    case object Mon extends EnumVal(0, "Mon")
    case object Tue extends EnumVal(1, "Tue")
    case object Wed extends EnumVal(2, "Wed")
    case object Thu extends EnumVal(3, "Thu")
    case object Fri extends EnumVal(4, "Fri")
    case object Sat extends EnumVal(5, "Sat")
    case object Sun extends EnumVal(6, "Sun")

    val values = Seq(Mon, Tue, Wed, Thu, Fri, Sat, Sun)
  }

  val m = WeekDay.values.find(_.name == "Mon")
  println(s"Found Mon? $m")

  val mv = WeekDay.Mon.id
  println(s"Monday ordinal: $mv")

  assert(WeekDay.Mon < WeekDay.Tue)
  assert(WeekDay.Tue < WeekDay.Wed)
  assert(WeekDay.Wed < WeekDay.Thu)
  assert(WeekDay.Thu < WeekDay.Fri)
  assert(WeekDay.Fri < WeekDay.Sat)
  assert(WeekDay.Sat < WeekDay.Sun)

  def weekend(d: WeekDay.EnumVal) = d match {
    case WeekDay.Sat | WeekDay.Sun => true
  }
  /*
  sealed-enum.scala:24: match may not be exhaustive.
  [warn] It would fail on the following inputs: Fri, Mon, Thu, Tue, Wed
  [warn]   def weekend(d: WeekDay.EnumVal) = d match {
  ...which is a good thing.
  */

}