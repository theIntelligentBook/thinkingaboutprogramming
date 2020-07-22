package willtap.typesTopic

import coderunner.JSCodable
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import lavamaze.Maze
import willtap.Common

object ObjectTypes {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Object and Reference Types
        |
        |(and strings)
        |
        |""".stripMargin).withClass("center middle")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/objects/two of you.png"), <("figcaption")("Suppose there was a copy of you...")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/objects/arrow on one.png"), <("figcaption")("Pointing at one...")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/objects/dyed hair.png"), <("figcaption")("Making a change...")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      s"""## Identity vs Equality
        |
        |There is often a distinction between *value types* and *reference types*
        |
        |* For a *value type*, a variable's content is the value you might expect. e.g. `1`, `true`. Many different
        |  variables might have the same value
        |
        |* For a *reference type*, a variables content is *a reference to an item in memory*. Variables might point to
        |  the same item in memory, or they might point to different items in memory that just happen to have the same
        |  content.
        |
        |This can lead to a difference between *identity* and *equality* for reference types
        |
        |* Identity (or reference equality) is whether the references *point to the same item*.  \t
        |  In JavaScript, typically checked with `==`  \t
        |  e.g., `if (documentA == documentB)`. So,
        |
        |  ```js
        |  let a = { "name": "Algernon" }
        |  let b = { "name": "Algernnon" }
        |  console.log(a == b) // false, they have the same content but are different items in memory
        |  b = a
        |  console.log(a == b) // true, a and b now point to the same item
        |  ```
        |
        |* Equality may be harder to define...
        |""".stripMargin)
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/objects/cards.png"), <("figcaption")("Are these equal?")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/objects/one signed.png"), <("figcaption")("Are these equal?")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## Object equality in different languages
        |
        |* In some languages, e.g. Java and Scala (not JavaScript), you can define what equality should mean for an
        |  object. Typically by defining an `equals()` method. Languages differ in how this works - e.g. in Scala this
        |  affects the `==` operator but in Java it doesn't.
        |
        |* In JavaScript, there is only reference equality, but there are some libraries you can use
        |  (e.g. Lodash) that include an `isEqual` function that can compare two objects by their content.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Value types vs reference types in JavaScript
        |
        |**⭣ Value types ⭣** (primitive types)
        |
        |* Boolean
        |
        |* Number
        |
        |* undefined
        |
        |* null
        |
        |---
        |
        |* String -- *a primitive type wrapped to be somewhat reference-like*
        |
        |---
        |
        |* Object (including arrays and functions)
        |
        |**⭡ Reference types ⭡**
        |""".stripMargin)
    .markdownSlide(
      """## Strings in JavaScript
        |
        |Strings are implemented as a primitive type but can also be treated as if they were objects
        |
        |* They are compared by *value* (so, `Algernon` == `Algernon` is `true`)
        |
        |* They have *methods* - functions that belong to the object and operate on the object, e.g.:
        |
        |  ```js
        |  let s = "Algernon".toUpperCase()
        |  console.log(s) // ALGERNON
        |  ```
        |
        |* They have *properties*, eg. `"Algernon".length`, but these properties cannot be updated, e.g.:
        |
        |  ```js
        |  let s = "Algernon"
        |  s.length = 4
        |  console.log(s.length) // 8 - we have not actually managed to alter the length property
        |  ```
        |""".stripMargin)
    .markdownSlide(
      s"""## Some methods and operations on String
        |
        |* `"Algernon" < "Bertie"` is `true`. \t
        |  `"Algernon" < "algernon"` is `true`.  \t
        |  `"1gernon" < "2gernon"` is `true`  \t
        |  Alphanumeric comparison
        |
        |* `"Algernon".slice(1, -1)` - `slice` is a convenient way to return part of a string
        |
        |* `"Algernon".charAt(1)` - `charAt` returns the character at a particular location (as a string)
        |
        |* `"Algernon"[1]` - strings can also be accessed similarly to if they were an array of characters
        |
        |* `"hello world".split(" ")` - `split` splits a string into an array of strings. If you split on `"\n"`, it
        |  will split on the newline character, breaking a string into its lines
        |""".stripMargin)
    .markdownSlide(
      """## Template literals
        |
        |Often, we want to compose some values inside a string. This can be done with *template literals*.
        |
        |```js
        |let day = "Wednesday"
        |let date = "15"
        |
        |console.log(`Today is ${day} the ${date}th.`)
        |```
        |
        |Hint: they are very useful if you want to print something for debugging purposes
        |""".stripMargin)
    .markdownSlide(
      s"""## Arrays
         |
         |Sometimes, we may wish to hold a list of data. e.g.:
         |
         |```js
         |let days = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ]
         |```
         |
         |This produces an `array` - a contiguous list of items held in memory.
         |
         |* To reference an item, use square brackets. e.g., `console.log(days[1])`
         |
         |* Note that they are *zero-indexed* - `days[0]` is `"Sunday"`; `days[1]` is `"Monday"`
         |
         |* This also means the last item is at `days.length - 1` is the last entry (`Saturday`)
         |""".stripMargin
    )
    .markdownSlide(
      """## Special for-loops for arrays
        |
        |So far we've seen *C-style* for-loops, and these can be used for arrays:
        |
        |```js
        |let days = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ]
        |for (let i = 0; i < days.length; i++) {
        |  console.log(days[i].toUpperCase())
        |}
        |```
        |
        |There is also a style of for loop `for ... of` that works on *iterable* objects, such as arrays:
        |
        |```js
        |let days = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ]
        |for (const day of days) {
        |  console.log(day.toUpperCase())
        |}
        |```
        |
        |The `const` keyword here is optional, but can remind you not to alter the cursor variable (because any changes
        |to it would be lost when it moves on to the next item)
        |""".stripMargin)
    .markdownSlide(
      """## Useful array methods
        |
        |* `days.slice(1, -1)` - `slice` is a convenient way to take part of an array as another array
        |
        |* `days.push("Octaday")` - `push` adds an element to the end of an array
        |
        |* `let o = days.pop()` - removes the last element from an array
        |
        |* `days.reverse()` - reverses an array
        |
        |There are plenty more on [MDN](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array)
        |
        |""".stripMargin)
    .markdownSlide(
      """## Two dimensional arrays
        |
        |It's fairly common to want to store data in two dimensions. e.g., the grid of tiles in a game like LavaMaze
        |
        |Arrays are only one-dimensional, but this can be done as an *array of arrays*
        |
        |```js
        |let grid = [
        |  [ "x", "#", "#", "#", "#" ],
        |  [ ".", "#", "#", "#", "#" ],
        |  [ ".", ".", ".", "h", "." ],
        |  [ "#", "#", "#", "#", "." ],
        |  [ "#", "#", "#", "#", "*" ],
        |]
        |console.log(grid[2][3]) // "h" - note, it's y first
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Objects
        |
        |* Arrays map numbers to values
        |
        |* Objects map strings to values
        |
        |```js
        |let myData = {
        |  name: "Algernon Moncrieff",
        |  status: "fictional",
        |  book: "The Importance of Being Earnest",
        |}
        |```
        |
        |* Values can be looked up by their key using `[]` notation:
        |
        |  ```js
        |  console.log(myData["name"])
        |  ```
        |
        |* ...or by dot notation
        |
        |  ```js
        |  console.log(myData.name)
        |  ```
        |""".stripMargin)
    .markdownSlide(
      """## Special for loops for objects
        |
        |We've seen *C-style* for loops and `for ... of` loops for arrays. Objects have a `for ... in` loop
        |
        |```js
        |let myData = {
        |  name: "Algernon Moncrieff",
        |  status: "fictional",
        |  book: "The Importance of Being Earnest",
        |}
        |
        |for (const key in myData) {
        |  console.log(`${property}: ${myData[property]}`)
        |}
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Beware JavaScript's weak typing
        |
        |We've talked about arrays and objects, but Arrays are also objects in JavaScript. This means we *could set non-numeric keys*...
        |
        |```js
        |let days = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ]
        |days["year"] = 365
        |```
        |
        |It's not common to use this feature. Usually you want to know if your item is an array or an object, but:
        |
        |* The string and number keys can be retrieved using a `for ... in` loop
        |
        |* Only the values with number keys would be iterated over using a `for ... of` loop
        |
        |* If you use a numeric string as a key when setting an item in a, e.g. `days["8"] = "Octaday"`, it'll get treated as a numeric key
        |""".stripMargin)
    .markdownSlide(
      """## Functions and objects
        |
        |In JavaScript, functions are also objects. Again, we can set key/value pairs on them
        |
        |```js
        |function twice(i) {
        |  return 2 * i
        |}
        |
        |twice["status"] = "being tested"
        |console.log(twice.status)
        |```
        |
        |*This means that if you use `[]` rather than `()` when calling a function, you're setting a property, not calling it!*
        |
        |Named functions have a pseudo-property called `name` that can be read but not set
        |
        |```js
        |console.log(twice.name)
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Event handlers
        |
        |Functions being objects means they can be passed and set as properties.
        |
        |Later in the course, we will meet HTML, but for the moment, let's demonstrate this by programmatically
        |inserting a button into a blank page and making it do something
        |
        |```js
        |let b = document.createElement("button")
        |b.innerText = "This is my button"
        |document.body.append(b)
        |
        |b.onclick = (evt) => {
        |  console.log("I was just clicked")
        |}
        |```
        |
        |When the button is clicked, the browser knows to call the button's `onclick` method. Because this is a property,
        |we can change what it points to, and point it to our function
        |""".stripMargin)
    .markdownSlide(
      """## Event handlers
        |
        |Different event handlers can handle different situations
        |
        |```js
        |let i = document.createElement("input")
        |document.body.append(i)
        |i.onchange = (evt) => {
        |    console.log("I was changed to: " + evt.target.value)
        |}
        |
        |i.oninput = (evt) => {
        |    console.log(evt.target.value.toUpperCase())
        |}
        |```
        |
        |When the button is clicked, the browser knows to call the button's `onclick` method. Because this is a property,
        |we can change what it points to, and point it to our function
        |""".stripMargin)
    .markdownSlide(
      """## A different way of defining a robot?
        |
        |So far, we've programmed robots as *sequential programs* - there is one block of code that is executed.
        |
        |As the course, goes on, however, we'll start to write programs that *react to events* and *change state*.
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")



  val deck = builder.renderNode



}
