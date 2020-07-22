package willtap.control

import canvasland.{CanvasLand, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.DeckBuilder
import willtap.Common

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
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "https://upload.wikimedia.org/wikipedia/commons/d/d5/HMS_Association_%281697%29.jpg"), <("figcaption")("Engraving of the Scilly Naval Disaster 1707")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## Open Loop control
        |
        |* Many control systems just follow a fixed set of instructions
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Turtle graphics"),
      Common.markdown(
        """The "turtle" in the middle of the screen can be asked to turn `left` and `right`, or to move `forward` and `back`. It has a pen
          |that can change colour, and if the pen is down it'll leave ink behind as it goes...
          |""".stripMargin),
      JSCodable(CanvasLand()(
        fieldSize = 1280 -> 720,
        r = Turtle(320, 320),
        setup = (c) => {
          c.withCanvasContext { ctx =>
            ctx.drawImage(scilly, 0, 0, 1000, 1000, 0, 0, 1000, 1000)
          }
        }
      ))()
    ))
    .markdownSlide(
      """## still writing slides...
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode



}
