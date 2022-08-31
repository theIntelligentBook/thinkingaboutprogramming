package willtap.control

import canvasland.{CanvasLand, LineTurtle, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import willtap.{Common, given}

import scala.util.Random

object OpenAndClosedLoop {

  val scilly = <.img(^.src := "images/control/scilly isles.jpg").create()

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Open and Closed Loop Control
        |
        |Avoiding the rocks and following lines
        |
        |""".stripMargin).withClass("center middle")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/ship.jpg"), <("figcaption")("Imagine you're a ship at sea...")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/ship measurements.jpg"), <("figcaption")("Measuring where you are")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/dead reckoning.jpg"), <("figcaption")("Dead reckoning")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/dead reckoning error.jpg"), <("figcaption")("Dead reckoning error")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/scilly isles.jpg"), <("figcaption")("Entering the English Channel")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(<.div(
      <.h2("Sailing into the Channel"),
      Common.markdown(
        """We should be able to sail into the Channel with
          |
          |```js
          |left(30); forward(600); right(20); forward(300);
          |```
          |
          |But what if storms, winds, or currents means our first leg was a little bit wrong?
          |""".stripMargin),
      JSCodable(CanvasLand()(
        viewSize = 920 -> 640,
        fieldSize = 1280 -> 720,
        r = Turtle(50, 600),
        setup = (c) => {
          c.drawImage(scilly, 0, 0,1280, 720, 0,0,1280, 720)
        }
      ))(tilesMode = false)
    ))
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "https://upload.wikimedia.org/wikipedia/commons/d/d5/HMS_Association_%281697%29.jpg"), <("figcaption")("Engraving of the Scilly Naval Disaster 1707")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## Open Loop control
        |
        |Many control systems just follow a fixed set of instructions:
        |
        |* Old dishwashers run fixed cycles of how long they rinse for
        |
        |* Old clothes driers run for a set time (with a set cooldown time at the end) regardless of whether your clothes
        |  are dry
        |
        |* Our turtle graphics turtle has no idea where it is
        |
        |* A robot like sphero can be pre-programmed to follow a set path
        |
        |These are simple, easy to implement, stable, but not always robust
        |
        |* Dirty dishes might not come out clean, or it might waste water washing clean dishes too long
        |
        |* Your clothes might not be dry at the end of the cycle
        |
        |* A ship sailing by dead reckoning might not be where it thinks it is
        |
        |* Sphero might not follow exactly the same path
        |
        |Our turtle graphics turtle is fine, because our programs run deterministically, but the real world is less
        |reliable.
        |""".stripMargin)
    .markdownSlide(
      """## Closed Loop control
        |
        |At some point, we often want to "close the loop" and *sense* or *measure* whether we are in the state we
        |think we are.
        |
        |* Ships try to measure their nautical position
        |
        |* Clothes driers might sense the moisture in the air
        |
        |* Dishwashers might sense how dirty the water they spray is
        |
        |Sometimes you can take good measurements and sometimes you can't. You have to work with the information you
        |can get. For instance, ships would sail a while by dead reckoning and periodically take soundings of
        |the seabed or make other checks to try to verify their position.
        |
        |The *Longitude Prize* sought to develop maritime clocks that could work at sea, so sailors could calculate
        |longitude not just latitude from the positon of the Sun.
        |""".stripMargin)
    .markdownSlide(
      """## Robots and lines
        |
        |A common task for computational thinking outreaches is to get a robot to navigate its way through a maze...
        |
        |""".stripMargin)
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/outreachevents/coderchallenge2018.jpg"), <("figcaption")("UNE Cisco Coders Challenge")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/outreachevents/coderchallenge2018b.jpg"), <("figcaption")("UNE Cisco Coders Challenge")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/outreachevents/coderchallenge2019t.jpg"), <("figcaption")("UNE Cisco Coders Challenge")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/outreachevents/coderchallenge2019a.jpg"), <("figcaption")("UNE Cisco Coders Challenge")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/outreachevents/scienceexperience2019.jpg"),
        <("figcaption")("Science Experience.")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## Navigating a maze
        |
        |Typically, these challenges only have two kinds of sensor to play with:
        |
        |* A "line sensor" that shines a light and sees if it reflects back, to detect the colour of the paper
        |
        |* A "range sensor" that can detect if an object is in front of the robot
        |
        |The first challenge is just to get the robot to follow a line using its line sensor.
        |
        |How you do that *depends on the layout of the robot's sensors and the lines*
        |
        |""".stripMargin)
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/edison ipad.jpg"),
        <.img(^.src := "images/control/edgefollowing.png"),
        <("figcaption")("Edison. One line sensor. Follow the edge.")
      )
    ).withClass("image-slide pp contain")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/bitbot ipad.jpg"),
        <.img(^.src := "images/control/aroundtheline.png"),
        <("figcaption")("Bit:Bot. Two widely spaced line sensors. Surround the line.")
      )
    ).withClass("image-slide pp contain")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/makebot.jpg"),
        <.img(^.src := "images/control/ontheline.png"),
        <("figcaption")("MakeBot. Two narrowly spaced line sensors. On the line.")
      )
    ).withClass("image-slide pp contain")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/control/bitbot.jpg"),
        <.img(^.src := "images/control/makebot grabber.jpg"),
        <("figcaption")("Sometimes the line looks black to you, but not to the robot.")
      )
    ).withClass("image-slide pp cover")
    .veautifulSlide(<.div(
      <.h2("Introducing... LineTurtle"),
      Common.markdown(
        """LineTurtle is a version of our graphics turtle that lets us add line sensors to it, so we can sense the
          |colour of the canvas underneath us. Each sensor returns a number between 0 and 1, but we can also set what
          |colours it's sensitive to.
          |
          |Let's implement our line followers...
          |""".stripMargin),
      JSCodable(CanvasLand()(
        fieldSize=(920 -> 640),
        viewSize=(920 -> 640),
        r = LineTurtle(120, 100) { r =>  },
        setup = c => {
          c.fillCanvas("white")
          c.drawGrid("rgb(200,240,240)", 25, 1)
          c.withCanvasContext { ctx =>
            ctx.strokeStyle = "rgb(60,60,60)"
            ctx.lineWidth = 25
            ctx.beginPath()
            ctx.moveTo(100, 100)
            ctx.lineTo(770, 100)
            ctx.lineTo(770, 540)
            ctx.bezierCurveTo(670, 540, 150, 200, 150, 100)
            ctx.stroke()
          }
        }
      ))(tilesMode = false)
    ))
    .veautifulSlide(<.div(
      <.h2("Stability"),
      Common.markdown(
        """A closed loop control system can become unstable. A quick demonstration of this is what happens if we
          |*move the line sensor behind the robot*. Fortunately, having the line sensor *in front* of the robot gives
          |us some "damping" in our control system (the oscillations get smaller).
          |""".stripMargin),
      JSCodable(CanvasLand()(
        fieldSize=(920 -> 640),
        viewSize=(920 -> 640),
        r = LineTurtle(120, 100) { r => },
        setup = c => {
          c.fillCanvas("rgb(200,180,0)")
          c.drawGrid("rgb(200,240,240)", 25, 1)
          c.withCanvasContext { ctx =>
            ctx.strokeStyle = "rgb(60,60,60)"
            ctx.lineWidth = 50
            ctx.beginPath()
            ctx.moveTo(100, 100)
            ctx.lineTo(770, 100)
            ctx.lineTo(770, 540)
            ctx.bezierCurveTo(670, 540, 150, 200, 150, 100)
            ctx.stroke()
          }
        }
      ))(tilesMode = false)
    ))
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "https://i.ytimg.com/vi/50L67a0BWgs/maxresdefault.jpg"),
        <("figcaption")("RoboCup Junior Resuce Line.")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## RoboCup Junior Rescue Line
        |
        |* Follow the line
        |
        |* At junctions, if there's a green square before the junction, turn in that direction
        |
        |* There may be gaps in the line
        |
        |* There may be obstacles on the line you have to go around
        |
        |Rather than *show* it, we're going to try *doing* it. We'll see it in two versions.
        |
        |* First, with LineTurtle, there aren't any physical obstacles. It's a turtle with line sensors.
        |
        |* Later, we'll use a (very simplified) physics simulation of a robot. Wheels can slip. Obstacles can lie in your path.
        |
        |""".stripMargin
    )
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderSlides



}
