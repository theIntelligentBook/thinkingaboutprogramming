package willtap.typesTopic

import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.DeckBuilder
import willtap.Common

object TypesTalk {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Types
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
        |""".stripMargin)
    .markdownSlide(
      """## Type Checking
        |
        |The source code you write is in a language written for humans. At some stage, it has to be translated into machine
        |operations.
        |
        |* In a **statically typed** language, the *compiler* (that converts your code from source code to a binary before
        |you can run it) analyses the *type safety* of the program and will throw a *compile error* if it finds problems.
        |Typically, this means that variables and function arguments have types
        |
        |  e.g., Scala, Haskell, Java
        |
        |   ```scala
        |   var a:Int = 1
        |   a = "one"
        |   ```
        |
        |   In Scala, this would give a compile error. We cannot assign a string into variable of type `Int`.
        |
        |* In a **dynamically typed** language, the types of values and operations are checked when the program is run (at "run-time").
        |
        |  e.g., JavaScript, Python
        |
        |  ```js
        |  let a = 1
        |  a = "one"
        |  ```
        |
        |  JavaScript is happy with this. The *variable* (the name `a`) has no type. Only the *value* that is stored in it has a type.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Strong vs Weak Typing
        |
        |Another way in which languages can differ is what they do if they encounter a type discrepancy
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
        |""".stripMargin)
    .markdownSlide(
      """## Still writing the slides!
        |
        |More to come...
        |
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")



  val deck = builder.renderNode



}
