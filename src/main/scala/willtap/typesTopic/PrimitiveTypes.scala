package willtap.typesTopic

import coderunner.JSCodable
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.doctacular.{Challenge, DeckBuilder}
import lavamaze.Maze
import willtap.{Common, given}

object PrimitiveTypes {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Our First Types
        |
        |or *What are you talking about?*
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## What are you talking about?
        |
        |Some actions only make sense on certain types of data. Even in natural language, this becomes apparent
        |
        |Let's start with some sums...
        |""".stripMargin)
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/1plus1.jpg"), <("figcaption")("1 + 1 = ?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/2.jpg"), <("figcaption")("1 + 1 = 2")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/redplusgreen.jpg"), <("figcaption")("red + green = ?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/yellow.jpg"), <("figcaption")("red + green = yellow")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/redplusrabbit.jpg"), <("figcaption")("red + rabbit = ?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/redrabbit.jpg"), <("figcaption")("red + rabbit = a red rabbit")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/rabbitplusrabbit.jpg"), <("figcaption")("rabbit + rabbit = ?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/babyrabbits.jpg"), <("figcaption")("rabbit + rabbit = lots of baby rabbits")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/1plusred.jpg"), <("figcaption")("1 + red = ?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/1plusrabbit.jpg"), <("figcaption")("1 + rabbit = ?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/oneplusone.jpg"), <("figcaption")(""""one" + "one" = ?""")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/two.jpg"), <("figcaption")(""""one" + "one" = "two"""")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/types/oneone.jpg"), <("figcaption")(""""one" + "one" = "oneone"""")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## Types and operations
        |
        |Even in natural language, it makes sense to us that
        |
        |* Some operations only make sense on certain *types* of thing.
        |
        |* Some operations *mean* different things when applied to different types of thing
        |
        |This is also true in programming languages, but programming languages formally specify a *type system*.
        |
        |*Type errors* occur when a program tries to apply an operation to a type it is not valid for.
        |
        |*Type checking* is the process of checking for type errors
        |
        |""".stripMargin)
    .markdownSlide(
      """## Statically Typed Languages
        |
        |The source code you write is in a language written for humans. At some stage, it has to be translated into machine
        |operations.
        |
        |  In a **statically typed** language, the *compiler* (a program that converts your code from source code to a
        |  binary before you can run it) analyses the *type safety* of the program and will throw a *compile error* if
        |  it finds problems.
        |
        |  e.g.: Scala, Java, C++
        |
        |
        |  If there's a problem in your code, you might not be able to compile it, so you can't run it at all.
        |
        |  Typically, this means that variables and function arguments have types
        |
        |   ```scala
        |   var a:Int = 1
        |   a = "one"
        |   ```
        |
        |   In Scala, this would give a compile error. We cannot assign a string into variable of type `Int`.
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Dynamically Typed Languages"),
      Challenge.split(
        Common.markdown(
          """In a **dynamically typed** language, the types of values and operations are checked when the program is run (at "run-time").
            |
            |e.g., JavaScript, Python
            |
            |```js
            |let a = 1
            |a = "one"
            |```
            |
            |JavaScript is happy with this. The *variable* (the name `a`) has no type. Only the *value* that is stored in it has a type.
            |
            |This means most type errors will occur at *run-time* -- you won't know you've got a bug until you hit it.
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Strong vs Weak Typing"),
      Challenge.split(
        Common.markdown(
          """Another way in which languages can differ is what they do if they encounter a type discrepancy
            |
            |For example, what is `1 + true`?
            |
            |* **Strongly typed** languages will throw an error
            |
            |   Scala: `None of the alternatives of + in class Int match arguments (true: Boolean)`
            |
            |* **Weakly typed** languages will try to find a *type conversion* that will satisfy the operation
            |
            |   JavaScript: `2`
            |
            |Because JavaScript is *dynamically*, *weakly* typed, mistakes in your program will sometimes show up
            |through the program behaving unexpectedly, rather than an error being thrown.
            |
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Truthy and falsey"),
      Challenge.split(
        Common.markdown(
          """One particular area to watch out for is in `if` statements.
            |
            |We describe `if` in terms of `true` and `false` but it is possible to put something else into the condition
            |of an if -- in which case JavaScript will convert it into a boolean rather than throw an error.
            |
            |Accordingly, some values in JS are considered *truthy* and some are considered *falsey*.
            |
            |```js
            |let a = "false";
            |if (a) {
            |  println("But I thought a was false?")
            |}
            |```
            |
            |Let's try a few more on the next slide
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Truthy and falsey"),
      Challenge.split(
        Common.markdown(
          """One particular area to watch out for is in `if` statements.
            |
            |We describe `if` in terms of `true` and `false` but it is possible to put something else into the condition
            |of an if -- in which case JavaScript will convert it into a boolean rather than throw an error.
            |
            |Accordingly, some values in JS are considered *truthy* and some are considered *falsey*.
            |
            |```js
            |let a = "false";
            |if (a) {
            |  println("But I thought a was false?")
            |}
            |```
            |
            |Let's try a few more on the next slide
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("What's Truthy? What's falsey"),
      Challenge.split(
        Common.markdown(
          """Truthy:
            |
            |* Numbers other than `0`
            |* Non-empty strings. Even `"false"`
            |* Functions (easy mistake to make if you miss the parentheses)
            |* Objects and arrays
            |
            |Falsey:
            |
            |* `0`
            |* `undefined`, `null`
            |* The empty string, `""`
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Type Conversions"),
      Challenge.split(
        Common.markdown(
          """Because JavaScript is weakly typed, type conversions can give you some unexpected results
            |
            |* e.g., "true" converts to 1, so `1 + true` is `2`
            |
            |* `"0" + 0` versus `"1" - 1`
            |
            |Because it is dynamically typed, also applies within functions
            |
            |```js
            |function addOne(x) {
            |  return x + 1
            |}
            |```
            |
            |`addOne(1)` vs `addOne("1")`
            |
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Two kinds of equality"),
      Challenge.split(
        Common.markdown(
          """When testing whether values are equal, in JavaScript you have to decide whether you want to let it do a
            |type conversion
            |
            |* The `==` and `!=` operators allow type conversions
            |
            |  `"1" == 1` is true, and `"1" != 1` is false
            |
            |* The `===` and `!==` operators do not allow type conversions.
            |
            |  These are the "strict equality" and "strict inequality" operators
            |
            |  `"1" === 1` is *false*. `"1" !== 1` is *true*
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .markdownSlide(
      """## Types in JavaScript
        |
        |JavaScript has six "language types":
        |
        |* `Boolean`, has values `true` and `false`
        |
        |* `Number`. e.g., `12` or `123.4` or `Infinity`
        |
        |* `String`, sequences of characters, e.g `"hello world"`, using the UTF-16 character set (how characters are encoded into binary)
        |
        |* `Object`. A collection of properties... curiously, includes functions and arrays
        |
        |* `undefined` (has exactly one value, `undefined`)
        |
        |* `null` (has exactly one value, `null`.)
        |
        |`typeof` will give you the type of a value. e.g, `typeof(true)` produces `"boolean"`
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Booleans"),
      Challenge.split(
        Common.markdown(
          """Booleans have values `true` and `false`
            |
            |There are various *logical operators* we can apply to them.
            |
            |* `&&` is the *logical* AND operator.
            |
            |* `||` is the *logical* OR operator.
            |
            |* `!` is logical negation. `!true` is `false`
            |
            |* There is no logical XOR, but if you know both arguments are boolean, you can use `!=`
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .markdownSlide(
      s"""## Numbers
         |
         |Most languages treat integers and decimals differently. JavaScript tries to keep them (mostly) in one type.
         |
         |JavaScript's number type is "double precision floating point". We won't need to look inside it much, but it
         |can sometimes be useful to know a little.
         |
         |* "Floating point" means numbers that can have a decimal point anywhere.  \t
         |  e.g, `1.2345` or `123.45`, as opposed to numbers that always have the same number of digits after the
         |  decimal point
         |
         |* "Double precision" means it uses 64 bits (1s and 0s) to store the number.  \t
         |  "Single precision" would be 32 bits.
         |
         |Imagine it like in mathematics, storing
         |
         |* 1,234 as <i>1.234 &times; 10<sup>3</sup></i>
         |
         |* 0.00001234 as <i>1.234 &times; 10<sup>-5</sup></i>
         |
         |* -0.00001234 as <i>-1 &times; 1.234 &times; 10<sup>-5</sup></i>
         |
         |A number has a *sign*, some digits (the *mantissa*) and an *exponent*
         |
         |""".stripMargin)
    .markdownSlide(
      """## Aside: inside JavaScript's numbers
        |
        |* A bit to indicate the sign; `1` = negative, `0` = positive
        |* 52 bits to represent what the digits are (the *mantissa*)
        |* 11 bits to represent an *exponent* - effectively, where to put the decimal point
        |
        |But this format can look very odd. e.g.,
        |
        |* Bit pattern `0 01111111111 0000000000000000000000000000000000000000000000000000` is the number 1
        |
        |* Bit pattern `0 00000000001 0000000000000000000000000000000000000000000000000000` is approx 1.79 * 10<sup>308</sup>
        |
        |* Bit pattern `0 11111111110 1111111111111111111111111111111111111111111111111111` is approx 2.2 * 10<sup>-308</sup>
        |
        |* Bit pattern `0 00000000000 0000000000000000000000000000000000000000000000000000` is "positive 0"
        |
        |* Bit pattern `1 00000000000 0000000000000000000000000000000000000000000000000000` is "negative 0"
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Some special numbers"),
      Challenge.split(
        Common.markdown(
          s"""Double precision numbers have some special values
            |
            |* There's `Infinity` *and* `-Infinity`.
            |
            |* There's `+0` *and* `-0`.  (Though they are equal and both print as `0`)  \t
            |  `1 / Infinity` is `+0`, but `-1 / Infinity` is `-0`
            |
            |* `NaN` means "not a number"
            |
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("BigInt"),
      Challenge.split(
        Common.markdown(
          s"""For numbers bigger than 9,007,199,254,740,991, ordinary numbers can "lose precision"
             |(52 bits aren't enough to hold all the digits)
             |
             |For this, there's `BigInt`. To use it, put an `n` at the end of the number
             |
             |e.g. `13841209347193750243528345984735n` * `293457293452678345872634578623495682834n` is  \t
             |`4061803833139390845148434322359519044487901182788181036016950965538990`
             |
             |BigInts can be of arbitrary length
             |
             |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Aside: Something weird JavaScript does..."),
      Challenge.split(
        Common.markdown(
          s"""When learning about binary, you might have been introduced to *bitwise* operations such as `&` and `|`, e.g.
             |on [Circuits Up!](https://theintelligentbook.com/circuitsup/#/binary/2/0)
             |
             |We always describe that in terms of *integer* types (no exponent), but all JavaScript's numbers have bits for
             |an exponent. How is that going to work?
             |
             |When you use any of the bitwise operators on a number, e.g.,
             |
             |* `5 | 3`, which is binary *101 OR 011*,
             |* `5 & 3`, which is binary *101 AND 011*,
             |* `5 ^ 3`, which is binary *101 XOR 011*, or
             |* `~5`, which is binary negation of *101*
             |
             |JavaScript internally converts the numbers to 32-bit integers (no floating point), performs the operation,
             |then reformats the result back into a number.
             |
             |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
      )
    ))
    .markdownSlide(
      """## Next time...
        |
        |So far, we've just covered the *primitive types*.
        |
        |Next time, we'll cover the *object types*.
        |
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")



  val deck = builder.renderSlides



}
