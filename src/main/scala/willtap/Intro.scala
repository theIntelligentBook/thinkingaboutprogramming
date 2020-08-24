package willtap

import com.wbillingsley.veautiful.html.{<, ^}
import willtap.templates.FrontPage

object Intro {

  val frontPage = new FrontPage(
    <.div(
      <.img(^.src := "images/tap720.jpg", ^.alt := "Thinking About Programming"),
      <.div(^.cls := "abs-bottom-right white-translucent-bg",
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
        s"""
           |## 1. Commands and Functions
           |
           |* [Making Impossible Things](${DeckRoute("impossibleThings", 0).path})  \t
           |  ...in which we start to motivate the idea of computational thinking
           |
           |* [Commands and Functions](${DeckRoute("commandsAndFunctions", 0).path})  \t
           |  ...in which we meet Snobot and learn a little JavaScript
           |
           |* [Tutorial: Turtle Graphics](${ChallengeRoute("turtleGraphics", 0, 0).path})  \t
           |  ...in which we meet a turtle and draw some shapes
           |
           |## 2 & 3. Types
           |
           |* [Types](${DeckRoute("types", 0).path})  \t
           |  ...in which we consider what operations mean when applied to different things
           |
           |* [Tutorial: Welcome to the Lava Maze](${ChallengeRoute("lavaMaze", 0, 0).path})  \t
           |  ...in which we get Snobot through some simple mazes and complete assignment 1.
           |
           |* [Reference Types](${DeckRoute("objectTypes", 0).path})  \t
           |  ...in which we consider whether a copy of you would be you
           |
           |## 4 & 5. Robots and Control
           |
           |* [Open and Closed Loop Control](${DeckRoute("closedLoop", 0).path})  \t
           |  ...in which we start to move from dead reckoning to using feedback
           |
           |* [Tutorial: Rescue Line](${ChallengeRoute("rescueLine", 0, 0).path})  \t
           |  ...in which we get LineTurtle through some simple mazes and complete assignment 2.
           |
           |* [Sensors and Motors](${DeckRoute("sensorsAndMotors", 0).path})  \t
           |  ...in which we take a little look at what small devices have available
           |
           |## 6. Little Data
           |
           |* [Passing Functions as Values](${DeckRoute("higherOrder", 0).path})  \t
           |   ...in which we study Peter Rabbit
           |
           |## 7. Markup Langauges
           |
           |* [Markup Languages](${DeckRoute("markupLanguages", 0).path})  \t
           |  ...in which we consider
           |
           |* [Tutorial: MicroRat](${ChallengeRoute("microRat", 0, 0).path})  \t
           |  ...in which we get a ball-like robot through a physical maze and complete assignment 3.
           |""".stripMargin
      )
    ),
    Seq.empty
  )

}
