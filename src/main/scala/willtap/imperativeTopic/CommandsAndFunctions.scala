package willtap.imperativeTopic

import canvasland.{CanvasLand, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.{DiffNode, MutableArrayComponent}
import com.wbillingsley.veautiful.html.{<, SVG, VHtmlComponent, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder, ScatterPlot}
import lavamaze.Maze
import org.scalajs.dom
import org.scalajs.dom.{Element, Node, html, svg}
import willtap.Common

object CommandsAndFunctions {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Commands and Functions
        |
        |And a quick intro to JavaScript
        |
        |""".stripMargin).withClass("center middle")
    .veautifulSlide(<.div(
      Challenge.split(
        Common.markdown(
          """## "Computers are kind of stupid..."
            |
            |When humans talk to each other, we're sometimes a little vague about what we mean...
            |
            |Our brains do a large amount of *natural language processing* to extract the words and sentences, and our
            |brains use a lot of *intelligence* to understand *context*.
            |
            |Modern devices in the home (smart assistants and social robots) are starting to be able to do that using
            |very advanced artificial intelligence ... but programming languages can't.
            |
            |*Computers are kind of stupid... they only do **exactly** what you tell them*
            |
            |""".stripMargin)
      )(
        <.div(^.attr("style") := "text-align: center; font-style: italic;",
          <.p(
            <.img(^.src := "images/commandsandfunctions/kettle.png"),
            <("figcaption")("Polly put the kettle on")
          ),
          <.p(
            <.img(^.src := "images/commandsandfunctions/jumper.png"),
            <("figcaption")("Polly put a jumper on")
          )
        )
      )
    ))
    .markdownSlide(
      """## JavaScript...
        |
        |The language we're going to use in this course is *JavaScript* (also called *ECMAScript* or *ES*)
        |
        |Like many programming languages, it's an *imperative language* - that is, it's about giving instructions for a
        |machine to follow.
        |
        |We're going to start introducing this using a little robot game.
        |
        |First, we should meet our cast of characters...
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Welcome to the Lava Maze"),
      Challenge.split(
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("S"); m.start();  },
          Common.markdown("*Snobot*: our hero")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString(".S"); m.start();  },
          Common.markdown("*Floor*: passable to snobots and blob guards")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("#S"); m.start();  },
          Common.markdown("*Wall*: impassable to snobots and blob guards")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString(" S"); m.start();  },
          Common.markdown("*Lava*: deadly to snobots, passable to blob guards")
        ),
      )(
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("BS"); m.start();  },
          Common.markdown("*Blob Guard*: our villain")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("OS"); m.start();  },
          Common.markdown("*Boulder*: you can push them around. Beware of ones that roll on their own.")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("*S"); m.start(); },
          Common.markdown("*Diamond*: collectable but slippery")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("GS"); m.start(); },
          Common.markdown("*Goal*: where Snobot has to get to")
        )
      )
    ))
    .markdownSlide("""## Vocabulary and Syntax
                     |
                     |To write a program, we're going to need some vocabulary - things to say.
                     |
                     |* Some of these things are defined by the language: *reserved words*. e.g., `if`, `let`, and `while`
                     |
                     |* Some are defined by the *standard library*. e.g., `Math`
                     |
                     |* Some are defined by the environment we're running the program in. e.g., `console`
                     |
                     |* Some might be defined by other parts of our program. e.g., the Lava Maze game defines `right()`
                     |  to be the command that makes Snobot go right one square.
                     |
                     |We're also going to need to use *syntax* - that is, we're going to have to compose our expressions in a very
                     |precise way that the language interpreter can parse. If we make a mistake, and the interpreter can't
                     |parse our code, we might have a *syntax error*.
                     |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Calling our first function
          |
          |To get to the goal, we're going to need Snobot to go right twice...
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(5 -> 5, 5 -> 5) { m =>
        m.loadFromString(
          """
            |
            | S.G
            |
            |
            |""".stripMargin)
      })()
    ))
    .markdownSlide(
      """## Calling our first function
        |
        |The program we should have ended up with is:
        |
        |```js
        |right();
        |right();
        |```
        |
        |Things to note from this
        |
        |* To call a function in JavaScript, you put the name of the function, followed by any arguments it takes in
        |parentheses. If it doesn't take any arguments, you end up with `right()`.
        |
        |* Statements in JavaScript end with a semi-colon. But if you miss it out, JavaScript will *try* to infer where
        |you meant to put it.
        |
        |* The two statements are executed one after another. This is because JavaScript is an *imperative* language
        |("imperative" means command). A lot of languages are like this, but there are some that aren't.
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Making decisions
          |
          |Hit play and reset a few times. This maze changes randomly!
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(5 -> 5, 5 -> 5) { m =>
        if (Math.random() < 0.5) {
          m.loadFromString(
            """
              |
              | S.G
              |""".stripMargin)
        } else {
          m.loadFromString(
            """
              |
              | S.
              |  G
              |""".stripMargin)
        }
      })()
    ))
    .markdownSlide(
      s"""## Making decisions
        |
        |A simple solution might be
        |
        |```js
        |right();
        |if (canGoRight()) {
        |  right();
        |} else {
        |  down();
        |}
        |```
        |
        |There's a few things going on here...
        |
        |* *Blocks* of code in JavaScript are surrounded by `{ }`. It's a *curly-brace* language.  \t
        |  (Some others, e.g. Python, use indentation instead)
        |
        |* `canGoRight()` returns a *Boolean* result: `true` or `false`.
        |
        |* The syntax of an `if` statement. Don't forget to put the condition you're checking inside `()`.
        |
        |* It doesn't have to have an `else` clause, but it can.
        |""".stripMargin)
    .markdownSlide(
      """## One to beware of...
        |
        |You might be tempted to write your `if` statements without curly-braces:
        |
        |```js
        |if (canGoRight())
        |  right()
        |else
        |  down()
        |```
        |
        |...but beware, because this might not do what you expect:
        |
        |```js
        |if (canGoRight())
        |  right();
        |else
        |  println("Can't go right");
        |  down();
        |```
        |
        |Your eyes can easily parse the code differently than the interpreter does. Programmers often decide on
        |*code conventions* (e.g. "always write `if`s with curly braces) to avoid this.
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Loops
          |
          |Our `right()` and `down()` commands only move one square. How can we go right *while we can*?
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
          m.loadFromString(
            """
              | S......
              |       .
              |       .
              |       .
              |       .
              |       G
              |""".stripMargin)
      })()
    ))
    .markdownSlide(
      """## While loops
        |
        |Something like this might work:
        |
        |```js
        |while (canGoRight()) {
        |  right();
        |}
        |while (canGoDown()) {
        |  down();
        |}
        |```
        |
        |A `while` loop performs a check at the start of each loop. If the check is `true`, the body executes.
        |If it `false`, it doesn't.
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## While true...
          |
          |Previously, we assumed the maze always goes right first -- we always went right *then* down. This maze flips
          |between the two randomly.
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
        if (Math.random() < 0.5) {
          m.loadFromString(
            """
              |
              | S.....
              |      .
              |      .
              |      .
              |      G
              |""".stripMargin)
        } else {
          m.loadFromString(
            """
              |
              | S
              | .
              | .
              | .
              | .....G
              |""".stripMargin)
        }
      })()
    ))
    .markdownSlide(
      """## Forever...
        |
        |We might have ended up with something like
        |
        |```js
        |while (true) {
        |  if (canGoRight()) {
        |    right()
        |  } else if (canGoDown()) {
        |    down()
        |  }
        |}
        |```
        |
        |Here, `while (true)` effectively means "do this forever".
        |
        |Notice the console doesn't print out "Completed"... the program hasn't stopped (it's just that a teleported
        |Snobot can't go anywhere). This is an *infinite loop*.
        |
        |**Beware**: You can hang your browser with an infinite loop...
        |""".stripMargin
    )
    .markdownSlide(
      """## A dead end ...
        |
        |Let's go back to this code for a moment, just to show how some other loops behave
        |
        |```js
        |while (canGoRight()) {
        |  right();
        |}
        |while (canGoDown()) {
        |  down();
        |}
        |```
        |
        |""".stripMargin
    )
    .veautifulSlide(<.div(
      Common.markdown(
        """## A dead end ...
          |
          |Going right and down should stick us in the dead end. But what if we change it to a `do ... while` loop?
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
        m.loadFromString(
          """
            | S......
            |      .
            |      .
            |      .
            |      .
            |      G
            |""".stripMargin)
      })()
    ))
    .markdownSlide(
      """## Do ... while
        |
        |A `do ... while ...` loop looks like this:
        |
        |```js
        |do {
        |  right();
        |} while (canGoRight())
        |do {
        |  down();
        |} while (canGoDown())
        |```
        |
        |In this case, the check is made at the *end* of the block. This means the block will always execute
        |*at least once*.
        |
        |""".stripMargin
    )
    .veautifulSlide(<.div(
      Common.markdown(
        """## Looping a set number of times
          |
          |How can we say "go right five times, then go down five times"?
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
        m.loadFromString(
          """
            | S......
            |      .
            |      .
            |      .
            |      .
            |      G
            |""".stripMargin)
      })()
    ))
    .markdownSlide(
      s"""## Looping a set number of times
        |
        |We probably ended up with something like:
        |
        |```js
        |let i = 0;
        |while (i < 5) {
        |  right();
        |  i = i + 1;
        |}
        |```
        |
        |There's quite a lot going on here.
        |
        |* `let` declares a *local variable* . It is *in scope* for the rest of the block of code it's in. Because it is
        |a variable, its value can be updated. (Whereas a `const` can't be.)
        |
        |* Programmers often start counting from `0` rather than from `1`.
        |
        |* `=` in JavaScript is *assignment*, not equality. It updates the variable's value.  \t
        |  (C, C++, and Java also use `=` for assignment. But some languages don't. For example, *R* uses `<-`)
        |
        |* `i += 1;` would also work - shorthand for `i = i + 1`.
        |
        |* `i++;` would also work - it means "increment `i`".
        |""".stripMargin
    )
    .markdownSlide(
      """## C-style `for` loops
        |
        |This is a common programming pattern, but it's easy to make a mistake (e.g. forgetting to increment `i`)
        |
        |```js
        |let i = 0;
        |while (i < 5) {
        |  right();
        |  i = i + 1;
        |}
        |```
        |
        |So there is another notation that means the same thing, but gathers the loop's *initialisation*, *condition*,
        |and what to do at the end of each loop into one line:
        |
        |```js
        |for (let i = 0; i < 5; i++) {
        |  right();
        |}
        |```
        |
        |They're called *"C-Style"* for loops because they were introduced in the programming language *C*.
        |""".stripMargin
    )
    .veautifulSlide(<.div(
      <.h2("A difference in scope"),
      Challenge.split(
        Common.markdown(
          """For a moment, let's not ask Snobot to do anything, and just print output to our game console
            |
            |```js
            |let i = 0;
            |while (i < 5) {
            |  println(i);
            |  i = i + 1;
            |}
            |println("Exited the loop")
            |println(i);
            |```
            |
            |Notice that after exiting the loop, `i` is `5`.
            |
            |What happens if we try that with a C-style for-loop?
            |
            |```js
            |for (let i = 0; i < 5; i++) {
            |  println(i);
            |}
            |println("Exited the loop")
            |println(i)
            |```
            |
            |You should find that `i` is *out of scope* -- it is only defined within the for-loop.
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })()
      )
    ))
    .markdownSlide(
      """## Names for variables, names for actions?
        |
        |When we were considering our loop, we declared a variable `i`. While it was in scope, it was something we
        |could refer to. We'd added a *name* to our vocabulary.
        |
        |What if we could name particular actions, such as going right five times?
        |
        |To do this, we can declare a *function*...
        |
        |```js
        |function rightFive() {
        |  for (let i = 0; i < 5; i++) {
        |    right();
        |  }
        |}
        |
        |rightFive();
        |```
        |""".stripMargin
    )
    .veautifulSlide(<.div(
      Common.markdown(
        """## A function for going right...
          |
          |Write functions for "go right *n* times" and "go down *n* times"?
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
        m.loadFromString(
          """
            | S......
            |      .
            |      .
            |      .
            |      .
            |      G
            |""".stripMargin)
      })()
    ))
    .markdownSlide(
      """## Functions with arguments
        |
        |Our function with an argument might have looked like this:
        |
        |```js
        |function goRight(n) {
        |  for (let i = 0; i < n; i++) {
        |    right();
        |  }
        |}
        |```
        |
        |Or, if we wanted to use `let` and declare it like a variable (using an *arrow function*), it might have looked
        |like;
        |
        |```js
        |let goRight = (n) => {
        |  for (let i = 0; i < n; i++) {
        |    right();
        |  }
        |}
        |```
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Pass by value"),
      Challenge.split(
        Common.markdown(
          """In JavaScript, function arguments are passed *by value*.
            |
            |For example
            |
            |```js
            |let age = 21;
            |let getOlder = function(ageInFunc) {
            |  ageInFunc = ageInFunc * 2;
            |  println("Now you are " + ageInFunc);
            |}
            |
            |getOlder(age);
            |println("But outside the function you are " + age);
            |```
            |
            |Changing the value of `age` inside the function didn't affect the variable *outside* the function.
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })()
      )
    ))
    .veautifulSlide(<.div(
      <.h2("Scopes and Shadowing"),
      Challenge.split(
        Common.markdown(
          """If you redefine a name with `let` in the same scope, you'll get an error. But if you make a new scope
            |(a function or a block), you can create a variable with the same name.
            |
            |It is a different variable, and the new declaration has *shadowed* (as in obscured, not as in copied)
            |the previous variable. So, our outer code
            |
            |```js
            |let age = 21;
            |let getOlder = function(age) {
            |  age = age * 2;
            |  println("Now you are " + age);
            |}
            |
            |getOlder(age);
            |println("But outside the function you are " + age);
            |```
            |
            |For example, our function can use the argument name `age` and it doesn't matter that the outermost code
            |also calls its variable `age`. They are separate variables.
            |
            |""".stripMargin
        )
      )(
        JSCodable(Maze()((0,0), (0,0)) { _ => })()
      )
    ))
    .veautifulSlide(<.div(
      Common.markdown(
        """## Return values
          |
          |When we called `canGoRight()` it returned a value: `true` or `false`. How can we write a function that
          |will tell us if we could do both? And when we called `right()` what did it return?
          |""".stripMargin
      ),
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
      s"""## Returning data
        |
        |To return a value from a function, we use the `return` keyword. For example
        |
        |```js
        |function canGoBoth() {
        |  return canGoRight() && canGoLeft();
        |}
        |```
        |
        |If we don't return anything, a call to the function will return the value `undefined`. This is a special value
        |that we'll see again in a moment.
        |
        |It's probably also worth remembering some of the comparison and logic operators. e.g.
        |
        |* `a <= b`  \t
        |   is `a` less than or equal to `b`
        |
        |* `(a <= b) && (b == c)`  \t
        |   is `a` less than or equal to `b` *and* `b` equal to `c`
        |
        |* `canGoRight() || canGoLeft()`  \t
        |   can go right or can go left. Note: *inclusive* or. `true || true` is `true`
        |
        |* `canGoRight() != canGoLeft()`  \t
        |   the result of `canGoRight()` is not equal to `canGoLeft()`.
        |
        |For a full list of operators, [see the MDN web docs](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators)
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Arguments are optional
          |
          |Let's define a function `goDownAndRight` that takes two arguments: how far to go down, and how far to go right.
          |What happens if we only pass one value?
          |""".stripMargin
      ),
      JSCodable(Maze("1.goright")(10 -> 10, 10 -> 10) { m =>
        m.loadFromString(
          """
            | S.....
            | .    .
            | .    .
            | .    .
            | .    .
            | .....G
            |""".stripMargin)
      })()
    ))
    .markdownSlide(
      """## Arguments are optional
        |
        |```js
        |function goDownAndRight(d, r) {
        |  if (d != undefined) {
        |    for (let i = 0; i < d; i++) {
        |      down();
        |    }
        |  }
        |
        |  if (r != undefined) {
        |    for (let i = 0; i < r; i++) {
        |      right();
        |    }
        |  }
        |}
        |```
        |
        |If we only call this with one argument, `goDownAndRight(5)`, Snobot will go down 5 squares. We gave the first
        |argument, `d`, the value `5`, but because we didn't pass a second argument, it is left with the value `undefined`.
        |""".stripMargin)
    .markdownSlide(
      """## The Lava Maze Will Return...
        |
        |The Lava Maze (with a few more inhabitants) is going to return a few times: next week's tutorial, the first assignment, and a later tutorial
        |where we want more than one thing happening at once.
        |
        |For the moment, though, this has hopefully illustrated a few pieces of JavaScript syntax.
        |
        |Before we go, let's introduce the environment you'll use in the first tutorial... as we meet a turtle and start
        |doodling like we're ten...
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Turtle graphics"),
      Common.markdown(
        """The "turtle" in the middle of the screen can be asked to turn `left` and `right`, or to move `forward` and `back`. It has a pen
          |that can change colour, and if the pen is down it'll leave ink behind as it goes...
          |""".stripMargin),
      JSCodable(CanvasLand()(
        r = Turtle(320, 320),
        setup = (c) => {
          c.drawGrid("lightgray", 25)
        }
      ))()
    ))
    .markdownSlide(Common.willCcBy).withClass("bottom")
  val deck = builder.renderNode

}
