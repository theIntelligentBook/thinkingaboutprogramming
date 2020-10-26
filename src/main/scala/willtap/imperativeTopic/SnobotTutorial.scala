package willtap.imperativeTopic

import canvasland.{CanvasLand, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.{Complete, Level, Open}
import lavamaze.{FloorTile, Goal, Maze, Overlay}
import org.scalajs.dom.{Element, Node}
import willtap.templates.{ExerciseStage, MarkdownStage, VNodeStage}
import willtap.{ChallengeRoute, Common, DeckRoute, IntroRoute, Router}

import scala.util.Random

object SnobotTutorial {

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("lavaMaze", l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ChallengeRoute("lavaMaze", 0, 0)), s"Start")
    }
  }

  implicit val onCompletionUpdate: () => Unit = () => challenge.rerender()


  /** Adds X and Y functions for Snobot and Goal locations to a maze */
  def addXYFunctions(maze:Maze):Unit = {
    maze.additionalFunctions = Seq(
      ("getX", Seq.empty, () => maze.snobot.tx),
      ("getY", Seq.empty, () => maze.snobot.ty),
      ("getGoalX", Seq.empty, () => maze.getGoalX),
      ("getGoalY", Seq.empty, () => maze.getGoalY),
    )
  }

  def mdTask(markdown:String, vs:(Int, Int) = 10 -> 10, ms:(Int, Int) = 10 -> 10,
             setup:Maze => _, beforeClass:Maze => _ = (_) => {}) = new ExerciseStage() {

    override def completion: Challenge.Completion = Open

    private val maze = Maze("exercise")(vs, ms)(setup)
    beforeClass(maze)

    private val codable = JSCodable(maze)(tilesMode = false)
    override protected def render: DiffNode[Element, Node] = Challenge.textColumn(
      <.div(^.cls := "lead", ^.attr("style") := "height: 200px", Common.markdown(markdown)), codable
    )

  }

  def smallMaze(s:String, vs:(Int, Int) = 10 -> 10, ms:(Int, Int) = 10 -> 10, setup:Maze => _, beforeClass:Maze => _ = (_) => {}) = {
    val m = Maze(s)(
      viewSize = vs,
      mazeSize = ms
    )(setup)

    beforeClass(m)
    m
  }

  val levels = Seq(
    Level(name = "Introduction", stages = Seq(
      VNodeStage.twoColumn("Welcome to the Lava Maze")(() => Common.markdown(
        s"""
           |Let's meet our little game environment.
           |
           |First, I'd better give you a quick rundown of the *functions* you can call in the environment:
           |
           |* `up()`, `down()`, `left()`, and `right()` make Snobot move one square.  \t
           |  Snobot can't walk through walls, and can't survive walking over lava, being caught by a blob guard, or
           |  being hit by a rolling boulder.
           |
           |* `canGoUp()`, `canGoDown()`, `canGoLeft()`, and `canGoRight()` return true if Snobot is stationary and
           |  Snobot thinks it can move safely into the square.
           |
           |We'll introduce a few more as we go, but remember, you can also write your own functions.
           |
           |Don't forget to copy and paste your code into a text editor, because the Lava Maze *won't save it for you*.
           |
           |If you are submitting the "assignment" exercises for credit at a school or university, you'll have to submit your code via your assignment's
           |instructions, because *the Lava Maze can't submit your work for you*.
           |
           |Your task is always to get Snobot to the goal, so he can teleport away. He will only teleport if he is *idle*
           |on the goal tile. As you can see, the maze starts frozen until you press play. That's so the Blob Guards
           |can't catch you before you begin!
           |
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while(canGoRight()) {
            |  right();
            |}
            |""".stripMargin,
          smallMaze("Hello world", 8 -> 3, 8 -> 3, setup = { maze =>
            maze.loadFromString(
              s"""
                | ZS...G
                |""".stripMargin)
          })
        ))
      ),
      mdTask(s"""### Right and down
                |
                |This maze is always the same size. Snobot just has to run 7 tiles right and 1 tile down.
                |
                |Try writing a for loop to make it shorter
                |
                |""".stripMargin,
        setup = _.loadFromString(
          """
            |
            |
            | S.......
            |        G
            |
            |
            |
            |""".stripMargin)
      ),
      VNodeStage.twoColumn("Lone boulders can be pushed")(() => Common.markdown(
        s"""
           |Snobot can push boulders, but he doesn't know he can until he tries.
           |
           |On the right, we have Snobot standing next to a boulder. When we ask him if he can go right, he'll say no.
           |But if we ask him to go right, he'll manage to push the boulder ahead of him.
           |""".stripMargin),
        () => VNodeStage.card(LoggingPrefabCodable(
          """if (canGoRight()) {
            |  println("Yes, I can go Right")
            |} else {
            |  println("No, there's a boulder in the way")
            |}
            |right()
            |down()
            |""".stripMargin,
          smallMaze("Hello world", 8 -> 4, 8 -> 4, setup = { maze =>
            maze.loadFromString(
              s"""
                 | SO...
                 |  G
                 |""".stripMargin)
          })
        ))
      ),
      mdTask(s"""### Blob Guards can be squashed
                |
                |Try making your way to the exit, pushing the boulder ahead of you. You should find you can squash
                |the Blob Guard with the boulder.
                |
                |""".stripMargin,
        setup = _.loadFromString(
          """
            |
            |
            | S.O....Z
            |        G
            |
            |
            |
            |""".stripMargin)
      ),
      mdTask(s"""### Diamonds are collectable and slippery
                |
                |We've popped in a boulder and a couple of diamonds and a boulder that wants to roll left.
                |
                |Try guiding snobot right 5 and down 1. You should find the blob guard doesnt have a good day.
                |""".stripMargin,
        setup = _.loadFromString(
          """
            |
            |
            | Z.S.*...
            |     *< G
            |     *
            |
            |
            |""".stripMargin)
      ),
    )),

    Level(name = "Algorithms", stages = Seq(
      VNodeStage.twoColumn("Algorithms for a changing world...")(() => Common.markdown(
        s"""
           |An *algorithm* is a set of rules that can be used to solve a problem.
           |
           |So far, our algorithm to solve the maze has only worked for fixed mazes.
           |In these puzzles, though, we're going to set different mazes that will need different solutions.
           |
           |Sometimes, the maze is going to change randomly according to some set rules, **so you'll need to run the
           |program more than once to check your answer**.
           |
           |For example, the maze on the right reflects itself vertically every on every try, and the code will only
           |work for one orientation! Oops!
           |
           |Some challenges encourage you to code your solution in a particular way. For instance, defining your own
           |functions that you'll then call in order (so the higher-level algorithm is more obvious) or functions you
           |call from inside a loop.
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while(canGoRight()) {
            |  right();
            |}
            |down();
            |""".stripMargin,
          {
            var parity = false
            smallMaze("Hello world", 8 -> 4, 8 -> 4, setup = { maze =>
              parity = !parity
              if (parity) {
                maze.loadFromString(
                  """
                    | S....
                    |     G
                    |""".stripMargin)
              } else {
                maze.loadFromString(
                  """
                    |     G
                    | S....
                    |""".stripMargin)
              }
            })
          }
        ))
      ),
      VNodeStage.twoColumn("The Cartesian Plane")(() => Common.markdown(
        s"""
           |The Lava Maze is made up of tiles that have `x` and `y` coordinates.
           |
           |This is the same maze from before, but this time we've overlayed the `x`,`y` coordinates of each tile onto
           |the tiles. The tiles always know their coordinates, it's just that this time we've drawn them on the
           |screen.
           |
           |Now, if only we had a way of knowing if the goal was above or below us...
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while(canGoRight()) {
            |  right();
            |}
            |down();
            |""".stripMargin,
          {
            var parity = false
            smallMaze("Hello world", 8 -> 4, 8 -> 4, setup = { maze =>
              parity = !parity
              maze.addOverlay(new Overlay.CoordinateOverlay(maze))

              if (parity) {
                maze.loadFromString(
                  """
                    | S....
                    |     G
                    |""".stripMargin)
              } else {
                maze.loadFromString(
                  """
                    |     G
                    | S....
                    |""".stripMargin)
              }
            })
          }
        ))
      ),
      {
        var parity = false
        mdTask(s"""### Topsy turvy world
                  |
                  |We've given you the maze from the previous page - that reflects every time you try it. It's your turn to solve it.
                  |
                  |We've taken away the overlay, but we've given you a couple of extra functions: `getY()` will return
                  |Snobot's current `y` coordinate, and `getGoalY()` will return the `y` coordinate of the goal.
                  |You might want to try `println(getY())` and `println(getGoalY())` first to see what they do.
                  |
                  |""".stripMargin,
          vs = 7 -> 4, ms = 7 -> 4,
          setup = (maze) => {
            parity = !parity

            if (parity) {
              maze.loadFromString(
                """
                  | S....
                  |     G
                  |""".stripMargin)
            } else {
              maze.loadFromString(
                """
                  |     G
                  | S....
                  |""".stripMargin)
            }
          },
          beforeClass = (maze) => maze.additionalFunctions = Seq(
            ("getY", Seq.empty, () => maze.snobot.ty),
            ("getGoalY", Seq.empty, () => maze.getGoalY),
          )
        )
      },
      VNodeStage.twoColumn("If the goal's above me, I want to go up...")(() => Common.markdown(
        s"""
           |Snobot can push boulders, but he doesn't know he can until he tries.
           |
           |On the right, we have Snobot standing next to a boulder. When we ask him if he can go right, he'll say no.
           |But if we ask him to go right, he'll manage to push the boulder ahead of him.
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while (canGoRight()) {
            |  right()
            |}
            |
            |if (getGoalY() > getY()) {
            |  down()
            |} else if (getGoalY() < getY()) {
            |  up()
            |}
            |""".stripMargin,
          {
            var parity = false
            smallMaze("Hello world", 8 -> 4, 8 -> 4, setup = { maze =>
              parity = !parity
              maze.addOverlay(new Overlay.CoordinateOverlay(maze))

              if (parity) {
                maze.loadFromString(
                  """
                    | S....
                    |     G
                    |""".stripMargin)
              } else {
                maze.loadFromString(
                  """
                    |     G
                    | S....
                    |""".stripMargin)
              }
            }, beforeClass = (maze) => maze.additionalFunctions = Seq(
              ("getY", Seq.empty, () => maze.snobot.ty),
              ("getGoalY", Seq.empty, () => maze.getGoalY),
            ))
          }
        ))
      ),
      mdTask(s"""### A little homing algorithm
                |
                |See if you can generalise the solution we had before to write a simple homing algorithm (the same one
                |the homing blob guard uses). This time, we've given you `getX()`, `getY()`, `getGoalX()`, and `getGoalY()`.
                |
                |The maze is always the same, but the goal and Snobot keep moving around...
                |""".stripMargin,
        setup = maze => {
          for { x <- 1 to 8; y <- 1 to 8 } maze.setTile(x, y, FloorTile)
          val sx = Random.nextInt(8) + 1
          val sy = Random.nextInt(8) + 1

          var (gx, gy) = (0, 0)
          do {
            gx = Random.nextInt(6) + 1
            gy = Random.nextInt(6) + 1
          } while (gx == sx || gy == sy)

          maze.snobotStart = (sx, sy)
          maze.addFixture(new Goal(gx, gy))
        },
        beforeClass = addXYFunctions _
      ),
      VNodeStage.twoColumn("Simple homing on a square")(() => Common.markdown(
        s"""
           |You might find you end up with this kind of solution for our simple homing algorithm.
           |
           |But, of course, we've not taken account of any lava squares, falling boulders, or dead-ends in our way.
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while (true) {
            |  if (getGoalY() > getY()) {
            |    down()
            |  } else if (getGoalY() < getY()) {
            |    up()
            |  } else if (getGoalX() < getX()) {
            |    left()
            |  } else if (getGoalX() > getX()) {
            |    right()
            |  }
            |}
            |""".stripMargin,
            smallMaze("Hello world", vs = 6 -> 6, ms = 6 -> 6,
              setup = maze => {
                for { x <- 1 to 4; y <- 1 to 4 } maze.setTile(x, y, FloorTile)
                val sx = Random.nextInt(4) + 1
                val sy = Random.nextInt(4) + 1

                var (gx, gy) = (0, 0)
                do {
                  gx = Random.nextInt(4) + 1
                  gy = Random.nextInt(4) + 1
                } while (gx == sx || gy == sy)

                maze.snobotStart = (sx, sy)
                maze.addFixture(new Goal(gx, gy))
              },
              beforeClass = maze => {
                maze.additionalFunctions = Seq(
                  ("getX", Seq.empty, () => maze.snobot.tx),
                  ("getY", Seq.empty, () => maze.snobot.ty),
                  ("getGoalX", Seq.empty, () => maze.getGoalX),
                  ("getGoalY", Seq.empty, () => maze.getGoalY),
                )
              }
            )
        ))
      ),

    )),

    Level(name = "Flood-fill pathfinding", stages = Seq(
      VNodeStage.twoColumn("We've reached a dead end...")(() => Common.markdown(
        s"""
           |Unfortunately, our simple homing algorithm doesn't cope very well with dead ends. It happily plunges down
           |blind alleys without any hope of getting out again.
           |
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while (true) {
            |  if (getGoalY() > getY() && canGoDown()) {
            |    down()
            |  } else if (getGoalY() < getY() && canGoUp()) {
            |    up()
            |  } else if (getGoalX() < getX() && canGoLeft()) {
            |    left()
            |  } else if (getGoalX() > getX() && canGoRight()) {
            |    right()
            |  }
            |}
            |""".stripMargin,

            smallMaze("Hello world", 8 -> 6, 8 -> 6, setup = { maze =>
                maze.loadFromString(
                  """
                    | S.....
                    |    .
                    |    ...
                    |    . G
                    |""".stripMargin)
            }, beforeClass = addXYFunctions _)
        ))
      ),

      VNodeStage.twoColumn("Jelly flood!")(() => Common.markdown(
        s"""
           |Imagine someone is pouring jelly onto the goal square.
           |
           |After zero turns, it's only on the goal square.  \t
           |After one turn, it'd move to the neighbouring squares.  \t
           |After two turns, it'd move to their neighbours.  \t
           |
           |If we count how long it takes to reach a square, that's the *distance from the goal*.
           |
           |We can then use this in an algorithm - we move to any square that has a lower goal distance than our own.
           |""".stripMargin),
        () => VNodeStage.card(
          JellyFlood()
        )
      ),
      {
        val overlay = new Overlay.FloodFill()

        mdTask(
          """## Countdown...
            |
            |In this exercise, we've given you `getX()`, `getY()`, and `getGoalDistance(x, y)`. See if you can find
            |your way to the exit...
            |""".stripMargin,
          setup = maze => {
            maze.addOverlay(overlay)
            for { x <- 0 to 1; y <- 0 to 1 } {
              maze.setTile(x,y, FloorTile)
              maze.setTile(10 - x - 1, 10 - y - 1, FloorTile)
            }

            for {
              mid <- Seq(Random.nextInt(7) + 2, Random.nextInt(7) + 2)
            } {
              for { i <- 1 until 10 } maze.setTile(i, 1, FloorTile)
              for { i <- 0 until 10 } maze.setTile(mid, i, FloorTile)
              for { i <- mid until 9 } maze.setTile(i, 8, FloorTile)
            }
            maze.addFixture(new Goal(9, 9))
          },
          beforeClass = maze => {
            maze.additionalFunctions = Seq(
              ("getX", Seq.empty, () => maze.snobot.tx),
              ("getY", Seq.empty, () => maze.snobot.ty),
              ("getGoalDistance", Seq("number", "number"), (x:Int, y:Int) => {
                overlay.distanceMap.getOrElse(x -> y, Int.MaxValue)
              })
            )
          }
        )
      },

      VNodeStage.twoColumn("Exiting by numbers")(() => Common.markdown(
        s"""
           |The code on the right should solve most mazes.
           |
           |This is where we're going to leave our automatic pathfinding for now - we'll come back to it when we
           |need to *explore* mazes to solve them much later.
           |
           |In our games, this kind of totally automatic algorithm would still be thwarted by obstacles. As we'll see on
           |the next slide, just a boulder in the way would stop our jelly from reaching us.
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while (getGoalDistance(getX(), getY()) > 0) {
            |  let x = getX()
            |  let y = getY()
            |  let g = getGoalDistance(x, y)
            |
            |  if (getGoalDistance(x+1, y) < g) {
            |    right()
            |  } else if (getGoalDistance(x-1, y) < g) {
            |    left()
            |  } else if (getGoalDistance(x, y+1) < g) {
            |    down()
            |  } else if (getGoalDistance(x, y-1) < g) {
            |    up()
            |  }
            |}
            |""".stripMargin,

          {
            val overlay = new Overlay.FloodFill()
            smallMaze("Hello world", 8 -> 6, 8 -> 6, setup = { maze =>
              maze.addOverlay(overlay)
              for { x <- 0 to 1; y <- 0 to 1 } {
                maze.setTile(x,y, FloorTile)
                maze.setTile(8 - x - 1, 6 - y - 1, FloorTile)
              }

              for {
                mid <- Seq(Random.nextInt(7) + 2, Random.nextInt(7) + 2)
              } {
                for { i <- 1 until 8 } maze.setTile(i, 1, FloorTile)
                for { i <- 0 until 6 } maze.setTile(mid, i, FloorTile)
                for { i <- mid until 7 } maze.setTile(i, 4, FloorTile)
              }
              maze.addFixture(new Goal(7, 5))
            }, beforeClass = maze => {
              maze.additionalFunctions = Seq(
                ("getX", Seq.empty, () => maze.snobot.tx),
                ("getY", Seq.empty, () => maze.snobot.ty),
                ("getGoalDistance", Seq("number", "number"), (x:Int, y:Int) => {
                  overlay.distanceMap.getOrElse(x -> y, Int.MaxValue)
                })
              )
            })
          }
        ))
      ),

      mdTask(
        """## Let's play games...
          |
          |In this exercise, we've taken away the location and goal distance functions, but we're still painting the
          |goal distance on the tiles. This is a fairly simple maze, but just that boulder in the path has stopped us
          |from working out the distance to the exit (our algorithm didn't know you can push them).
          |
          |Get to the exit just by going right five times and down five times... though you'll notice when we move
          |the boulder, suddenly the goal distance can count again.
          |""".stripMargin,
        setup = maze => {
          maze.addOverlay(new Overlay.FloodFill())
          maze.loadFromString(
            """
              |
              | S....O.
              |      .
              |      .
              |      .
              |      .
              |      G
              |
              |""".stripMargin)
        },
      )

    )),

    Level(name = "ASSIGNMENT", stages = Seq(

      VNodeStage.twoColumn("Your Assignment: Help Snobot Escape")(() => Common.markdown(
        s"""
           |Welcome to your escape assignment.
           |
           |There are eight levels, numbered 0 to 7. None of them require "flood fill" or anything that advanced, though there might be
           |some littler puzzles in them about how the pieces work. The challenges get you to use JavaScript programming
           |constructs, e.g. loops, conditionals, and defining functions.
           |
           |Each maze flips between two versions, so you can't *just* have a fixed list of directions to solve it.
           |Your solution to each maze will probably be some custom code, though. e.g. for the maze on the right,
           |go right while you can, then go down while you can.
           |
           |Remember: **Run your code against both versions of that exercise's maze** (i.e. run it twice to check it
           |works for both that level's mazes).
           |
           |Remember: `canGoRight()` will return `false` if there is a boulder to your right, even if you can push it.
           |The same is true for the other `canGo` functions.
           |
           |The only functions you have are:
           |
           |* `canGoLeft()`, `canGoRight()`, `canGoUp()`, `canGoDown()`
           |* `left()`, `right()`, `up()`, `down()`
           |
           |The rest is up to you...
           |
           |Again, don't forget:
           |
           |* **The maze cannot save your code** - copy and paste it regularly into a document you save
           |
           |* **The maze cannot submit your code** - if you are submitting these for course credit, you will need to
           |  use your school or university's submission system
           |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """while(canGoRight() || canGoDown()) {
            |  if (canGoRight()) {
            |    right()
            |  } else {
            |    down()
            |  }
            |}
            |""".stripMargin,
          {
            var parity = false
            smallMaze("Hello world", 8 -> 6, 8 -> 6, setup = { maze =>
              parity = !parity
              if (parity) {
                maze.loadFromString(
                  s"""
                     | Z.S...
                     |      *
                     |      1
                     |      G
                     |""".stripMargin)
              } else {
                maze.loadFromString(
                  s"""
                     | Z.S..
                     |     .*
                     |      1
                     |      G
                     |""".stripMargin)
              }
            })
          }
        ))
      ),
      {
        var parity = false
        mdTask(
          """## Exercise 0: Just go down
            |
            |Snobot just has to go down. But he also has to stop at the right point because he can roll past the goal.
            |
            |""".stripMargin,
          setup = maze => {
            parity = !parity
            maze.loadFromString(
              if (parity) {
                """
                  | S
                  | .
                  | .
                  | .
                  | .
                  | G
                  |""".stripMargin
              } else {
                """
                  | S
                  | .
                  | .
                  | .
                  | G
                  |""".stripMargin
              }
            )
          },
        )
      },
      {
        var parity = false
        mdTask(
          """## Exercise 1: Snakey maze
            |
            |This maze always goes right, down, doubles back on itself, and then doubles back to the exit
            |
            |""".stripMargin,
          setup = maze => {
            parity = !parity
            maze.loadFromString(
              if (parity) {
                """
                  | S......
                  |       .
                  |    ....
                  |    .
                  |    .
                  |    ....G
                  |""".stripMargin
              } else {
                """
                  | S.......
                  |        .
                  |        .
                  |  .......
                  |  .
                  |  ......G
                  |""".stripMargin
              }
            )
          },
        )
      },
      {
        var parity = false
        mdTask(
          """## Exercise 2: Boulder over
            |
            |There is a boulder between you and the exit. Remember, `canGoRight()` will return `false` if it sees a
            |boulder (even a pushable one)
            |
            |""".stripMargin,
          setup = maze => {
            parity = !parity
            maze.loadFromString(
              if (parity) {
                """
                  | ########
                  | ###..###
                  | #S..O..G#
                  | ###..###
                  | ########
                  | ########
                  |""".stripMargin
              } else {
                """
                  | ########
                  | ###...##
                  | #S...O.G#
                  | ###...##
                  | ########
                  | ########
                  |""".stripMargin
              }
            )
          },
        )
      },

      {
        var parity = false
        mdTask(
          """## Exercise 3: It is a Blob *Guard*
            |
            |There is a homing blob guard between you and the exit. Lead it a merry chase.
            |
            |If you need Snobot to pause, you might find `uptime()` useful. This function will give you the number of
            |milliseconds Snobot has been online for. You can read its value once into a constant and then write a loop
            |that calls uptime() until it's been long enough.
            |""".stripMargin,
          setup = maze => {
            parity = !parity
            maze.loadFromString(
              if (parity) {
                """
                  |##########
                  |##.*...###
                  |##.###.###
                  |S..###..ZG
                  |##.###.###
                  |##.1...###
                  |##########
                  |""".stripMargin
              } else {
                """
                  |##########
                  |#.*.....##
                  |#..####.##
                  |S.#####.ZG
                  |#..####.##
                  |#.1.....##
                  |##########
                  |""".stripMargin
              }
            )
          },
        )
      },

      {
        var parity = false
        mdTask(
          """## Exercise 4: Another Blob Guard puzzle
            |
            |There is a homing blob guard between you and the exit. Squash it with a boulder and then head to the
            |exit.
            |
            |Try to write your solution so it has three user-defined functions: `getInPosition()`, `waitAndSquash()`, and
            |`runToExit()`
            |
            |""".stripMargin,
          setup = maze => {
            parity = !parity
            maze.loadFromString(
              if (parity) {
                """
                  |###.######
                  |##.O...###
                  |##..##.###
                  |S...##..ZG
                  |##..##.###
                  |##.O...###
                  |###.######
                  |""".stripMargin
              } else {
                """
                  |###.######
                  |##..##.###
                  |##.O...###
                  |S...##..ZG
                  |##.O...###
                  |##..##.###
                  |###.######
                  |""".stripMargin
              }
            )
          },
        )
      },

    {
      var parity = false
      mdTask(
        """## Exercise 5: Operation boulder drop
          |
          |Each version of this maze has a boulder that wants to roll south. Diamonds and boulders are both slippery,
          |so if you make the space for it, it should roll off.
          |
          |Try to write your solution so it includes two functions you'll write: `dropBoulder()` and `runToExit()`
          |""".stripMargin,
        setup = maze => {
          parity = !parity
          maze.loadFromString(
            if (parity) {
              """   S
                | #...#
                | #.v.#
                | #***#
                | #2#2#
                | #.#.#
                | #.#.###
                | #...ZG#
                | #######
                |""".stripMargin
            } else {
              """   S
                | #...#
                | #...#
                | #.v.#
                | #*O*#
                | #1#1#
                | #.#.###
                | #....G#
                | #######
                |""".stripMargin
            }
          )
        },
      )
    },

    {
      var parity = false
      mdTask(
        """## Exercise 6: The diamond shuffle
          |
          |You'll need to collect all the diamonds to get through the gate. Try not to get trapped.
          |
          |Write a function that moves in a clockwise circle, and use it in your solution.
          |""".stripMargin,
        setup = maze => {
          parity = !parity
          maze.loadFromString(
            if (parity) {
              """
                |
                |###########
                |S..vvvv.5G#
                |..*****####
                |########
                |""".stripMargin
            } else {
              """
                |
                |###########
                |..Svvvv.5G#
                |..*****####
                |########
                |""".stripMargin
            }
          )
        },
      )
    },

      {
        var parity = false
        mdTask(
          """## Exercise 7: Spot the Difference
            |
            |In this one, the puzzle is not to block yourself. But, armed only with `canGoLeft()`, `canGoRight()`, etc.,
            |you're going to need to figure out which configuration the maze is so you'll know if you can keep the left
            |or right goals unblocked.
            |
            |Again, let's encourage you to define some of your own functions in your solution: `pushMiddleBoulder()`,
            |`collectDiamonds()`, and `runToExit()`. Keep a flag (a variable) for whether the maze is in the left or
            |right configuration, and either use it from inside the functions or pass it to them as a parameter.
            |""".stripMargin,
          setup = maze => {
            parity = !parity
            maze.loadFromString(
              if (parity) {
                """
                  | ####S####
                  | #v*...*v#
                  | #2.>1..2#
                  | #.......#
                  | .2#   #2.
                  | #G#   #G#
                  | ###   ###
                  |""".stripMargin
              } else {
                """
                  | ####S####
                  | #v*...*v#
                  | #2..1<.2#
                  | #.......#
                  | .2#   #2.
                  | #G#   #G#
                  | ###   ###
                  |""".stripMargin
              }
            )
          },
        )
      }

    ))
  )


  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(ChallengeRoute("lavaMaze", i, 0)),
    stagePath = (_, i, j) => Router.path(ChallengeRoute("lavaMaze", i, j)),
    homeIcon = <.span(),
    scaleToWindow = true
  )

}
