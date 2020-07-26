package willtap.control

import canvasland.{CanvasLand, LineTurtle, RescueLine, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.{Complete, Level, Open}
import org.scalajs.dom
import org.scalajs.dom.{Element, Node, html}
import willtap.templates.{ExerciseStage, VNodeStage}
import willtap._
import willtap.imperativeTopic.PrefabCodable

import scala.util.Random

object RescueLineTutorial {

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("rescueLine", l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("rescueLine", 0, 0)), s"Start")
    }
  }

  implicit val onCompletionUpdate: () => Unit = () => challenge.rerender()


  object DrawASquare extends ExerciseStage() {
    override def completion: Challenge.Completion = Complete(None, None)

    override protected def render: DiffNode[Element, Node] = Challenge.textColumn(
      <.div(^.cls := "lead",
        Common.markdown(
          """First of all, let's just draw a triangle. There are three sides to a triangle, so three times we're going to need to go `forward(100)`, then turn
            |`left(120)`.
            |
            |Initially, just write it out longhand. Then, try putting it into a C-style for-loop.
            |
            |If you need them, there are crib notes on the next page. You can go forward and back between exercises at any time using the navigation on the right.
            |""".stripMargin),
      ),
      JSCodable(CanvasLand("drawASquare")(
        r = Turtle(320, 320),
        setup = (c) => {
          c.drawGrid("lightgray", 25)
        }
      ))()
    )
  }

  def mdTask(markdown:String, start:(Int, Int) = 320 -> 320) = new ExerciseStage() {

    override def completion: Challenge.Completion = Open

    private val (rx, ry) = start

    val codable = JSCodable(CanvasLand("drawASquare")(
      r = Turtle(rx, ry),
      setup = (c) => {
        c.drawGrid("lightgray", 25)
      }
    ))(tilesMode = false)

    override protected def render: DiffNode[Element, Node] = Challenge.textColumn(
      <.div(^.cls := "lead", ^.attr("style") := "height: 200px", Common.markdown(markdown)), codable
    )
  }

  def applyStandardInaccuracy(r:LineTurtle):Unit = {
    r.sensorLimit = 16
    r.moveWobble = Random.nextDouble * 0.02
    r.moveWobbleBias = (Random.nextDouble - 0.5) * 0.0001
    r.turnInaccuracy = Random.nextDouble * 0.1
    r.turnBias = (Random.nextDouble - 0.5) * 0.1
    r.moveInaccuracy = Random.nextDouble * 0.1
    r.moveBias = (Random.nextDouble - 0.5) * 0.1
  }

  def smallMaze(s:String, tw:Int = 5, th:Int = 2, inaccurate:Boolean = true)(f: dom.CanvasRenderingContext2D => Unit) = {
    CanvasLand(s)(
      viewSize = (tw * RescueLine.tileSize) -> (th * RescueLine.tileSize),
      fieldSize= (tw * RescueLine.tileSize) -> (th * RescueLine.tileSize),
      r = LineTurtle(RescueLine.halfTile, RescueLine.halfTile) { r =>
        r.penDown = false
        if (inaccurate) applyStandardInaccuracy(r)
      },
      setup = c => {
        c.fillCanvas("white")
        c.drawGrid("rgb(200,240,240)", RescueLine.tileSize, 1)
        c.withCanvasContext { ctx =>
          f(ctx)
        }
      }
    )
  }


  val levels = Seq(
    Level(name = "Introduction", stages = Seq(
      VNodeStage.twoColumn("Rescue Line")(() => Common.markdown(
        s"""
          |These exercises are loosely inspured by RoboCup Junior's Rescue Line competition.
          |We've changed the rules, however, so we can run our challenges using a modified Turtle Graphics
          |Turtle. The intent for this challenge is that you use the **same** control program to solve all the challenges,
          |but you develop it by working from challenge to challenge adapting and improving your program.
          |
          |The theme is there has been a chemical spill, and your robot has been sent in to locate survivors. It has to
          |follow the path laid out by the line (which sometimes has breaks in it) and in some stages, it'll find a
          |spill area to look for survivors in.
          |
          |**Remember** the challenges do not save or mark your work. Cut and paste your code to and from a text editor.
          |
          |#### Sensors
          |
          |`addLineSensor(x, y, r, g, b)` adds a sensor to the turtle. `x` and `y` are where to put the sensor on the
          |turtle. `+x` is towards the front of the turtle, and `+y` is towards the right of the
          |turtle.
          |
          |`r`, `g`, and `b` are how sensitive the sensor is to the red, green, and blue colour values
          |underneath it. `255` means completely sensitive.
          |The sensitivity number is bitwise-ANDed with the colour value. Usually, you'll want to use numbers like
          |`255` or `127` that are mostly binary `1`s. (If you set a sensitivity to `256` you might be surprised it is
          |completely *insensitive* - rgb values go from 0 to 255, and any of those numbers when bitwise-ANDed with 256
          |will produce `0`.)
          |If you wish to read a single colour's value, put a sensor that is only sensitive to that colour. e.g.
          |`addLineSensor(50, 0, 255, 0, 0)` would add a sensor that is only sensitive to red.
          |
          |`readSensor(i)` will give you a sensor's reading. The sensors are numbered in the order you add them, starting
          |at `0`. Each sensor looks at a little region of the canvas - a block about 5 pixels on each side - and averages
          |the colour values of the pixels to work out its reading. It then returns a value between `0` and `1` depending
          |on how much light it saw.
          |
          |Usually, you are limited to adding a maximum of **16** sensors.
          |
          |#### Example
          |
          |Mazes start at the green line and finish at the red line. There will be obstacles along the way.
          |The code example on the right assumes a perfectly accurate robot. As we're about to see, that's not what
          |you'll often be using.
          |
          |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """addLineSensor(20, -8, 0, 255, 0) // sensor 0, green only
            |addLineSensor(20, 8, 0, 255, 0) // sensor 1, green only
            |
            |while (true) {
            |  let l = readSensor(0) > 0.5 // left seeing light
            |  let r = readSensor(1) > 0.5 // right seeing light
            |
            |  if (l && r) {
            |    forward(5)
            |  }
            |}
            |""".stripMargin,
          smallMaze("Hello world", th=1, inaccurate = false) { ctx =>
            RescueLine.start(0, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.dashed(1, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(2, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(3, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.end(4, 0, RescueLine.FACING_EAST, ctx)
          }
        ))
      ),
      VNodeStage.twoColumn("Wobble and Bias")(() => Common.markdown(
        s"""
           |Robots aren't perfectly accurate in their movements. To simulate this, on many of the mazes the robot has
           |some in-built inaccuracy and bias.
           |
           |You might ask it to turn right 90 degrees, and it turns out it turns 93 degrees one time and 86 degrees the next.
           |As it moves forward or backward, the direction it is facing can also wobble slightly. Sometimes one wheel's
           |motor is stronger than the other and it'll slowly veer off to one side.
           |Your control program has to be robust to this. When the maze is reset, the robot's inaccuracy is re-randomised.
           |
           |The example on the right should show you what we mean - we've set it to try heading towards the exit in
           |a preprogrammed way, drawing a line behind it. If you reset it and re-run it, you'll see it doesn't always
           |do very well. We're going to need to write a program to **control** the robot, not just give it a set of
           |directions to go in.
           |
           |**Remember** if you're doing this as an assignment, we mark it against what we see happen. Don't just regenerate the
           |maze until you get a more accurate robot because it might be marked with a less accurate one.
           |
           |**Also Remember** Your robot is expected to follow the course, *not* just get to the exit. Cutting across
           |humps and ignoring roundabouts is not seen as success.
           |
           |
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """setColour("blue")
            |penDown()
            |
            |forward(480)
            |right(90)
            |forward(120)
            |right(90)
            |forward(480)
            |""".stripMargin,
          smallMaze("Hello world") { ctx =>
            RescueLine.start(0, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(1, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(2, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(3, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.sharpTurnRight(4, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.sharpTurnRight(4, 1, RescueLine.FACING_SOUTH, ctx)
            RescueLine.straight(3, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.hump(2, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.zigzag(1, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.end(0, 1, Math.PI, ctx)
          }
        ))
      ),
      VNodeStage.twoColumn("T-Junctions")(() => Common.markdown(
        s"""
           |Sometimes, you will come to a junction.
           |
           |If, just before the junction, there is a *green marking* on the maze, your robot must turn in that direction.
           |For example, the robot on the right will encounter three junctions (two on the roundabout). Each time, it
           |must take a green-marked exit.
           |
           |If there is *no* green marking at a cross-road, your robot should go straight ahead.
           |
           |You can detect the marking with a sensor made sensitive to green, and a variable. You will probably need
           |to make your code more sophisticated - this example has the robot perfectly accurate and no zigzags. Note
           |also that the line can have breaks in it, for instance the dashed segment.
           |
           |It is, however, an example of detecting the colour at two points: the green junction check and the
           |check for red-but-not-green to see if we're on the stop marker.
           |
           |In fact, I recommend putting a colour stop sensor a little way in front of the robot. That way, later, you'll
           |also be able to use it to detect orange and green colours, which you'll need when looking for survivors
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """addLineSensor(20, -6, 0, 255, 0) // line sensor, green only
            |addLineSensor(20, 6, 0, 255, 0) // line sensor, green only
            |addLineSensor(8, 8, 255, 0, 255) // junction sensor, a little closer and wider
            |
            |addLineSensor(0, 0, 255, 0, 0) // stop sensor - red component
            |addLineSensor(0, 0, 0, 255, 255) // stop sensor - non-red component
            |
            |let stop = false
            |
            |while (!stop) {
            |  let l = readSensor(0) > 0.5
            |  let r = readSensor(1) > 0.5
            |
            |  let jr = readSensor(2) < 0.5 // if this goes dark, turn right
            |
            |  stop = readSensor(3) > 0.5 && readSensor(4) < 0.4
            |  console.log(stop)
            |
            |  if (l && r) {
            |    forward(2)
            |  } else if (l && !r) {
            |    right(3)
            |    forward(1)
            |  } else if (!l && r) {
            |    left(2)
            |    forward(1)
            |  } else {
            |    if (jr) {
            |      forward(1)
            |      right(5)
            |    } else {
            |      forward(1)
            |    }
            |  }
            |}
            |""".stripMargin,
          smallMaze("Hello world", inaccurate = false) { ctx =>
            RescueLine.start(0, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(1, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(2, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(3, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.teeRight(4, 0, RescueLine.FACING_EAST, ctx)
            RescueLine.roundaboutLeft(4, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(3, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.dashed(2, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.crossRoad(1, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.end(0, 1, Math.PI, ctx)
          }, codeStyle = Some("max-height: 400px;")
        ))
      ),
    ))
  )


  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(ChallengeRoute("rescueLine", i, 0)),
    stagePath = (_, i, j) => Router.path(ChallengeRoute("rescueLine", i, j)),
    homeIcon = <.span(),
    scaleToWindow = true
  )

}
