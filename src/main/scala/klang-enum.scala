/*

https://gist.github.com/viktorklang/1057513

...but modified to add ordering

*/
object KlangEnum extends App {

  trait Enum { //DIY enum type
    import java.util.concurrent.atomic.AtomicReference //Concurrency paranoia

    type EnumVal <: Value //This is a type that needs to be found in the implementing class

    private val _values = new AtomicReference(Vector[EnumVal]()) //Stores our enum values

    //Adds an EnumVal to our storage, uses CCAS to make sure it's thread safe, returns the ordinal
    private final def addEnumVal(newVal: EnumVal): Int = { import _values.{get, compareAndSet => CAS}
      val oldVec = get
      val newVec = oldVec :+ newVal
      if((get eq oldVec) && CAS(oldVec, newVec)) newVec.indexWhere(_ eq newVal) else addEnumVal(newVal)
    }

    def values: Vector[EnumVal] = _values.get //Here you can get all the enums that exist for this type

    //This is the trait that we need to extend our EnumVal type with, it does the book-keeping for us
    protected trait Value extends Ordered[Value] { self: EnumVal => //Enforce that no one mixes in Value in a non-EnumVal type
      final val ordinal = addEnumVal(this) //Adds the EnumVal and returns the ordinal

      def compare(that: Value) = this.ordinal - that.ordinal

      def name: String //All enum values should have a name

      override def toString = name //And that name is used for the toString operation
      override def equals(other: Any) = this eq other.asInstanceOf[AnyRef]
      override def hashCode = 31 * (this.getClass.## + name.## + ordinal)
    }
  }


 object WeekDay extends Enum {
    sealed trait EnumVal extends Value
    val Mon = new EnumVal { val name = "Mon" }
    val Tue = new EnumVal { val name = "Tue" }
    val Wed = new EnumVal { val name = "Wed" }
    val Thu = new EnumVal { val name = "Thu" }
    val Fri = new EnumVal { val name = "Fri" }
    val Sat = new EnumVal { val name = "Sat" }
    val Sun = new EnumVal { val name = "Sun" }
  }

  import WeekDay._

  val m = WeekDay.values.find(_.name == "Mon")
  println(s"Found Mon? $m")

  val mv = WeekDay.Mon.ordinal
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
  klang-enum.scala:51: match may not be exhaustive.
  [error] It would fail on the following inputs: $anon(), $anon(), $anon(), $anon(), $anon(), $anon(), $anon()
  [error]   def weekend(d: WeekDay.EnumVal) = d match {
  */

  // Planet example:
  object Planet extends Enum {
    sealed abstract class EnumVal(val mass: Double, val radius: Double) extends Value {
      def surfaceGravity: Double = Planet.G * mass / (radius * radius)
      def surfaceWeight(otherMass: Double): Double = otherMass * surfaceGravity
    }
    val MERCURY = new EnumVal(3.303e+23, 2.4397e6) { val name = "Mercury" }
    val VENUS   = new EnumVal(4.869e+24, 6.0518e6) { val name = "Venus"   }
    val EARTH   = new EnumVal(5.976e+24, 6.37814e6){ val name = "Earth"   }
    val MARS    = new EnumVal(6.421e+23, 3.3972e6) { val name = "Mars"    }
    val JUPITER = new EnumVal(1.9e+27,   7.1492e7) { val name = "Jupiter" }
    val SATURN  = new EnumVal(5.688e+26, 6.0268e7) { val name = "Saturn"  }
    val URANUS  = new EnumVal(8.686e+25, 2.5559e7) { val name = "Uranus"  }
    val NEPTUNE = new EnumVal(1.024e+26, 2.4746e7) { val name = "Neptune" }

    private val G: Double = 6.67300E-11
  }

  println(Planet.values.filter(_.radius > 7.0e6))
  // -> Vector(Jupiter, Saturn, Uranus, Neptune)

}