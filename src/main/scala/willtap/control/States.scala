package willtap.control

import canvasland.{CanvasLand, LineTurtle, LunarLanderSim, Turtle}
import coderunner.{JSCodable, LoggingPrefabCodable}
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlComponent, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import lavamaze.{BlobGuard, Boulder, Dogbot, Maze, Mob, Snobot}
import org.scalajs.dom.Event
import org.scalajs.dom.{Element, Node}
import willtap.Common
import willtap.imperativeTopic.PrefabCodable

import scala.util.Random

object States {

  class ShowingState(title:VHtmlNode, cardFilter: Mob => Boolean) {

    val maze = Maze()((10, 5), (10, 5)) { maze =>
      maze.loadFromString(
        """ #v..vv.#
          | #S****G#
          | #.#...##
          | #B..*..Z""".stripMargin)
      maze.additionalFunctions = maze.dogbotFunctions
    }

    private def stateCard(m:Mob) = m match {
      case s:Snobot =>
        <.div(^.cls := "card",
          <.div(^.cls := "card-header", "Snobot"),
          <.ul(^.cls := "list-group list-group-flush",
            <.li(^.cls := "list-group-item", ^.attr("style") := "margin-bottom: 0",
              s"x: ${s.px.toString}, y: ${s.py.toString}"
            ),
            <.li(^.cls := "list-group-item list-group-flush", ^.attr("style") := "margin-bottom: 0",
              s.action.stringify
            ),
            <.li(^.cls := "list-group-item list-group-flush",
              s.action.durationStringify
            )
          )
        )
      case s:BlobGuard =>
        <.div(^.cls := "card",
          <.div(^.cls := "card-header", "Blob Guard"),
          <.ul(^.cls := "list-group list-group-flush",
            <.li(^.cls := "list-group-item", ^.attr("style") := "margin-bottom: 0",
              s"x: ${s.px.toString}, y: ${s.py.toString}"
            ),
            <.li(^.cls := "list-group-item list-group-flush", ^.attr("style") := "margin-bottom: 0",
              s.action.stringify
            ),
            <.li(^.cls := "list-group-item list-group-flush",
              s.action.durationStringify
            )
          )
        )
      case s:Dogbot =>
        <.div(^.cls := "card",
          <.div(^.cls := "card-header", "Dogbot"),
          <.ul(^.cls := "list-group list-group-flush",
            <.li(^.cls := "list-group-item", ^.attr("style") := "margin-bottom: 0",
              s"x: ${s.px.toString}, y: ${s.py.toString}"
            ),
            <.li(^.cls := "list-group-item list-group-flush", ^.attr("style") := "margin-bottom: 0",
              s.action.stringify
            ),
            <.li(^.cls := "list-group-item list-group-flush",
              s.action.durationStringify
            )
          )
        )
      case s:Boulder =>
        <.div(^.cls := "card",
          <.div(^.cls := "card-header", "Boulder"),
          <.ul(^.cls := "list-group list-group-flush",
            <.li(^.cls := "list-group-item", ^.attr("style") := "margin-bottom: 0",
              s"x: ${s.px.toString}, y: ${s.py.toString}"
            ),
            <.li(^.cls := "list-group-item list-group-flush", ^.attr("style") := "margin-bottom: 0",
              s.action.stringify
            ),
            <.li(^.cls := "list-group-item list-group-flush",
              s.action.durationStringify
            )
          )
        )
      case _ =>
        <.span()
    }

    object StateItems extends VHtmlComponent {
      override def render: DiffNode[Element, Node] = <.div(
        for { m <- maze.allMobs.filter(cardFilter) } yield stateCard(m)
      )
    }

    maze.onTick = (_) => StateItems.rerender()

    val code =
      """while (canGoRight()) {
        |  right()
        |}
        |""".stripMargin

    val lfc = LoggingPrefabCodable(code, maze)

    def slide = <.div(
      title,
      Challenge.textAndEx(
        <.div(^.cls := "card-columns",
          StateItems
        )
      )(lfc)
    )
  }

