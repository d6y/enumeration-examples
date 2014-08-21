# Scala Enumerations


Enumeration an example of adding a feature via a library, not a language change.

A look at the problems and alternatives to `scala.Enumeration`.


> "Enumerations must D.I.E."
[on scala-internal](https://groups.google.com/forum/#!topic/scala-internals/8RWkccSRBxQ).


> "Now I officially hate scala.Enumeration"
[says @hseeberger and others](https://twitter.com/hseeberger/status/308514922768236544).


----

## Problems

1. Enumerations have the same type after erasure.
2. No exhaustive matching during compile.
3. They look a bit weird.
4. Java Interoperability.

### Same type after erasure

	scala> object Colours extends Enumeration {
	     |   val Red, Amber, Green = Value
	     | }
	defined object Colours

	scala> object Weekdays extends Enumeration {
	     |  val Mon,Tue,Wed,Thu,Fri = Value
	     | }
	defined object Weekdays

	scala> object Fs {
	     | def f(x: Colours.Value) = "colour"
	     | def f(x: Weekdays.Value) = "weekday"
	     | }
	<console>:11: error: double definition:
	def f(x: Colours.Value): String at line 10 and
	def f(x: Weekdays.Value): String at line 11
	have same type after erasure: (x: Enumeration#Value)String
	       def f(x: Weekdays.Value) = "weekday"
           ^


Or if you prefer:


	scala> def light(c: Colours.Value) = s"Lighting up all $c"
	light: (c: Colours.Value)String

	scala> light(Colours.Red)
	res5: String = Lighting up all Red

	scala> light(Weekdays.Mon)
	<console>:11: error: type mismatch;
	 found   : Weekdays.Value
	 required: Colours.Value
	              light(Weekdays.Mon)

	scala> light(Weekdays.Mon.asInstanceOf[Colours.Value])
	res7: String = Lighting up all Mon


)


### No exhaustive matching during compile


	scala> def traffic(colour: Colours.Value) = colour match {
	     | case Colours.Green => "Go"
	     | }
	trafficControl: (colour: Colours.Value)String


### The look a bit weird

TODO

### Java Interop

TODO

-----


## What do you want from an Enumeration?

- access to the members
- pattern matching + exhaustiveness
- extending methods on them?
- serialization
- one class?
- ?

Regarding one class:

> I really think that enums should be lightweight. One class (or even two) per value is not acceptable. If you are willing to pay that sort of price, it's not too burdensome to just define the case objects directly. Enums fill a different niche: essentially as efficient as integer constants but safer and more convenient to define and to use.
from [Martin on the mailing list](https://groups.google.com/d/msg/scala-internals/8RWkccSRBxQ/U4y0XpRJfdQJ)


## Alternatives

### Sealed traits

(Example now in this project)

* you need to add your own list of values, which is error prone


### The Klang Enum

https://gist.github.com/viktorklang/1057513


### Macros

http://stackoverflow.com/questions/13671734/iteration-over-a-sealed-trait-in-scala
https://github.com/cb372/enumerate


### Using Java Enumerations

If you want to interop from Scala into Java as a Enum, write a Java Enum.

### Shapeless?

What does shapeless say about enumerations?

### Scalaz?

Does Scalaz say anything about enumerations?

-----

# Conclusions

TODO


