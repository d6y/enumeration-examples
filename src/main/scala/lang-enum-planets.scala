/*
The Oracle Java enum Planets example using the Klang DIY Enum, modified to include ordering.
*/
object KlangEnumPlanets extends App {

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


  // Planet example:
  object Planets extends Enum {
    sealed abstract class EnumVal(val mass: Double, val radius: Double) extends Value {
      def surfaceGravity: Double = G * mass / (radius * radius)
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

  println(Planets.values.filter(_.radius > 7.0e6))
  // -> Vector(Jupiter, Saturn, Uranus, Neptune)

  import Planets._

  println(EARTH < MARS)
  // -> true

  println(JUPITER)
  // -> Jupiter

  def inner(planet: Planets.EnumVal): Boolean =
    planet match {
      case MERCURY | VENUS | EARTH | MARS => true
    }
  // [warn] It would fail on the following inputs: $anon(), $anon(), $anon(), $anon(), $anon(), $anon(), $anon(), $anon()
  // [warn]     planet match {
  // [warn]     ^

}