  val lmAllStates = new ShowingState(Common.markdown(
    """## States *inside* the Lava Maze
      |
      |Rather than start with the states in your program, let's start with some of the states in mine. What's going on
      |inside the Lava Maze characters?
      |""".stripMargin), _ => true)
  val lmSnobot = new ShowingState(Common.markdown(
    """## Snobot
      |
      |Snobot has an `x` and `y` location. He also has an `action`. This action has its own state - how far through it
      |is. So, just in going right, there are 32 frames, in each of which Snobot's state changes.
      |""".stripMargin), { case _: Snobot => true; case _ => false })
  val lmBlobGuard = new ShowingState(Common.markdown(
    """## BlobGuards
      |
      |BlobGuards work very similarly, but this time the code for what they should do next is inside the game.
      |Many games have higher level states - monsters can be "on patrol" and then "chasing" when they catch sight of you.
      |In this case, though these Blob Guards always use the same strategy.
      |""".stripMargin), { case _: BlobGuard => true; case _ => false })
  val lmBoulder = new ShowingState(Common.markdown(
    """## Boulders
      |
      |In the Lava Maze, the Boulders are also mobs. In this case, the transition check on their next action depends on
      |looking at the maze around them. Particularly, would any of the other mobs block them from moving down, down-left,
      |or down-right? You'll notice they take fewer ticks to complete their actions.
      |""".stripMargin), { case _: Boulder => true; case _ => false })

  val introducingLander = new LunarLanderSim("lander")(onReset = { sim =>
    sim.world.gravity.y = 0.16

    sim.Lander.setPosition(1000, 500)
    sim.Lander.angle = Random.nextDouble() * 2 * Math.PI
  })

  val linearControl = new LunarLanderSim("lander", lz = (0, 640, 420))(onReset = { sim =>
    sim.world.gravity.y = 0.16
    sim.Lander.setPosition(1000, 500)
  })

  val bangBangControl = new LunarLanderSim("lander", lz = (0, 640, 420))(onReset = { sim =>
    sim.world.gravity.y = 0.16
    sim.Lander.setPosition(1000, 500)
  })

  val homingIn = new LunarLanderSim("lander")(onReset = { sim =>
    sim.world.gravity.y = 0.16
    sim.Lander.setPosition(4660 + 600 * Random.nextDouble(), 500)
  })

  val theChallenge = new LunarLanderSim("lander")(onReset = { sim =>
    sim.world.gravity.y = 0.16

    sim.Lander.setPosition(1000, 500)
    sim.Lander.angle = Random.nextDouble() * 2 * Math.PI
  })

  object NearEnough1 extends VHtmlComponent {

    val sim = new LunarLanderSim("lander")(onReset = { sim =>
      sim.world.gravity.y = 0.16
      sim.Lander.setPosition(1000, 500)
    })

    var hover:String = "0.096"
    var a:String = "1"
    var t:String = "25"
    var dist:String = "100"

    def updateHover(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      hover = e.inputValue getOrElse ""
      rerender()
    }

    def updateA(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      a = e.inputValue getOrElse ""
      rerender()
    }

    def updateT(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      t = e.inputValue getOrElse ""
      rerender()
    }

    def updateDist(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      dist = e.inputValue getOrElse ""
      rerender()
    }

