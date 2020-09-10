package willtap.control

import canvasland.{CanvasLand, LineTurtle, MicroRat, RescueLine, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.{Complete, Level, Open}
import org.scalajs.dom
import org.scalajs.dom.{Element, Node, html}
import willtap.templates.{ExerciseStage, MarkdownStage, VNodeStage, YouTubeStage}
import willtap._
import willtap.imperativeTopic.{JellyFlood, PrefabCodable}

import scala.util.Random

object MicroRatTutorial {

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("microRat", l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("microRat", 0, 0)), s"Start")
    }
  }

  implicit val onCompletionUpdate: () => Unit = () => challenge.rerender()

  def mdTask(markdown:String, mazeString:String, tw:Int = 10, th:Int = 10, start:(Int, Int) = (0, 0), inaccurate:Boolean = true) = new ExerciseStage() {

    override def completion: Challenge.Completion = Open

    private val (rx, ry) = start

    val maze = new MicroRat()({ sim =>
      sim.loadMazeFromString(mazeString)

    }, { sim => })

    val codable = JSCodable(CanvasLand("exercise")(
      fieldSize= (tw * MicroRat.ONE_TILE_PIXELS) -> (th * MicroRat.ONE_TILE_PIXELS),
      r = maze.robot,
      setup = c => {
        c.fillCanvas("black")
        maze.paintCanvas(c)
        c.addSteppable(maze.Goal)
        c.addSteppable(maze)
      }
    ))(tilesMode = false)

    override protected def render: DiffNode[Element, Node] = Challenge.textColumn(
      <.div(^.cls := "lead", ^.attr("style") := "height: 200px", Common.markdown(markdown)), codable
    )
  }


  def smallMaze(s:String, tw:Int = 5, th:Int = 2, mazeString:String, start:(Int, Int) = (0, 0), inaccurate:Boolean = true, largeInaccurate:Boolean=false)(f: dom.CanvasRenderingContext2D => Unit) = {
    val maze = new MicroRat(dimensions = (tw, th), start=start)({ sim =>
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
           |We're going to have a lot to show you before you can get this little guy solving mazes...
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
              |########""".stripMargin, (1,1), inaccurate = false) { ctx =>
          }
        ))
      ),
      YouTubeStage("dhWUOp5wOgU"),
      mdTask(
        """In a moment, we'll keep going, writing some turn and move functions. But before we do, just have a play
          |with Bumper. A little challenge: make Bumper drive right, but turn the tail light green after it gets
          |halfway. The tiles are 64 pixels wide, so the 6th tile starts at x coordinate 320. You can set the tail
          |light colour with `setLightColour("green")` and get the `x` position with `getX()`
          |""".stripMargin,
        """..........
          |.###..###.
          |.#......#.
          |.#.####.#.
          |...#......
          |......#...
          |.#.####.#.
          |.#......#.
          |.###..###.
          |..........
          |""".stripMargin
      )
    )),
    Level(name = "Turning Bumper", stages = Seq(
      YouTubeStage("mnhsIcB-WO4"),
      mdTask(
        """Now it's your turn. Try implementing the turn function. Then see what angles you need to turn it to to
          |hit the various wall blocks. You might find they're a little far away - Bumper's turn won't be totally
          |accurate. That's going to be ok, because in the maze we'll only be steering him to destinations one
          |square away (before we work out where we want to turn to next).
          |
          |Hint: Once you've done this, copy your code and save it in a text file. You'll need it for the next stage.
          |""".stripMargin,
        """.........#
          |..........
          |..........
          |..........
          |..........
          |.........#
          |..........
          |..........
          |..........
          |#....#...#
          |""".stripMargin
      )
    )),
    Level(name = "Moving Bumper", stages = Seq(
      YouTubeStage("2mNgVdMqPo0"),
      mdTask(
        """Your move this time. Implement the move function, and see if you can steer bumper through this fixed maze.
          |
          |In the challenge we're building up to, you're not allowed to hard-code the route or give Bumper "foreknowledge
          |of the maze". For the moment, though, let's just test our move function works
          |
          |Hint: Once you've done this, copy your code and save it in a text file. You'll need it for the next stage.
          |""".stripMargin,
        """..........
          |.###..###.
          |.#......#.
          |.#.####.#.
          |...#......
          |......#...
          |.#.####.#.
          |.#......#.
          |.###..###.
          |..........
          |""".stripMargin
      )
    )),
    Level(name = "Mapping and Routefinding", stages = Seq(
      VNodeStage.twoColumn("Remember our Jelly Flood")(() => Common.markdown(
        s"""
           |Let's go back to the flood fill algorithm, where we imagine pouring some liquid jelly onto the goal.
           |
           |After zero turns, it's only on the goal square.  \t
           |After one turn, it'd move to the neighbouring squares.  \t
           |After two turns, it'd move to their neighbours.  \t
           |
           |If we count how long it takes to reach a square, that's the *distance from the goal*.
           |We can then use this in an algorithm - we move to any square that has a lower goal distance than our own.
           |
           |That's a route-finding algorithm if we know where the wall are. Curiously, it's works as an exploration
           |algorithm when we *don't* know where the walls are...
           |
           |""".stripMargin),
        () => VNodeStage.card(
          JellyFlood(w=10, h=10, goalX=5, goalY=5, mazeString = Some(
            """..........
              |.###..###.
              |.#......#.
              |.#.######.
              |.#.#......
              |.#....#...
              |.######.#.
              |.#......#.
              |.###..###.
              |..........""".stripMargin))
        )
      ),
      VNodeStage.twoColumn("Mapping")(() => Common.markdown(
        s"""
           |To begin with, we assume the entire map is passable, and try to follow the fastest route to the middle.
           |
           |""".stripMargin),
        () => VNodeStage.card(
          JellyFlood(w=10, h=10, goalX=5, goalY=5, mazeString = Some(
            """..........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........""".stripMargin))
        )
      ),
      VNodeStage.twoColumn("Mapping")(() => Common.markdown(
        s"""
           |Whenever we bump into a wall, we remember it's a wall and re-run the flood.
           |
           |""".stripMargin),
        () => VNodeStage.card(
          JellyFlood(w=10, h=10, goalX=5, goalY=5, mazeString = Some(
            """..........
              |.#........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........
              |..........""".stripMargin))
        )
      ),
      VNodeStage.twoColumn("Mapping")(() => Common.markdown(
        s"""
           |After a while, just from bumping into walls, we discover more of the map.
           |
           |So, to implement it you're going to need *two 2-dimensional arrays*:
           |
           |* One to hold the map (as you know it so far)
           |* One to hold the estimated distances from the goal
           |
           |You can initialise an empty two-dimensional array like this:
           |
           |```
           |let grid = []
           |for (let y = 0; y < 10; y++) {
           |  let row = []
           |  for (let x = 0; x < 10; x++) {
           |    row[x] = true
           |  }
           |  grid[y] = row
           |}
           |```
           |
           |That should give you:
           |
           |```js
           |[
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |  [true, true, true, true, true, true, true, true, true, true],
           |]
           |```
           |
           |Whenever you bump into a wall, turn that grid entry false, and re-run the jelly flood.
           |""".stripMargin),
        () => VNodeStage.card(
          JellyFlood(w=10, h=10, goalX=5, goalY=5, mazeString = Some(
            """..........
              |.###.###..
              |..#...#..#
              |#.#.##.#..
              |....#...#.
              |##.##.#.#.
              |....###.#.
              |.####.....
              |......####
              |.###.....#""".stripMargin))
        )
      ),
      VNodeStage.twoColumn("Routefinding")(() => Common.markdown(
        s"""The jelly flood needs a grid of numbers.
           |
           |Start by assuming everything is *very far* from the goal. Say, set everything in the distance grid to 999
           |
           |Then, what we're going to do is a little recursion. I'm going to give you the algorithm; you write it in
           |code:
           |
           |Let's call the function `flood`, and let's say it takes `x`, `y`, and `d` as parameters. `x` and `y` are
           |the coordinates of a square, and `d` is the distance the jelly has reached.
           |
           |1. If x and y are inside the grid (between 0 and 9)...
           |2. ... and the entry in the map is floor (true) ...
           |3. ... and the entry in the distance grid is *larger than* d (i.e., the jelly has not already reached this square)...
           |3. set the entry in the grid to d
           |4. and call `flood` for the four neighbouring squares, using `d+1` as the distance parameter
           |   (i.e. make the jelly flow to them next, so set their distance one square higher)
           |
           |You start the flood by calling `flood` on the goal square, with a distance of `0`.
           |
           |I recommend you test that in a JavaScript console, just by hard-coding a map array and a distance array,
           |and running your function.
           |
           |It should finish quickly. If it doesn't you might have an "infinite recursion".
           |e.g. if you forget to check the distance is larger than d, then it'll look at the neighbour, but then
           |come back because this square is one of its neighbour's neighbours, and then it'll go back to the neighbour,
           |and then it'll come back... and so on.
           |
           |It should recursively flood the whole grid, and you'll then have a map of how far each square is from the
           |goal.
           |""".stripMargin),
        () => <.div()
      ),
      VNodeStage.twoColumn("Putting it all together")(() => Common.markdown(
        s"""This has hopefully given you the parts, now you just have to put them together.
           |
           |This tutorial's given you:
           |
           |* A function for turning to face a particular direction
           |* A function for turning to a square and moving to it
           |* A sensor for if you've hit a wall
           |* A function for backing off slightly if you've hit a wall (so you're not stuck on the wall)
           |* A grid of which tiles are floor or wall (start by guessing it's all floor)
           |* A way of working out a grid of distances to any square in the maze, by flood-fill
           |
           |Bumper needs to:
           |
           |* Start exploring its way to the middle
           |* Return to the start square
           |* Then follow the fastest path it found back to the middle
           |
           |Remember, the tiles are 64px by 64px.
           |
           |So, the middle of any tile is at co-ordinate (x * 64 + 32, y * 64 + 32)
           |
           |If you want to go from a coordinate to an index into an array of tiles, `Math.floor(x / 64)` should give
           |you the index of the tile you're in
           |""".stripMargin),
        () => <.div()
      ),
    )),
    Level(name = "ASSIGNMENT", stages = Seq(
      VNodeStage.twoColumn("Assignment Levels")(() => Common.markdown(
        s"""
           |In the challenge levels, you need to program Bumper to map and solve the maze, go back to the start, and
           |then race to the middle again.
           |
           |So it's more visible what Bumper is doing:
           |
           |* When Bumper is exploring and mapping the maze, set the tail-light blue
           |* When Bumper is returning to the start square, set it red
           |* When Bumper is racing to the middle, set it green
           |
           |There are a few maps for you to try your program on. You should use the same program for Bumper, we've just
           |given you a few different mazes to try it out on.
           |
           |Note, though, that the score *isn't* dependent on how *many* mazes you can solve, it's based on how your
           |robot performs in one maze. (It picks up points for moving, turning corners, finding its way to the middle,
           |etc.) We just don't tell you which one we'll try it out in.
           |
           |**One more thing:** You're going to need to know how Bumper can find out where the goal is. The *pixel*
           |coordinates of the goal can be found with `getGoalX()` and `getGoalY()`, but you just need to get Bumper
           |into the right tile.
           |
           |The *tile* index of that can be found with
           |
           |```
           |let goalTileX = Math.floor(getGoalX() / 64)
           |let goalTileY = Math.floor(getGoalY() / 64)
           |```
           |
           |(Take the pixel coordinates, divide by the fact tiles are 64 pixels wide, and round down because we were
           |given the coordinate of the middle of the square.)
           |
           |Or, shortcut: it's always in tile (5, 5)
           |""".stripMargin),
        () => <.div()
      ),
      mdTask(
        """## Map 1
          |
          |A test maze for you to try your program on.
          |""".stripMargin,
        """..........
          |.###..###.
          |.#......#.
          |.#.####.#.
          |...#......
          |......#...
          |.#.####.#.
          |.#......#.
          |.###..###.
          |..........
          |""".stripMargin
      ),
      mdTask(
        """## Map 2
          |
          |A test maze for you to try your program on.
          |""".stripMargin,
        """..........
          |.###.###..
          |..#...#..#
          |#.#.##.#..
          |....#...#.
          |##.##.#.#.
          |....###.#.
          |.####.....
          |......####
          |.###.....#""".stripMargin
      ),
      mdTask(
        """## Map 3
          |
          |A test maze for you to try your program on.
          |""".stripMargin,
        """...#......
          |.#.#..###.
          |.#..#...#.
          |##.####...
          |...#....#.
          |.#....#.##
          |.#.####.#.
          |.#.#.#..#.
          |.###.#.##.
          |..........""".stripMargin
      )
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
