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
           |""".stripMargin
      )
    ),
    Seq.empty
  )

}