    override def render = <.div(Challenge.textAndEx(
      <.div(
        Common.markdown(
          s"""## Getting close...
            |
            |When we're writing code for hobby robots or games, we might not use closed loop control all the time.
            |
            |If we're a long way out, we might start by making a rough and ready estimate that's going to get us into
            |the ballpark of where we want to be.
            |
            |Suppose we accelerate right for some number of `ticks` and then decelerate for `ticks`. Let's find a value for
            |`power` and `ticks` that will move us approximately 100.
            |
            |Then, if we want to move a bigger distance, say 1000, we could call it 10 times and it should get us
            |somewhere near.
            |
            |""".stripMargin),
        <.table(^.cls := "table",
          <.tr(
            <.th("hoverThrust"), <.td(<.input(^.on("input") ==> updateHover, ^.attr("size") := 6, ^.prop("value") := hover)),
            <.th("dist"), <.td(<.input(^.on("input") ==> updateDist, ^.attr("size") := 6, ^.prop("value") := dist)),
          ),
          <.tr(
            <.th("power"), <.td(<.input(^.on("input") ==> updateA, ^.attr("size") := 6, ^.prop("value") := a)),
            <.th("ticks"), <.td(<.input(^.on("input") ==> updateT, ^.attr("size") := 6, ^.prop("value") := t)),
          ),
        ),
      )
    )(DynamicPrefabCodable("nearEnough")(
      s"""showState()
        |function moveRight100() {
        |  setSideThrust(${a})
        |  wait(${t})
        |  setSideThrust(-${a})
        |  wait(${t})
        |  setSideThrust(0)
        |}
        |
        |function moveRight(dist) {
        |  for (let i = 0; i < dist / 100; i++) {
        |    moveRight100()
        |  }
        |}
        |
        |setThrust(${hover})
        |moveRight(${dist})
        |""".stripMargin, sim.canvasLand, codeStyle = Some("max-height: 360px; overflow-y: scroll;"))))
  }

  /** Dynamic slide, in which we search for parameters knowing that d = a * Math.pow(t, 2) / 2 */
  object NearEnough2 extends VHtmlComponent {

    val sim = new LunarLanderSim("lander")(onReset = { sim =>
      sim.world.gravity.y = 0.16
      sim.Lander.setPosition(1000, 500)
    })

    var power:String = "0.5"
    var a:String = "16.6"
    var b:String = "0.02"
    var dist:String = "3960"

    def updatePower(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      power = e.inputValue getOrElse ""
      rerender()
    }

    def updateA(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      a = e.inputValue getOrElse ""
      rerender()
    }

    def updateB(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      b = e.inputValue getOrElse ""
      rerender()
    }

    def updateDist(e:Event):Unit = {
      import com.wbillingsley.veautiful.html._
      dist = e.inputValue getOrElse ""
      rerender()
    }

