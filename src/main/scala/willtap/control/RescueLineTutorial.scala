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

  def mdTask(markdown:String, tw:Int = 5, th:Int = 5, start:(Int, Int) = (0, 0), inaccurate:Boolean = true)(f: dom.CanvasRenderingContext2D => Unit) = new ExerciseStage() {

    override def completion: Challenge.Completion = Open

    private val (rx, ry) = start

    val codable = JSCodable(CanvasLand("exercise")(
      fieldSize= (tw * RescueLine.tileSize) -> (th * RescueLine.tileSize),
      r = LineTurtle(start._1 * RescueLine.tileSize + RescueLine.halfTile, start._2 * RescueLine.tileSize + RescueLine.halfTile) { r =>
        r.penDown = false
        r.sensorLimit = 16
        if (inaccurate) applyStandardInaccuracy(r)
      },
      setup = c => {
        println("Setup called in mdTask")
        c.fillCanvas("white")
        println("about to draw a line")

        c.withCanvasContext { ctx =>
          ctx.strokeStyle = "blue"
          ctx.lineWidth = 5
          ctx.moveTo(10, 10)
          ctx.lineTo(200,200)  
        }
        c.drawGrid("rgb(200,240,240)", RescueLine.tileSize, 1)
        c.withCanvasContext { ctx =>
          f(ctx)
        }
      }
    ))(tilesMode = false)

    override protected def render: DiffNode[Element, Node] = Challenge.textColumn(
      <.div(^.cls := "lead", ^.attr("style") := "height: 200px", Common.markdown(markdown)), codable
    )
  }

  def applyStandardInaccuracy(r:LineTurtle):Unit = {
    r.moveWobble = Random.nextDouble * 0.02
    r.moveWobbleBias = (Random.nextDouble - 0.5) * 0.0001
    r.turnInaccuracy = Random.nextDouble * 0.05
    r.turnBias = (Random.nextDouble - 0.5) * 0.05
    r.moveInaccuracy = Random.nextDouble * 0.05
    r.moveBias = (Random.nextDouble - 0.5) * 0.05
  }

  def applyLargeInaccuracy(r:LineTurtle):Unit = {
    r.moveWobble = Random.nextDouble * 0.02
    r.moveWobbleBias = (Random.nextDouble - 0.5) * 0.0001
    r.turnInaccuracy = Random.nextDouble * 0.1
    r.turnBias = (Random.nextDouble - 0.5) * 0.1
    r.moveInaccuracy = Random.nextDouble * 0.1
    r.moveBias = (Random.nextDouble - 0.5) * 0.1
  }

  def smallMaze(s:String, tw:Int = 5, th:Int = 2, start:(Int, Int) = (0, 0), inaccurate:Boolean = true, largeInaccurate:Boolean=false)(f: dom.CanvasRenderingContext2D => Unit) = {
    CanvasLand(s)(
      viewSize = (tw * RescueLine.tileSize) -> (th * RescueLine.tileSize),
      fieldSize= (tw * RescueLine.tileSize) -> (th * RescueLine.tileSize),
      r = LineTurtle(start._1 * RescueLine.tileSize + RescueLine.halfTile, start._2 * RescueLine.tileSize + RescueLine.halfTile) { r =>
        r.penDown = false
        r.sensorLimit = 16

        if (inaccurate) applyStandardInaccuracy(r)
        if (largeInaccurate) applyLargeInaccuracy(r)
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
          |underneath it.
          |These are relative to each other. So, if `r` is twice `g`, then the sensor will be twice as sensitive to
          |the red channel as it is to the green channel.
          |The equation it uses to come to a result is:
          |
          |`((r * red / 255) + (g * green / 255) + (b * blue / 255)) / (r + g + b)`.
          |
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
          smallMaze("Hello world", largeInaccurate = true) { ctx =>
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
      VNodeStage.twoColumn("Rescue Zones")(() => Common.markdown(
        s"""
           |Sometimes, you will come across an orange area with no line. This is the chemical spill.
           |
           |Inside the chemical spill, there may be green circles (survivors) in locations that move slightly
           |with each reset of the maze.
           |
           |Your robot should enter the orange zone, search for survivors and "rescue" them by painting a blue blob
           |on the survivor. (`setColour("blue")` and `penDown()` are available on the turtle to do this.
           |You'll then need to move forward a pixel to make it paint. Don't forget to `penUp()` afterwards.)
           |
           |Once your robot has found a survivor, it should find the line out of the chemical spill to continue its
           |journey. Try not to accidentally go back the way you came!
           |
           |The example code we've given you demonstrates a simple search pattern for a survivor
           |Again, it's not as sophisticated as what you will probably need.
           |For example, the sample code forgets to keep looking for the exit afterwards.
           |It's also a little susceptible to the problem that it can "look" at the edge of the survivor, reading some
           |orange and some green pixels. In which case, because the red and green it sees are roughly equal, it decides it's
           |neither part of the rescue zone nor the survivor.
           |
           |**Note** The survivor is quite a dark green (darker than the turn signal green).
           |
           |There is always exactly one survivor in each rescue zone, but rescue zones can vary in size.
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """addLineSensor(20, 0, 255, 0, 0) // colour sensor - red component
            |addLineSensor(20, 0, 0, 255, 0) // colour sensor - non-red component
            |
            |let foundSurvivor = false
            |
            |function isOrange() {
            |  let r = readSensor(0)
            |  let g = readSensor(1)
            |
            |  // orange is more red than green
            |  if (r > 0.4 && g < 0.9 * r) {
            |    return true
            |  } else {
            |    return false
            |  }
            |}
            |
            |function isSurvivor() {
            |  let r = readSensor(0)
            |  let g = readSensor(1)
            |  console.log(`r is ${r} and g is ${g}`)
            |
            |  // The survivor colour has a lot of green in it and not much red
            |  if (g > 0.4 && r < 0.9 * g) {
            |    return true
            |  } else {
            |    return false
            |  }
            |}
            |
            |// bad - the sample isn't even following the line!
            |while (!isOrange()) {
            |  forward(5)
            |}
            |forward(20)
            |left(80)
            |
            |// start searching for a survivor
            |let toggle = false
            |while (!foundSurvivor) {
            |   while (isOrange()) {
            |     forward(10)
            |   }
            |   if (isSurvivor()) {
            |     foundSurvivor = true
            |     forward(20)
            |     setColour("blue")
            |     penDown()
            |     forward(1)
            |     penUp()
            |     forward(10)
            |   } else {
            |     toggle = !toggle
            |     if (toggle) {
            |       right(160)
            |     } else {
            |       left(160)
            |     }
            |   }
            |}
            |
            |""".stripMargin,
          smallMaze("Hello world", th=3, start=(0,1), inaccurate = true) { ctx =>
            RescueLine.start(0, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.straight(1, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.rescueZone(2, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.rescueSurvivor(3, 1, RescueLine.FACING_EAST, ctx)
            RescueLine.end(4, 1, RescueLine.FACING_EAST, ctx)
          }, codeStyle = Some("max-height: 300px;")
        ))
      ),
      VNodeStage.twoColumn("Assignment Levels")(() => Common.markdown(
        s"""
           |There are *two* sets of levels for your robot to attempt:
           |
           |* **Set 1** have a robot that works accurately
           |* **Set 2** has a robot that has inaccuracies built in
           |
           |You should use the same code across *both* sets.
           |
           |**Remember:** This challenge doesn't save your work, doesn't submit your work, doesn't mark your work. If
           |you are taking this challenge as part of a class's assessment (rather than just finding it out in public),
           |you'll need to submit your code via your class's submission mechanism.
           |
           |Set 2 is harder than Set 1. This is so that when this is used as a class exercise, there are enough marks
           |in the easier challenge that "perfection" doesn't become the minimum standard.
           |""".stripMargin),
        () => <.div()
      ),
    )),
    Level(name = "ASSIGNMENT Set 1", stages = Seq(
      mdTask(
        """#### Set 1, Stage 1
          |
          |Just make your way to the exit
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        straight(1, 1, FACING_EAST, ctx)
        straight(2, 1, FACING_EAST, ctx)
        end(3, 1, FACING_EAST, ctx)
      },

      mdTask(
        """#### Set 1, Stage 2
          |
          |Just make your way to the exit
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        straight(1, 1, FACING_EAST, ctx)
        straight(2, 1, FACING_EAST, ctx)
        straight(3, 1, FACING_EAST, ctx)
        end(4, 1, FACING_EAST, ctx)
      },
      mdTask(
        """#### Set 1, Stage 3
          |
          |Just make your way to the exit
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        straight(1, 1, FACING_EAST, ctx)
        straight(2, 1, FACING_EAST, ctx)
        straight(3, 1, FACING_EAST, ctx)
        sharpTurnRight(4, 1, FACING_EAST, ctx)
        end(4, 2, FACING_SOUTH, ctx)
      },
      mdTask(
        """#### Set 1, Stage 4
          |
          |Remember, a green square before a t-juntion tells you which way to turn
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        straight(1, 1, FACING_EAST, ctx)
        straight(2, 1, FACING_EAST, ctx)
        teeRight(3, 1, FACING_EAST, ctx)
        teeLeft(3, 2, FACING_SOUTH, ctx)
        end(4, 2, FACING_EAST, ctx)
      },
      mdTask(
        """#### Set 1, Stage 5
          |
          |Remember, a green square before a t-juntion tells you which way to turn.
          |If there's no green square at a cross-roads, go straight across.
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        straight(1, 1, FACING_EAST, ctx)
        crossRoad(2, 1, FACING_EAST, ctx)
        teeRight(3, 1, FACING_EAST, ctx)
        teeRight(3, 2, FACING_SOUTH, ctx)
        teeRight(2, 2, Math.PI, ctx)
        end(2, 0, FACING_NORTH, ctx)
      },
      mdTask(
        """#### Set 1, Stage 6
          |
          |Remember, a green square before a t-juntion tells you which way to turn.
          |If there's no green square at a cross-roads, go straight across.
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        hump(1, 1, FACING_EAST, ctx)
        crossRoad(2, 1, FACING_EAST, ctx)
        teeRight(3, 1, FACING_EAST, ctx)
        roundaboutLeft(3, 2, FACING_EAST, ctx)
        teeRight(2, 2, Math.PI, ctx)
        end(2, 0, FACING_NORTH, ctx)
      },
      mdTask(
        """#### Set 1, Stage 7
          |
          |Remember, a green square before a t-juntion tells you which way to turn.
          |If there's no green square at a cross-roads, go straight across.
          |""".stripMargin, start=(0, 1), inaccurate = false) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        dashed(1, 1, FACING_EAST, ctx)
        crossRoad(2, 1, FACING_EAST, ctx)
        teeRight(3, 1, FACING_EAST, ctx)
        dashed(3, 2, FACING_SOUTH, ctx)
        roundaboutLeft(3, 3, FACING_EAST, ctx)
        teeRight(2, 3, Math.PI, ctx)
        dashedHump(2, 2, FACING_SOUTH, ctx)
        end(2, 0, FACING_NORTH, ctx)
      }
    )),
    Level(name = "ASSIGNMENT Set 2", stages = Seq(
      mdTask(
        """#### Set 2, Stage 1
          |
          |Remember, in Set 2, the robot is not perfectly accurate in its movements
          |""".stripMargin, start=(0, 1), inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        straight(1, 1, FACING_EAST, ctx)
        straight(2, 1, FACING_EAST, ctx)
        end(3, 1, FACING_EAST, ctx)
      },
      mdTask(
        """#### Set 2, Stage 2
          |
          |Remember, in Set 2, the robot is not perfectly accurate in its movements
          |""".stripMargin, start=(0, 1), inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        dashed(1, 1, FACING_EAST, ctx)
        hump(2, 1, FACING_EAST, ctx)
        sharpTurnRight(3, 1, FACING_EAST, ctx)
        end(3, 2, FACING_SOUTH, ctx)
      },
      mdTask(
        """#### Set 2, Stage 3
          |
          |Remember, in Set 2, the robot is not perfectly accurate in its movements
          |""".stripMargin, start=(0, 1), inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        dashed(1, 1, FACING_EAST, ctx)
        hump(2, 1, FACING_EAST, ctx)
        sharpTurnRight(3, 1, FACING_EAST, ctx)
        crossRoad(3, 2, FACING_SOUTH, ctx)
        teeRight(3, 3, FACING_SOUTH, ctx)
        dashed(2, 3, FACING_EAST, ctx)
        teeRight(1, 3, Math.PI, ctx)
        sharpTurnRight(1, 2, FACING_NORTH, ctx)
        dashed(2, 2, FACING_EAST, ctx)
        end(4,2, FACING_EAST, ctx)
      },
      mdTask(
        """#### Set 2, Stage 4
          |
          |Remember, in Set 2, the robot is not perfectly accurate in its movements
          |""".stripMargin, start=(0, 1), inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        dashed(1, 1, FACING_EAST, ctx)
        hump(2, 1, FACING_EAST, ctx)
        sharpTurnRight(3, 1, FACING_EAST, ctx)
        crossRoad(3, 2, FACING_SOUTH, ctx)
        teeRight(3, 3, FACING_SOUTH, ctx)
        zigzag(2, 3, FACING_EAST, ctx)
        teeRight(1, 3, Math.PI, ctx)
        sharpTurnRight(1, 2, FACING_NORTH, ctx)
        roundaboutAhead(2, 2, FACING_EAST, ctx)
        end(4,2, FACING_EAST, ctx)
      },
      mdTask(
        """#### Set 2, Stage 5
          |
          |Remember, in Set 2, the robot is not perfectly accurate in its movements
          |""".stripMargin, start=(0, 1), inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        dashed(1, 1, FACING_EAST, ctx)
        straight(2, 1, FACING_EAST, ctx)
        sharpTurnRight(3, 1, FACING_EAST, ctx)
        crossRoad(3, 2, FACING_SOUTH, ctx)
        rescueSurvivor(3, 3, FACING_EAST, ctx)
        end(3, 4, FACING_SOUTH, ctx)

      },
      mdTask(
        """#### Set 2, Stage 6
          |
          |Remember, in Set 2, the robot is not perfectly accurate in its movements
          |""".stripMargin, start=(0, 1), inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 1, FACING_EAST, ctx)
        dashed(1, 1, FACING_EAST, ctx)
        hump(2, 1, FACING_EAST, ctx)
        sharpTurnRight(3, 1, FACING_EAST, ctx)
        crossRoad(3, 2, FACING_SOUTH, ctx)
        rescueSurvivor(3, 3, FACING_EAST, ctx)
        teeRight(3, 4, FACING_SOUTH, ctx)
        zigzag(2, 4, FACING_EAST, ctx)
        teeRight(1, 4, FACING_WEST, ctx)
        straight(1, 3, FACING_NORTH, ctx)
        sharpTurnRight(1, 2, FACING_NORTH, ctx)
        roundaboutAhead(2, 2, FACING_EAST, ctx)
        end(4,2, FACING_EAST, ctx)
      },
      mdTask(
        """#### Set 2, Stage 7
          |
          |The ultimate test in this assignment.
          |Remember, there's a survivor in each rescue zone.
          |""".stripMargin, th=6, inaccurate = true) { ctx =>
        import RescueLine._

        start(0, 0, FACING_EAST, ctx)
        dashed(1, 0, FACING_EAST, ctx)
        hump(2, 0, FACING_EAST, ctx)
        roundaboutAhead(3, 0, FACING_EAST, ctx)
        sharpTurnRight(4, 0, FACING_EAST, ctx)
        roundaboutLeft(4, 1, FACING_EAST, ctx)
        rescueZone(3, 1, FACING_EAST, ctx)
        rescueSurvivor(2, 1, FACING_EAST, ctx)
        sharpTurnLeft(2, 2, FACING_SOUTH, ctx)
        dashed(3, 2, FACING_EAST, ctx)
        roundaboutLeft(4, 2, FACING_NORTH, ctx)
        crossRoad(4, 3, FACING_EAST, ctx)
        teeRight(4, 4, FACING_SOUTH, ctx)
        dashedHump(3, 4, FACING_EAST, ctx)
        zigzag(2, 4, FACING_EAST, ctx)
        rescueSurvivor(1, 4, FACING_EAST, ctx)
        rescueZone(1, 3, FACING_EAST, ctx)
        roundaboutAhead(1, 2, FACING_NORTH, ctx)
        sharpTurnLeft(1, 1, FACING_NORTH, ctx)
        teeLeft(0, 1, FACING_WEST, ctx)
        end(0, 2, FACING_SOUTH, ctx)
      },
    )),
  )

}
