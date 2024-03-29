package willtap

import com.wbillingsley.veautiful.html.{<, ^, unique}
import willtap.templates._
import site._

object Intro {

  val frontPage = <.div(
    <.div(^.attr("style") := "position: relative; top: 0;",
      <.img(^.src := "images/tap720.jpg", ^.alt := "Thinking About Programming"),
      <.div(^.attr("style") := "position: absolute; bottom: 0px; right: 0px; background: rgba(255, 255, 255, 0.7);",
        Common.markdown("[Will Billingsley](https://www.wbillingsley.com)'s computational thinking course")
      )
    ),
    <.div(
      <.div(^.cls := "lead",
        Common.markdown(
          """
            | What are programs? Why are they useful? Why do people keep talking about "computational thinking".
            |
            | This site works a little like an open source textbook. It's an introduction to programming for adults
            | and children, that talks as much about "how to think about programming" as about syntax. I use JavaScript
            | as the language, so you can try programming things right in the site itself, but there's also a blocks
            | programming environment, robot mazes, and various other things to try to help you along.
            |""".stripMargin
        )
      ),
      Common.markdown(
        s"""## 0. Intro
           |
           |* [Video intro](${router.path(VideoRoute("intro"))})  \t
           |  ...in which we hear what this course is about
           |
           |## 1. Commands and Functions
           |
           |* [Making Impossible Things](${router.path(DeckRoute("impossibleThings", 0))})  \t
           |  ...in which we start to motivate the idea of computational thinking
           |
           |* [Commands and Functions](${router.path(DeckRoute("commandsAndFunctions", 0))})  \t
           |  ...in which we meet Snobot and learn a little JavaScript
           |
           |* [Tutorial: Turtle Graphics](${router.path(ListPathRoute("challenges", "turtleGraphics", Nil))})  \t
           |  ...in which we meet a turtle and draw some shapes
           |
           |## 2 & 3. Types
           |
           |* [Types](${router.path(DeckRoute("types", 0))})  \t
           |  ...in which we consider what operations mean when applied to different things
           |
           |* [Tutorial: Welcome to the Lava Maze](${router.path(ListPathRoute("challenges", "lavaMaze", Nil))})  \t
           |  ...in which we get Snobot through some simple mazes and complete assignment 1.
           |
           |* [Reference Types](${router.path(DeckRoute("objectTypes", 0))})  \t
           |  ...in which we consider whether a copy of you would be you
           |
           |## 4 & 5. Robots and Control
           |
           |* [Open and Closed Loop Control](${router.path(ListPathRoute("challenges", "closedLoop", Nil))})  \t
           |  ...in which we start to move from dead reckoning to using feedback
           |
           |* [Handling state and state variables](${router.path(DeckRoute("states", 0))})  \t
           |  ...in which we land on the Moon
           |
           |* [Tutorial: Rescue Line](${router.path(ListPathRoute("challenges", "rescueLine", Nil))})  \t
           |  ...in which we get LineTurtle through some simple mazes and complete assignment 2.
           |
           |* [Sensors and Motors](${router.path(DeckRoute("sensorsAndMotors", 0))})  \t
           |  ...in which we take a little look at what small devices have available
           |
           |## 6. Little Data
           |
           |* [Passing Functions as Values](${router.path(DeckRoute("higherOrder", 0))})  \t
           |   ...in which we study Peter Rabbit
           |
           |## 7. Markup Langauges
           |
           |* [Markup Languages](${router.path(DeckRoute("markupLanguages", 0))})  \t
           |  ...in which we consider
           |
           |* [Tutorial: MicroRat](${router.path(ListPathRoute("challenges", "microRat", Nil))})  \t
           |  ...in which we get a ball-like robot through a physical maze and complete assignment 3.
           |
           |# 8. Nested Structures
           |
           |* [Nested Structures](${router.path(DeckRoute("nestedStructures", 0))})  \t
           |  ...in which we use 2D arrays and discover Abstract Syntax Trees.
           |
           |
           |# 9. Asynchronous Programming
           |
           |* [Flows of Control](${router.path(DeckRoute("flowsOfControl", 0))})  \t
           |  ...in which we learn to do more than one thing at once, and how to handle errors
           |
           |
           |# 10. Debugging
           |
           |* [Debugging](${router.path(DeckRoute("debugging", 0))})  \t
           |  ...in which we learn to find the cause of our errors
           |""".stripMargin
      )
    ),
    Seq.empty
  )

}
