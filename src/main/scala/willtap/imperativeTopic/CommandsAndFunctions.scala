package willtap.imperativeTopic

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
    .markdownSlide(
      """## Still being written...
        |
        |Still writing the slides!
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")

  val deck = builder.renderNode

}
