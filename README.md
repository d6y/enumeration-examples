# Scala Enumerations

This repository demonstrates the pros and cons of `scala.Enumeration` and examines alternative structures.  The objective is to help you evaluate which kind of enumeration suits your needs.

## Summary

TODO

----

## Why do we need to do this?

`scala.Enumeration` is an example of adding a feature to Scala via a library, and not a language change (_Programming in Scala_, [p. 453](http://www.artima.com/pins1ed/abstract-members.html#20.8)).  This is contrasted with the Java language, which did not gain enumerations until the language itself was changed in Java 5 (_The Java Language Specification_, third edition, [section 8.9](http://docs.oracle.com/javase/specs/jls/se5.0/html/classes.html#8.9)).

However, this does mean the enumeration support in Scala depends on what can be achieved with the language as-it-stands. For some, the version of `Enumeration` include in the Scala standard library is not considered good enough.

For examples of his view see:

> "Enumerations must D.I.E."
[on scala-internal](https://groups.google.com/forum/#!topic/scala-internals/8RWkccSRBxQ).

And comments such as:


> "Now I officially hate scala.Enumeration"
[from @hseeberger and others](https://twitter.com/hseeberger/status/308514922768236544).

To explore this we will recap the issues with `scala.Enumeration` and look at the alternatives.

## The problems with scala.Enumeration


1. Enumerations have the same type after erasure.
2. No exhaustive matching during compile.
3. They look a bit weird.
4. Java Interoperability.

We will look in more detail at these in a moment. But let first me ask: what do we mean by an enumeration? It has different connotations to different people.


## What do you want from an enumeration?

- type-safe access to the members
- pattern matching
- exhaustiveness test on pattern matching
- iterate over the members
- ability to go beyond an identifier and string
- serialisation
- efficiency of representation

That last one may seem a small point, but it gets to the heart of what it means to be an enumeration for some.  For example:

> I really think that enums should be lightweight. One class (or even two) per value is not acceptable. If you are willing to pay that sort of price, it's not too burdensome to just define the case objects directly. Enums fill a different niche: essentially as efficient as integer constants but safer and more convenient to define and to use. ([Martin on the mailing list](https://groups.google.com/d/msg/scala-internals/8RWkccSRBxQ/U4y0XpRJfdQJ))

We might also add:

- values are associated with consecutive integers
- values print as the name with which they were defined
- values are ordered

...depending on what you expect of an enumeration (again, see comments from [scala-internals](https://groups.google.com/d/msg/scala-internals/8RWkccSRBxQ/NUwHtj-VsjQJ)).



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


## Our tests



## Alternatives

### Sealed traits

(Example now in this project)

* you need to add your own list of values, which is error prone


### Macros

http://stackoverflow.com/questions/13671734/iteration-over-a-sealed-trait-in-scala
https://github.com/cb372/enumerate


### The Klang Enum

https://gist.github.com/viktorklang/1057513


### Using Java Enumerations

If you want to interop from Scala into Java as a Enum, write a Java Enum.

### Shapeless?

What does shapeless say about enumerations?

### Scalaz?

Does Scalaz say anything about enumerations?

-----

# Conclusions

TODO


