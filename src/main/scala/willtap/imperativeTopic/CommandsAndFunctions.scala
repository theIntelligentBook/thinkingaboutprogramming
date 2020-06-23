package willtap.imperativeTopic

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
          Maze()((1,1), (1,1)) { m => m.loadFromString("S") },
          Common.markdown("*Snobot*: our hero")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString(".S") },
          Common.markdown("*Floor*: passable to snobots and blob guards")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString("#S") },
          Common.markdown("*Wall*: impassable to snobots and blob guards")
        ),
        <.p(
          Maze()((1,1), (1,1)) { m => m.loadFromString(" S") },
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
    .markdownSlide(
      """## Still being written...
        |
        |Still writing the slides!
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")

  val deck = builder.renderNode

}
