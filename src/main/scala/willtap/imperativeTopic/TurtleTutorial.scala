package willtap.imperativeTopic

import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.Level
import willtap.templates.MarkdownStage
import willtap.{ChallengeRoute, IntroRoute, Router}

object TurtleTutorial {

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("turtleGraphics", l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("turtleGraphics", 0, 0)), s"Start")
    }
  }


  val levels = Seq(
    Level(name = "Introduction", stages = Seq(
      MarkdownStage(() =>
        s"""
          |# Turtle Graphics
          |
          |On the next page, we're going to meet our turtle graphics environment.
          |
          |First, I'd better give you a quick rundown of what the *functions* you can call on the environment do.
          |
          |* `left(a)` turns the turtle left (anticlockwise) by *a* degrees.  \t
          |  `anticlockwise(a)` turns the turtle anticlockwise by *r* radians, if you prefer radians
          |
          |* `right(a)` turns the turtle right (clockwise) by *a* degrees.  \t
          |  `clockwise(a)` turns the turtle clockwise by *r* radians, if you prefer radians
          |
          |* `forward(d)` asks the turtle to move forward *d* pixels.  \t
          |  `back(d)` asks the turtle to move backward *d* pixels.  \t
          |
          |* `setColour(s)` sets the pen's colour. It takes a <a target="_blank" href="https://developer.mozilla.org/en-US/docs/Web/CSS/color_value">CSS colour</a>.<br />
          |   e.g. `setColour("red")` or `setColour("#ff0000")` or `setColour("rgb(255, 0, 0)")`. You can even use pens with transparency, if you use
          |   `setColour("rgba(128, 64, 64, 0.5)")` - the last number in `rgba` is the "alpha" value, or how opaque you would like the pen to be.
          |   In any case, don't forget that the content of `setColour` is a string. i.e., it is always in quotes `"like this"`.
          |
          |* `penUp()` lifts the pen off the page, and `penDown()` puts it back down again. The pen will remember its colour while it is up.
          |
          |* `setThickness(r)` sets the radius of the pen's stroke.
          |
          |You can come back to this reference at any time, or you'll find the names of these functions under the "game" section in the tile tray.
          |
          |We can't auto-mark these exercises - you're going to have to see visually if it drew what you expected (and if not, think about why not). Don't forget, you
          |can insert `println("text")` statements in many places, to debug where your program is up to.
          |""".stripMargin)
    ))
  )


  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(ChallengeRoute("turtleGraphics", i, 0)),
    stagePath = (_, i, j) => Router.path(ChallengeRoute("turtleGraphics", i, j)),
    homeIcon = <.span(),
    scaleToWindow = true
  )

  val hilbertCode =
    """let maxDepth = 6
      |
      |let a = (d) => {
      |    if (d < maxDepth) {
      |      left(90)
      |      b(d + 1)
      |      forward(10)
      |      right(90)
      |      a(d + 1)
      |      forward(10)
      |      a(d + 1)
      |      right(90)
      |      forward(10)
      |      b(d + 1)
      |      left(90)
      |    }
      |}
      |
      |let b = (d) => {
      |    if (d < maxDepth) {
      |        right(90)
      |        a(d + 1)
      |        forward(10)
      |        left(90)
      |        b(d + 1)
      |        forward(10)
      |        b(d + 1)
      |        left(90)
      |        forward(10)
      |        a(d + 1)
      |        right(90)
      |    }
      |}
      |
      |a(1)
      |""".stripMargin


}
