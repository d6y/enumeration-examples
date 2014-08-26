object SealedEnumPlanets extends App {

object SolarSystemPlanets {

  sealed abstract class Planet(
    val orderFromSun : Int,
    val mass         : Kilogram,
    val radius       : Meter) extends Ordered[Planet] {

      // SealedEnumPlanets$SolarSystemPlanets$Mercury$ -> Mercury (YMMV)
      lazy val name: String = {
        val n = this.getClass.getName.init
        n.substring(1 + n lastIndexOf "$")
      }

      def compare(that: Planet) = this.orderFromSun - that.orderFromSun

      lazy val surfaceGravity = G * mass / (radius * radius)

      def surfaceWeight(otherMass: Kilogram) = otherMass * surfaceGravity

      override def toString = name
  }

  case object Mercury extends Planet(1, 3.303e+23, 2.4397e6)
  case object Venus   extends Planet(2, 4.869e+24, 6.0518e6)
  case object Earth   extends Planet(3, 5.976e+24, 6.3781e6)
  case object Mars    extends Planet(4, 6.421e+23, 3.3972e6)
  case object Jupiter extends Planet(5, 1.9e+27,   7.1492e7)
  case object Saturn  extends Planet(6, 5.688e+26, 6.0268e7)
  case object Uranus  extends Planet(7, 8.686e+25, 2.5559e7)
  case object Neptune extends Planet(8, 1.024e+26, 2.4746e7)

  import EnumerationMacros._
  val planets: Set[Planet] = sealedInstancesOf[Planet]

  type Kilogram = Double
  type Meter    = Double
  private val G = 6.67300E-11 // universal gravitational constant  (m3 kg-1 s-2)
}

  import SolarSystemPlanets._

  println(planets)
  // TreeSet(Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune)

  assert(Earth < Mars)
  // true

  println(planets.filter(_.radius > 7.0e6))
  // TreeSet(Jupiter, Saturn, Uranus, Neptune)

  val yourWeightOnEarth : Kilogram = 70

  val mass: Kilogram = yourWeightOnEarth/Earth.surfaceGravity
  planets.foreach { p =>
    println(f"Your weight on $p%-7s is ${p.surfaceWeight(mass)}%6.1f kg")
  }

}