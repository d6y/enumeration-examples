
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

  assert(WeekDay.Mon < WeekDay.Tue)
  assert(WeekDay.Tue < WeekDay.Wed)
  assert(WeekDay.Wed < WeekDay.Thu)
  assert(WeekDay.Thu < WeekDay.Fri)
  assert(WeekDay.Fri < WeekDay.Sat)
  assert(WeekDay.Sat < WeekDay.Sun)

  // Planet example:
  // Via: http://stackoverflow.com/a/6546480

  object Planet extends Enumeration {
    protected case class Val(name: String, val mass: Double, val radius: Double) extends super.Val(nextId, name) {
     def surfaceGravity: Double = Planet.G * mass / (radius * radius)
     def surfaceWeight(otherMass: Double): Double = otherMass * surfaceGravity
   }

   implicit def valueToPlanetVal(x: Value) = x.asInstanceOf[Val]

   val G: Double = 6.67300E-11
   val Mercury = Val("Mercury", 3.303e+23, 2.4397e6)
   val Venus   = Val("Venus", 4.869e+24, 6.0518e6)
   val Earth   = Val("Earth", 5.976e+24, 6.37814e6)
   val Mars    = Val("Mars", 6.421e+23, 3.3972e6)
   val Jupiter = Val("Jupiter", 1.9e+27, 7.1492e7)
   val Saturn  = Val("Saturn", 5.688e+26, 6.0268e7)
   val Uranus  = Val("Uranus", 8.686e+25, 2.5559e7)
   val Neptune = Val("Neptune", 1.024e+26, 2.4746e7)
 }

 println(Planet.values.filter(_.radius > 7.0e6))
 // ->  Planet.ValueSet(Jupiter, Saturn, Uranus, Neptune)

}