    override def render = <.div(Challenge.textAndEx(
      <.div(
        Common.markdown(
          s"""## A cleverer guess...
             |
             |We could come up with a smarter parametrised function if we want: so we can accelerate to half-way
             |and then brake the rest of the way, rather than moving in 100 unit chunks.
             |
             |If we accelerate flat out from a stand still:  \t
             |`distance = 0.5 * acceleration * Math.pow(time, 2)`
             |
             |We need to solve for time, and end up with something that's going to look like
             |
             |`t = Math.sqrt(b * distance)`
             |
             |Fiddling with the numbers would be harder, but we could work them out if we knew what the acceleration
             |force is.
             |""".stripMargin),
        <.table(^.cls := "table",
          <.tr(
            <.th("dist"), <.td(<.input(^.attr("size") := "6", ^.on("input") ==> updateDist, ^.prop("value") := dist)),
            <.th("tickLength"), <.td(<.input(^.attr("size") := "6", ^.on("input") ==> updateA, ^.prop("value") := a)),
          ),
          <.tr(
            <.th("power"), <.td(<.input(^.attr("size") := "6", ^.on("input") ==> updatePower, ^.prop("value") := power)),
            <.th("b"), <.td(<.input(^.attr("size") := "6", ^.on("input") ==> updateB, ^.prop("value") := b)),
          ),
        ),
      )
    )(DynamicPrefabCodable("nearEnough")(
      s"""showState()
         |function moveRight(dist) {
         |  const t = Math.sqrt(${b} * dist)
         |  setSideThrust(${power})
         |  wait(t * ${a})
         |  setSideThrust(-${power})
         |  wait(t * ${a})
         |  setSideThrust(0)
         |}
         |
         |setThrust(0.096)
         |moveRight(${dist})
         |""".stripMargin, sim.canvasLand, codeStyle = Some("max-height: 360px; overflow-y: scroll;"))))
  }

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Handling state and state variables
        |
        |And playing lunar lander in code
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## Program design
        |
        |So far, we've met some functions and we've met some variables. We haven't talked a lot about *what to put
        |in your functions and variables*.
        |
        |That's partly because it varies from topic to topic, but there are some principles.
        |
        |When we design things *well* they become easier to think about.
        |
        |* Short, clear, meaningful functions are easier to debug, so we prefer to write short, clear, meaningful functions.
        |
        |* If the model in our code fits the problem well, it's usually easier to think about too.
        |
        |Computer programs are possibly the most complex thing mankind has ever invented. Generally, though, we are not
        |seeking to create complexity. We're looking at a complex problem and trying to find the simplicity in it.
        |
        |How can we take this hideously complex problem, and make it simple enough that even I can understand it on most
        |days (not just on my best day)?
        |""".stripMargin)
    .markdownSlide(
      s"""## Two sayings...
        |
        |These are more motivational than totally accurate, but...
        |
        |> Half the art of good programming is making life easy for yourself.  \t
        |> (The other half is making it easy for other people.) - *Will*
        |
        |and
        |
        |> Everything is easy.  \t
        |> (Otherwise the teacher wouldn't understand it) - *Prof John Billingsley*
        |
        |
        |""".stripMargin
    )
    .markdownSlide(
      """## State
        |
        |If we're going to model or program something, sometimes we will need to ask ourselves what *state* our model is in.
        |
        |* In a simulation, there may be some set of *variables* we need to describe our situation. Engineers call these the
        |  "state variables". Programmers might just call them the properties of an object.
        |
        |    * As your tea gets cold, the tea has a temperature and so does the room.
        |
        |* In other kinds of program, we might talk about something having a discrete set of states that change
        |  what it does.
        |
        |    * A website might behave differently if you are logged in or not logged in.
        |
        |    * A monster might behave differently if it's *asleep* than if it's *patrolling* or *hunting*
        |
        |In some of our programmable games we have a mix of both... Let's start by peeking under the hood
        |of the Lava Maze...
        |""".stripMargin)
    .veautifulSlide(lmAllStates.slide)
    .veautifulSlide(lmSnobot.slide)
    .veautifulSlide(lmBlobGuard.slide)
    .veautifulSlide(lmBoulder.slide)
    .markdownSlide(
      """## Writing control code...
        |
        |It's all well and good seeing what the states in the Lava Maze are, but *the code of the game itself* is too
        |big for our playful examples (and anyway, it's not written in JavaScript).
        |
        |Let's write some code to control a complex situation. Let's go land on the Moon...
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Introducing our lunar lander
          |
          |Here we have our lander. It has a main thruster and four attitude control jets (two on each side). We're
          |going to need to get it across the landscape to land on a small green target... but let's just play with it
          |first.
          |""".stripMargin),
      JSCodable(introducingLander.canvasLand)(tilesMode = false)
    ))
    .veautifulSlide(
      Challenge.textAndEx(
        Common.markdown(
          s"""## Proportional control
            |
            |We could control the descent of our lander with a mathematical function.
            |
            |We measure the *input*, `vy`, and work out how far out we are: our current *error*.  \t
            |`let err = getVy() - target`
            |
            |The power we send to the thrusters is proportional to that error  \t
            |`let thrust = p * err`
            |
            |We need to bound the output between 0 and 1. We can't apply negative thrust, and the thruster only goes
            |up to 1.
            |""".stripMargin)
      )(LoggingPrefabCodable(
        """showState()
          |
          |// Our thrust is proportional to how much we need to slow down
          |function control(vy) {
          |  const target = 2
          |  let err = getVy() - target
          |  let thrust = 0.9 * err
          |  return Math.max(0, Math.min(1, thrust))
          |}
          |
          |// Let it get too fast
          |wait(250)
          |
          |// Now control the descent
          |while (true) {
          |  let t = control(getVy())
          |  setThrust(t)
          |  println(t)
          |}
          |""".stripMargin, linearControl.canvasLand, codeStyle = Some("max-height: 240px; overflow-y: scroll;")))
    )
    .veautifulSlide(
      Challenge.textAndEx(
        Common.markdown(
          """## Bang Bang control
            |
            |One of our state variables is how fast we are descending: `vy`.
            |
            |However, we could decide to simplify that and think about "are we going too fast?"
            |
            |If we're going too fast, turn the thruster on and slow down. If we're not, turn it off.
            |
            |In engineering, this kind is called *on/off* control, or, sometimes
            |"bang bang" control. We never adjust the throttle - it's just fully on or fully off.
            |
            |As a programmer, though, you could think of it as defining your own higher-level derived state:
            |*too fast*.
            |""".stripMargin)
      )(PrefabCodable(
        """showState()
          |function tooFast() {
          |  return getVy() > 2
          |}
          |
          |// Let it get too fast
          |wait(250)
          |
          |while (true) {
          |  if (tooFast()) {
          |    setThrust(1)
          |  } else {
          |    setThrust(0)
          |  }
          |}
          |""".stripMargin, bangBangControl.canvasLand, codeStyle = Some("max-height: 360px; overflow: auto;")))
    )
    .veautifulSlide(NearEnough1)
    .veautifulSlide(NearEnough2)
    .veautifulSlide(
      Challenge.textAndEx(
        Common.markdown(
          """## Homing in
            |
            |After we've taken our big open loop guess, we'll be somewhere near where we want to be, but we will have
            |got it wrong to some extent.
            |
            |But we can let one of our control routines take over and home us in on where we need to be.
            |
            |It turns out, this is fairly similar to what humans do when we point with a mouse. First we move rapidly towards the
            |rough area of the target, then we watch the pointer as we adjust it onto the target.
            |
            |In the example on the right, the homing in function does a little trick where we consider "where we'll be
            |in a moment at our current velocity" rather than where we are now. This gives it some in-built damping.
            |""".stripMargin)
      )(PrefabCodable(
        """showState()
          |const target = 4960
          |
          |// Home in on the horizontal spot
          |function homeIn() {
          |  const power = 0.1
          |  if (getX() + 10 * getVx() > target) {
          |    setSideThrust(-power)
          |  } else {
          |    setSideThrust(power)
          |  }
          |}
          |
          |// Are we descending too fast?
          |function tooFast() {
          |  return getVy() > 5
          |}
          |
          |// Control our descent
          |function controlDescent() {
          |  if (tooFast()) {
          |    setThrust(1)
          |  } else {
          |    setThrust(0)
          |  }
          |}
          |
          |// Notice we're calling horizontal and
          |// vertical control functions every tick
          |while (true) {
          |  homeIn()
          |  controlDescent()
          |}
          |""".stripMargin, homingIn.canvasLand, codeStyle = Some("max-height: 360px; overflow: auto;")))
    )
    .markdownSlide(
      """## Stages in our strategy
        |
        |Already, it looks like there might be a couple of phases to our strategy
        |
        |1. Make a big but rough move where we want to be, while hovering
        |
        |2. Home in on the target while descending carefully
        |
        |But lets make things harder for ourselves... let's start with the ship not upright!
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Live coding our lunar lander
          |
          |This is a big and complex task, so let's live code a "simple" solution... it's quite a bit of code, but
          |at least it's broken down to be a little more meaningful.
          |""".stripMargin),
      JSCodable(theChallenge.canvasLand)(tilesMode = false)
    ))
    .markdownSlide(
      """## Our solution
        |
        |Our solution seemed to progress through some phases - effectively, a succession of states:
        |
        |1. Quickly spin around to *nearly* upright, before we fall too far. Because we don't want to use the main
        |   thruster until we know we're roughly upright.
        |
        |2. Use some feedback control to get ourselves steady, upright, and hovering
        |
        |3. Move rapidly sideways to roughly where we need to be
        |
        |4. Use some feedback control to steady ourselves again
        |
        |5. Home in on the target while controlling our descent
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode



}
