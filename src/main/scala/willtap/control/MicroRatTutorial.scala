package willtap.control

import canvasland.{CanvasLand, LineTurtle, MicroRat, RescueLine, Turtle}
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

object MicroRatTutorial {

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("microRat", l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("microRat", 0, 0)), s"Start")
    }
  }

  implicit val onCompletionUpdate: () => Unit = () => challenge.rerender()

  def mdTask(markdown:String, mazeString:String, tw:Int = 5, th:Int = 5, start:(Int, Int) = (0, 0), inaccurate:Boolean = true)(f: dom.CanvasRenderingContext2D => Unit) = new ExerciseStage() {

    override def completion: Challenge.Completion = Open

    private val (rx, ry) = start

    val maze = new MicroRat({ sim =>
      sim.loadMazeFromString(mazeString)
    }, { sim => })

    val codable = JSCodable(CanvasLand("exercise")(
      fieldSize= (tw * MicroRat.ONE_TILE_PIXELS) -> (th * MicroRat.ONE_TILE_PIXELS),
      r = maze.robot,
      setup = c => {
        c.fillCanvas("white")
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


  def smallMaze(s:String, tw:Int = 5, th:Int = 2, mazeString:String, start:(Int, Int) = (0, 0), inaccurate:Boolean = true, largeInaccurate:Boolean=false)(f: dom.CanvasRenderingContext2D => Unit) = {
    val maze = new MicroRat({ sim =>
      sim.loadMazeFromString(mazeString)
    }, { sim => })

    CanvasLand(s)(
      viewSize = (tw * MicroRat.ONE_TILE_PIXELS) -> (th * MicroRat.ONE_TILE_PIXELS),
      fieldSize= (tw * MicroRat.ONE_TILE_PIXELS) -> (th * MicroRat.ONE_TILE_PIXELS),
      r = maze.robot,
      setup = c => {
        c.fillCanvas("black")
        maze.paintCanvas(c)
        c.addSteppable(maze.Goal)
        c.addSteppable(maze)
      }
    )
  }


  val levels = Seq(
    Level(name = "Introduction", stages = Seq(
      VNodeStage.twoColumn("MicroRat")(() => Common.markdown(
        s"""These challenges are loosely based on [MicroMouse](https://en.wikipedia.org/wiki/Micromouse), a robotics
           |challenge that has been running since the late 1970s, and the Robotic Bilby Competition, which was a
           |simplified challenge for secondary schools in Australia.
           |
           |In the challenge, your robot has to find its way to the middle of a maze, then go back to the start and
           |race to the middle. There's no line to follow - instead the maze has floor you can roll over, and walls
           |you can bump into.
           |
           |The challenge is again designed so you'll *talk a lot about strategy*. Don't share code, but the learning
           |experience is designed around talking to each other and asking for help. It is a difficult challenge, not
           |one you're expected to blast out in an evening.
           |
           |The challenge is also designed so you pick up points for what your robot can do. Even getting around the
           |first bend scores some points, but we're going to try to get you much further through the challenge.
           |
           |### Meet Bumper
           |
           |In this challenge, we're going to bring things a little closer to the robot.
           |
           |* The robot is physically simulated. It has mass and inertia. The motors inside Bumper apply forces to it.
           |  There's no simulated inaccuracy, but the weight and momentum of the robot cause their own difficulties.
           |
           |* To make up for Bumper being harder to steer, we've given it perfectly accurate position sensors.
           |  It has a compass to tell you where it's heading and rat-GPS that can tell you its position and velocity.
           |
           |* We've made the robot a ball, a little like [Sphero](https://en.wikipedia.org/wiki/Sphero).
           |  It has a couple of wheels and motors (left and right) inside the ball that can make it move and turn.
           |  It doesn't have any line sensors or range sensors - just a "bump" sensor for when it has bumped into something.
           |  We know where we are; we're going to find the walls by bumping into them.
           |
           |* It also has a light that you can change the colour of. (It starts out orange.)
           |
           |There's a little code on the right to make it bounce back and forth off the walls, but that's just to
           |illustrate that the walls are solid.
           |
           |We're going to have a lot to show you before you can get this little guy solving mazes, so let's start
           |with how on earth we steer it if all we can do is set the power and direction of the motors...
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function go() {
            |  if (forward) {
            |    setLeftPower(1)
            |    setRightPower(1)
            |  } else {
            |    setLeftPower(-1)
            |    setRightPower(-1)
            |  }
            |}
            |
            |let forward = true
            |clearCollision()
            |go()
            |
            |while (true) {
            |  if (isCollisionDetected()) {
            |    forward = !forward
            |    clearCollision()
            |    go()
            |  }
            |}
            |""".stripMargin,
          smallMaze("Hello world", 8, 3,
            """########
              |#......#
              |########
              |""".stripMargin, (1,1), inaccurate = false) { ctx =>
          }
        ))
      ),
    )),
  )


  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(ChallengeRoute("microRat", i, 0)),
    stagePath = (_, i, j) => Router.path(ChallengeRoute("microRat", i, j)),
    homeIcon = <.span(),
    scaleToWindow = true
  )

}
