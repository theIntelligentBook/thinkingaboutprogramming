package willtap.nested

import coderunner.JSCodable
import com.wbillingsley.veautiful.html.<
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import lavamaze.Maze
import willtap.Common
import willtap.imperativeTopic.ImpossibleThings.builder

object NestedStructures {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Nested Structures
        |
        |2D arrays and trees
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## Nested structures
        |
        |When we introduce data types, we tend to introduce them in a small way. For instance, we might declare an
        |array of strings
        |
        |```js
        |let days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
        |```
        |
        |or we might declare an object with some mappings:
        |
        |```js
        |let daysInMonth = {
        |  "January": 31, "February": 28, "March": 31, "April": 30,
        |  "May": 31, "June":30, "July":31, "August": 31,
        |  "September": 30, "October": 31, "November": 30, "December": 31
        |}
        |```
        |
        |However, our entries in objects and arrays can *also* be objects and arrays.
        |""".stripMargin
    )
    .markdownSlide(
      """## 2 Dimensional arrays
        |
        |2D arrays are often stored as an "array of arrays". Many of the games we use in this course are like this.
        |
        |I tend to store these as an array of rows - largely so you can lay them out easily in sourcecode. Here's
        |noughts and crosses:
        |
        |```js
        |let grid = [
        |  ["O", "X", "O"],
        |  [" ", "X", " "],
        |  [" ", " ", "X"]
        |]
        |```
        |
        |`O`'s turn. I recommend playing at (1, 2). But as we've laid this out as an array of rows that'd be
        |
        |```js
        |grid[2][1] = "O"
        |```
        |""".stripMargin
    )
    .markdownSlide(
      """## Live coding ...
        |
        |These are in separate videos, but I'll show you live-coding:
        |
        |* Conway's Game of Life
        |* Minesweeper
        |
        |The next few slides have some tips and tricks from the live code
        |
        |""".stripMargin
    )
    .markdownSlide(
      """## Destructuring assigment
        |
        |Recent versions of JavaScript include "destructuring assignment".  \t
        |If we have an array, and we want to set two variables to the first two elements, we can do this in one go:
        |
        |```js
        |let arr = [1, 2]
        |let [a, b] = arr
        |```
        |
        |`a` becomes `1` and `b` becomes `2`
        |""".stripMargin)
    .markdownSlide(
      s"""## Destructuring assigment inside a `for ... of`
        |
        |Sometimes, we want to get the coordinates of all the *neighbours* of a particular square.  \t
        |For instance, all the letters surrounding the `*` at `y=2`, `x=1`
        |
        |```
        |....
        |abc.
        |d*e.
        |fgh.
        |```
        |
        |One quick way to do this is using *destructuring assignment* in a `for ... of` loop
        |
        |```js
        |for (let [xx, yy] of [
        |  [+x-1, +y-1], [+x, +y-1], [+x+1, +y+1],
        |  [+x-1, +y  ],             [+x+1, +y  ],
        |  [+x-1, +y+1], [+x, +y+1], [+x+1, +y+1]
        |]) {
        |  console.log("" + xx + " " + yy)
        |}
        |```
        |
        |Note that we said `+x+1`. This helps solve the issue if it turns out `x` was `"0"` instead of `0`.  \t
        |
        |`"0" + 1` is `"01"` but `+"0" + 1` causes the `0` to be converted to a number first, and we get `1`
        |""".stripMargin)
    .markdownSlide(
      """## Closing over values
        |
        |*Closures* are a fairly complex topic. A quick way of explaining it, though, is that inside a loop we can
        |create an event handler that "closes over" the loop variable. Even after the loop has finished, it'll still
        |remember the variable.
        |
        |```js
        |let app = document.getElementById("app")
        |for (let y = 0; y < 10; y++) {
        |  let button = document.createElement("button")
        |  button.innerText = `$${y}`
        |  button.onclick = (evt) => {
        |    console.log(`This event handler remembered ${y}`)
        |  }
        |  app.append(button)
        |}
        |```
        |
        |**Note**: This only works if you're declaring the loop variable inside the `for` (so that it's a different
        |variable each time through the loop).
        |""".stripMargin)
    .markdownSlide(
      """## Getting the indexes of an array with `for ... in`
        |
        |We've seen a lot of code like this:
        |
        |```js
        |for (let y = 0; y < h; y++) {
        |  for (let x = 0; x < w; x++) {
        |    // do something
        |  }
        |}
        |```
        |
        |In one video, I spend a few minutes debugging because I accidentally wrote `y++` instead of `x++`
        |
        |If we were to do this:
        |
        |```js
        |for (let y in grid) {
        |  for (let x in grid[+y]) {
        |    // do something with +x and +y
        |  }
        |}
        |```
        |
        |We could get the keys automatically. Note, though, they come out as **strings**, so we need to use `+x`
        |and `+y` to force them to become numbers. And you have to remember your `in` from your `of`.
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Abstract Syntax Trees
          |
          |Another common data structure is a *tree*. In this, we have objects whose child elements are objects.
          |
          |The example I'm going to use is *your program*. This is most obvious if we use my blocks-programming
          |language, where we can see that tiles get nested inside each other...
          |
          |""".stripMargin),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
        m.loadFromString(
          """
            | S......
            | .    .
            | .    .
            | .    .
            | .    .
            | ...  G
            |""".stripMargin)
      })()
    ))
    .markdownSlide(
      """## Abstract Syntax Trees
        |
        |However even if you write your code in text, the *parser* for the programming environment you use will
        |parse it into a tree-like data structure.
        |Let's show you that on [astexplorer.net](https://astexplorer.net/)
        |
        |
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")

  val deck = builder.renderNode

}
