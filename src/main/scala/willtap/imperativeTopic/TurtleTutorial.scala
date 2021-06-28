package willtap.imperativeTopic

import canvasland.{CanvasLand, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.{Complete, Level, Open}
import org.scalajs.dom.{Element, Node}
import willtap.templates.{ExerciseStage, MarkdownStage, VNodeStage}
import willtap.{Common, site}

object TurtleTutorial {

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

  def smallCanvas(s:String, vs:(Int, Int) = 320 -> 320, start:Option[(Int, Int)] = None) = {
    val (rx, ry) = start getOrElse (25, vs._2 - 25)

    CanvasLand(s)(
      viewSize = vs,
      r = Turtle(rx, ry),
      setup = (c) => {
        c.drawGrid("lightgray", 25)
      }
    )
  }

  val levels = Seq(
    Level(name = "Introduction", stages = Seq(
      VNodeStage.twoColumn("Turtle Graphics")(() => Common.markdown(
        s"""
          |Let's meet our turtle graphics environment.
          |
          |First, I'd better give you a quick rundown of the *functions* you can call in the environment:
          |
          |* `left(a)` turns the turtle left (anticlockwise) by *a* degrees.  \t
          |  `anticlockwise(r)` turns the turtle anticlockwise by *r* radians, if you prefer radians
          |
          |* `right(a)` turns the turtle right (clockwise) by *a* degrees.  \t
          |  `clockwise(r)` turns the turtle clockwise by *r* radians, if you prefer radians
          |
          |* `forward(d)` asks the turtle to move forward *d* pixels.  \t
          |  `back(d)` asks the turtle to move backward *d* pixels.  \t
          |  If the pen is down when the turtle moves forward or back, it'll leave a mark.
          |
          |* `setColour(s)` sets the pen's colour. It takes a <a target="_blank" href="https://developer.mozilla.org/en-US/docs/Web/CSS/color_value">CSS colour</a>.<br />
          |   e.g. `setColour("red")` or `setColour("#ff0000")` or `setColour("rgb(255, 0, 0)")`. You can even use pens with transparency, if you use
          |   `setColour("rgba(128, 64, 64, 0.5)")` - the last number in `rgba` is the "alpha" value, or how opaque you would like the pen to be.
          |   In any case, don't forget that the content of `setColour` is a string. i.e., it is always in quotes `"like this"`.
          |
          |* `penUp()` lifts the pen off the page, and `penDown()` puts it back down again. The pen will remember its colour while it is up.
          |
          |* `setThickness(r)` sets the radius of the pen.
          |
          |You can come back to this reference at any time, or you'll find the names of these functions under the "game" section in the tile tray.
          |
          |The canvas on the right has a pre-written program, but on many pages you'll be asked to write the program. I recommend copying and pasting
          |occasionally into a text editor, because the canvas won't save when you close the page.
          |
          |We can't auto-mark these exercises - you're going to have to see visually if it drew what you expected (and if not, think about why not). Don't forget, you
          |can insert `println("text")` statements in many places, to debug where your program is up to.
          |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """left(90)
            |forward(100)
            |right(180)
            |forward(50)
            |left(90)
            |forward(50)
            |right(90)
            |forward(50)
            |left(90)
            |penUp()
            |forward(50)
            |left(90)
            |penDown()
            |forward(50)
            |penUp()
            |forward(37)
            |penDown()
            |setThickness(12)
            |forward(1)
            |penUp()
            |right(135)
            |forward(140)
            |""".stripMargin,
          smallCanvas("Hello world", 640 -> 200)
        ))
      ),
      mdTask(s"""First of all, let's just draw a triangle. There are three sides to a triangle, so three times we're going to need to go `forward(100)`, then turn
                  |`left(120)`.
                  |
                  |Initially, just write out each command and get it drawing the triangle.
                  |Then, try changing your program to use a <a target="_blank" href="${site.router.path(site.DeckRoute("commandsAndFunctions", 18))}">while loop</a>.
                  |Then, try changing your program to use a <a target="_blank" href="${site.router.path(site.DeckRoute("commandsAndFunctions", 19))}">C-style for-loop</a>.
                  |
                  |If you need them, there are crib notes on the next page. You can go forward and back between exercises at any time using the navigation on the right.
                  |""".stripMargin),
      VNodeStage.twoColumn("Drawing Shapes")(
        () => Common.markdown(
          """
            |When we wrote the code out longhand, we might have had:
            |
            |```js
            |forward(100);
            |left(120);
            |forward(100);
            |left(120);
            |forward(100);
            |left(120);
            |```
            |
            |Turning it into a while loop might give us:
            |
            |```js
            |let i = 0;
            |while (i < 3) {
            |  forward(100);
            |  left(120);
            |  i++;
            |}
            |```
            |
            |Turning that into a for-loop would give us:
            |
            |```js
            |for (let i = 0; i < 3; i++) {
            |  forward(100);
            |  left(120);
            |}
            |```
            |
            |On the right hand side, we take our for-loop, and put it into a function so we can re-use it.
            |
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function triangle() {
            |  for (let i = 0; i < 3; i++) {
            |    forward(100);
            |    left(120);
            |  }
            |}
            |
            |triangle();
            |left(180);
            |triangle();
            |""".stripMargin,
          smallCanvas("bow tie", 640 -> 320, start = Some(320 -> 160))
        ))
      ),
      mdTask(
        """## A Playground
          |
          |For this canvas, there's not really a task. It's a moment to play with the code. See if you can draw a
          |robotic smiley face (probably with square eyes because we haven't done circles yet!)
          |""".stripMargin)
    )),
    Level("Functions", Seq(
      VNodeStage.twoColumn("Reusable code - functions")(
        () => Common.markdown(
          """
            |If we drew our shapes longhand, even using loops where we could, we would end up with very messy and
            |unreadable code that's hard for us to edit. "Spaghetti code".
            |
            |Instead, programmers like to define small reusable functions. Mostly, this is so we can make our own
            |lives easier - the shorter a function is, the less that can go wrong with it. If it's small and neat,
            |we can read it more easily, think about it more easily, and debug it more easily.
            |
            |When we know "triangle" works, we can just call it and don't have to worry about making a mistake they way
            |we might if we had to write the code out a second time.
            |
            |Let's start by taking our triangle function from the end of the previous level. At the moment, this just
            |draws triangles of length `100`. If I wanted a triangle of a different length, I'd be out of luck. That's
            |not so good. So, in your next task, you need to *parametrise* the `triangle` function so you can call
            |`triangle(d)` and it'll draw a triangle with sides of length `d`.
            |
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function triangle() {
            |  for (let i = 0; i < 3; i++) {
            |    forward(100);
            |    left(120);
            |  }
            |}
            |
            |triangle();
            |left(180);
            |triangle();
            |""".stripMargin,
          smallCanvas("bow tie", 640 -> 320, start = Some(320 -> 160))
        ))
      ),
      mdTask(
        """
          |Take the `triangle` function from the previous page and paste it into the coding area. Now modify the function
          |so that it takes an argument `d` for how long the sides of the triangle should be.
          |
          |See if you can draw a big triangle, and inside it draw an upside-down triangle that touches the mid-point of
          |each side. We've moved the start to the lower left to make a bit more space.
          |Again, there's a solution on the next page.
          |""".stripMargin, start = 50 -> 595),
      VNodeStage.twoColumn("Drawing two triangles")(
        () => Common.markdown(
          """The code on the right should draw our triangles for us. Though you'll notice I cheated, and turned the
            |triangle 60 degrees rather than upside down (but it still *looks* upside down...)
            |
            |You'll notice that the triangle inside makes it appear to split the triangle into four. We'll come back
            |to that a little later, when we talk about *recursion*.
            |
            |For the moment, though, your next challenge is going to be to add a second parameter for the number of
            |sides the shape should have. So that if you say `shape(4, 100)`, it'll draw a square with four sides.
            |
            |As a rule of thumb, when programmers look at the code of a function and they see a number other than `0` or
            |`1` (e.g., in `3` for the sides of the triangle, and `120` for the angle to turn), they think to themselves
            |"I wonder if that could be turned into a function parameter, or derived from a function parameter?"
            |
            |Think about how far you'll have to turn, remembering that after completing all *n* sides, you will end up
            |facing back the way you started.
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function triangle(d) {
            |  for (let i = 0; i < 3; i++) {
            |    forward(d);
            |    left(120);
            |  }
            |}
            |
            |triangle(200);
            |forward(100);
            |left(60);
            |triangle(100);
            |""".stripMargin,
          smallCanvas("two triangles", 640 -> 320)
        ))
      ),
      mdTask(
        """## A polygon function
          |
          |Modify the code you had before, so that you have a function called `shape` that lets you draw polygons with
          |`n` sides of length `d`.
          |
          |Try looping and drawing different shapes on top of each other with different thicknesses.
          |
          |""".stripMargin, start = 200 -> 575),
      VNodeStage.twoColumn("A polygon function")(
        () => Common.markdown(
          """The code on the right should draw our polyfons for us.
            |
            |In these short snippets of code, we've already:
            |
            |* called functions
            |* declared variables (loop variables)
            |* used two kinds of loops
            |* declared a function
            |* used function parameters
            |
            |Your next challenge is going to be a little harder to think about. What if we wanted to draw a circle?
            |We can do a loop going forward a bit, turning a bit, etc, until we end up back the way we are facing, but
            |what would be a nice way of parametrizing that. How could you write a circle function *you* would like to
            |call?
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function shape(n, d) {
            |  for (let i = 0; i < n; i++) {
            |    forward(d);
            |    left(360 / n);
            |  }
            |}
            |
            |// Now let's loop and call it for shapes of up to 12 sides
            |for (let i = 3; i < 13; i++) {
            |  shape(i, 100);
            |}
            |""".stripMargin,
          smallCanvas("two triangles", 640 -> 480, start=Some(200 -> 455))
        )),
      ),
    )),
    Level("Optional parameters", Seq(
      mdTask(
        """## A circle function
          |
          |Try to write a circle function. Maybe the parameter it should take is the radius of the circle?
          |
          |In JavaScript, the circumference of a circle is `2 * Math.PI * r`, and you'll go around the circumference in
          |360 degrees. (You'll need to use text for this one, because we don't have a `Math.PI` tile yet.)
          |
          |""".stripMargin, start = 200 -> 575),
      VNodeStage.twoColumn("A circle function")(
        () => Common.markdown(
          """The code on the right will draw a circle, but it's rather slow.
            |
            |We've also used the `const` keyword to define a constant. You could just use a variable (and not change it)
            |but it seemed a good moment to show the use of `const`. Using `const` for things that shouldn't change
            |during the function is good practice, because it tells other programmers you don't expect it to change.
            |
            |The reason for this is because of how my turtle environment works. Regardless of how fast he can *move* (the tick rate
            |of the animation), he can only *think* at a certain rate. The calls from your code into the turtle take
            |time, and there are two calls for every step of the circle.
            |
            |One trade-off we could make is that instead of doing 360 steps, each time turning 1 degree, we could do
            |(360 / *stepSize*) steps each time turning *stepSize* degrees. Then our circles could be faster but more
            |jagged if we want.
            |
            |In the next exercise, you're asked to make an *optional parameter* stepSize. If it is `undefined`, use `4`.
            |
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function circle(r) {
            |    penUp()
            |    forward(r)
            |    left(90)
            |    penDown()
            |
            |    const dist = 2 * Math.PI * r / 360;
            |
            |    for (let i = 0; i < 360; i++) {
            |        forward(dist)
            |        left(1)
            |    }
            |}
            |
            |circle(50)
            |""".stripMargin,
          smallCanvas("two triangles", 640 -> 240, start=Some(320 -> 120))
        )),
      ),
      mdTask(
        """## A circle function with a speed parameter
          |
          |Modify the circle function so it takes an *optional* second parameter for how many degrees of arc it should
          |draw at each step. If the value is `undefined`, use `4`.
          |
          |""".stripMargin, start = 200 -> 575),
      VNodeStage.twoColumn("A circle function with an optional step")(
        () => Common.markdown(
          """A little solution to this is on the right
            |
            |You'll notice that
            |
            |* We've compared `step` to the value `undefined` to see if it was passed or not. We used `===` for this.
            |  Triple-equals is one of the equality operators in JavaScript. We'll see how it differs from `==` (double-equals)
            |  when we look at *types* next time.
            |
            |* We've set a constant for the number of steps. This isn't perfect, because if the step size we choose isn't
            |  a factor of 360, we'll find we don't complete the circle correctly, but for this exercise we'll put up with
            |  that.
            |
            |If we run our cirle algorithm for different steps, we should see they become faster but more jagged for our
            |turtle to draw. That's really about our turtle rather than programming in general.
            |
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function circle(r, step) {
            |    if (step === undefined) {
            |        step = 4
            |    }
            |
            |    penUp()
            |    forward(r)
            |    left(90)
            |    penDown()
            |
            |    const dist = step * 2 * Math.PI * r / 360;
            |    const numSteps = 360 / step
            |    for (let i = 0; i < numSteps; i++) {
            |        forward(dist)
            |        left(step)
            |    }
            |}
            |
            |circle(50)
            |right(90); penUp(); forward(100); penDown()
            |circle(50, 15)
            |right(90); penUp(); forward(100); penDown()
            |circle(50, 30)
            |""".stripMargin,
          smallCanvas("two triangles", 640 -> 240, start=Some(120 -> 120)), codeStyle=Some("max-height: 480px;")
        )),
      ),
    )),
    Level("Calling yourself", Seq(
      VNodeStage.twoColumn("Functions that call themselves - Recursion")(
        () => Common.markdown(
          """So far, we've written some functions that call other functions such as `left(90)` and `forward(100)`.
            |
            |What if a function called itself? This is called *recursion* and it's surprisingly useful in computer science.
            |But can be hard to think about - it's notoriously one of the hardest topics for programmers to get their heads around.
            |So much so, in fact, that many experienced programmers find ways to avoid using recurson so their programs can be easier to think about.
            |
            |Fortunately, in this subject, you *don't* need to write any recursive code. But in the hope that *showing*
            |it happening will help it feel more familiar if it comes up in later subjects, let's animate some recursions for you and
            |just get you to play them and see it happening. If nothing else, it's an excuse for me to write some code to draw pretty patterns!
            |
            |On the right, we've got a `square` function that draws a square.
            |
            |We've also got a function `vanishingSquares` that draws a square, then calls itself again to draw another one... which will call itself again
            |to draw another one...
            |
            |Each time, it calls itself with a smaller value of `d`, so eventually it'll call itself with a `d` so small it won't draw the square.
            |
            |This last time it calls itself is called the *terminating case*. When we write code that uses recursion,
            |there should be one, otherwise it'd just keep going forever. Let's try that in the next slide
            |
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function square(d) {
            |    for (let i = 0; i < 4; i++) {
            |        left(90);
            |        forward(d);
            |    }
            |}
            |
            |function vanishingSquares(d) {
            |    if (d > 25) {
            |        square(d);
            |        left(15);
            |        vanishingSquares(0.75 * d);
            |    }
            |}
            |
            |setThickness(2)
            |vanishingSquares(100)
            |""".stripMargin,
          smallCanvas("vanishing squares", 640 -> 320, start=Some(320 -> 160)), codeStyle=Some("max-height: 480px;")
        )),
      ),
      mdTask(
        """## An infinite recursion!
          |
          |Copy and paste the code from the previous slide. Just change the condition in the if to `true`, and press play. Notice that
          |the turtle just keeps drawing and turning... You'll need to hit reset to stop it.
          |""".stripMargin),
      VNodeStage.twoColumn("Let's subdivide triangle")(
        () => Common.markdown(
          """Recursion can let us draw some repeating patterns using a very short amount of code  - though it's not
            |always easy to understand what's going on unless you are well practiced.
            |
            |First, let's show you something that *isn't* recursive. On the right, we have some code that draws a
            |triangle, and then draws three smaller triangles inside it.
            |
            |(We've called the function `sierpinski` because of what's going to happen in the *next* slide.)
            |""".stripMargin),
        () => VNodeStage.card(PrefabCodable(
          """function triangle(d) {
            |    for (let i = 0; i < 3; i++) {
            |        forward(d)
            |        left(120)
            |    }
            |}
            |
            |function sierpinski(d) {
            |    triangle(d)
            |    if (d > 20) {
            |        triangle(d/2)
            |        forward(d)
            |        left(120)
            |        triangle(d/2)
            |        forward(d)
            |        left(120)
            |        triangle(d/2)
            |        forward(d)
            |        left(120)
            |    }
            |}
            |
            |setThickness(1)
            |sierpinski(320)
            |""".stripMargin,
          smallCanvas("vanishing squares", 640 -> 320, start=Some(120 -> 300)), codeStyle=Some("max-height: 400px;")
        )),
      ),
      VNodeStage.twoColumn("Sierpinski's triangle")(
        () => <.div(
          Common.markdown(
          """Recursion can let us draw some repeating patterns using a very short amount of code - though it's not
            |always easy to understand what's going on unless you are well practiced. Let's stick with just
            |*seeing* it happen in this unit!
            |
            |Let's take the code from the previous slide, that drew a triangle and then drew three smaller triangles.
            |
            |But instead of drawing three smaller triangles, let's change it to draw three smaller *sierpinskis*.
            |
            |This changes the function it from the version on the left, to the version on the right:
            |""".stripMargin),
          Challenge.split(<.pre(
            """function sierpinski(d) {
              |    triangle(d)
              |    if (d > 20) {
              |        triangle(d/2)
              |        forward(d)
              |        left(120)
              |        triangle(d/2)
              |        forward(d)
              |        left(120)
              |        triangle(d/2)
              |        forward(d)
              |        left(120)
              |    }
              |}
              |""".stripMargin
          ))(<.pre(
            """function sierpinski(d) {
              |    triangle(d)
              |    if (d > 20) {
              |        sierpinski(d/2)
              |        forward(d)
              |        left(120)
              |        sierpinski(d/2)
              |        forward(d)
              |        left(120)
              |        sierpinski(d/2)
              |        forward(d)
              |        left(120)
              |    }
              |}""".stripMargin
          )),
          Common.markdown(
            """
              |It's now recursive, and will draw a recursive pattern that is what you get if you keep subdividing
              |a triangle into smaller and smaller triangles.
              |""".stripMargin)
        ),
        () => VNodeStage.card(PrefabCodable(
          """function triangle(d) {
            |    for (let i = 0; i < 3; i++) {
            |        forward(d)
            |        left(120)
            |    }
            |}
            |
            |function sierpinski(d) {
            |    triangle(d)
            |    if (d > 20) {
            |        sierpinski(d/2)
            |        forward(d)
            |        left(120)
            |        sierpinski(d/2)
            |        forward(d)
            |        left(120)
            |        sierpinski(d/2)
            |        forward(d)
            |        left(120)
            |    }
            |}
            |
            |setThickness(1)
            |sierpinski(320)
            |""".stripMargin,
          smallCanvas("vanishing squares", 640 -> 320, start=Some(120 -> 300)), codeStyle=Some("max-height: 400px;")
        )),
      ),
      VNodeStage.twoColumn("Hilbert's Curve")(
        () => <.div(
          Common.markdown(
          """Let's finish with something totally crazy. *Mutual recursion*, where two functions call each other.
            |We're just going to see how this can produce some very interesting patterns (although it is very hard
            |to trace what's going on in the code in your head)
            |
            |Below, we've got two functions, just called `a` and `b`. They call each other (several times), and they look somewhat like
            |mirror images of each other.
            |""".stripMargin),
          Challenge.split(
            <.pre(
              """function a(d) {
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
                |""".stripMargin
            )
          )(
            <.pre(
              """function b(d) {
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
                |""".stripMargin
            )
          ),
          Common.markdown(
            """And then we're going to call them.
              |
              |```js
              |let maxDepth = 6
              |a(1);
              |```
              |
              |This will produce a curve that will fill a space of any arbitrary
              |size (depending on what we set `maxDepth` to). It was invented by a mathematician, David Hilbert, and
              |how it works is quite complex.
              |
              |But for the moment, the take-home message is how quite small functions - we've just got `forward`, `left`,
              |and `right`, and less than a page of code - can produce incredibly complex patterns.
              |""".stripMargin),
        ),
        () => VNodeStage.card(PrefabCodable(
          """function a(d) {
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
            |function b(d) {
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
            |let maxDepth = 6
            |a(1);
            |""".stripMargin,
          smallCanvas("hilbert", 640 -> 400, start=Some(120 -> 380)), codeStyle=Some("max-height: 300px;")
        )),
      ),
    ))
  )

}
