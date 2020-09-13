package willtap.async

import coderunner.JSCodable
import com.wbillingsley.veautiful.html.<
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import lavamaze.Maze
import willtap.Common
import willtap.imperativeTopic.{LoggingPrefabCodable, PrefabCodable}

object AsyncProgramming {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Flows of Control
        |
        |Exceptions, promises, async and await
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## WARNING: Still being worked on
        |
        |(Some slides will be re-ordered and a few extra ones added in)
        |""".stripMargin)
    .markdownSlide(
      """## John Lewis Christmas advert 2011
        |
        |What do you do while you're waiting?
        |
        |<iframe src="https://player.vimeo.com/video/31909633?color=ffffff&title=0&byline=0&portrait=0" width="1280" height="720" frameborder="0" allow="autoplay; fullscreen" allowfullscreen></iframe>
        |<p><a href="https://vimeo.com/31909633">John Lewis - The Long Wait</a> from <a href="https://vimeo.com/blinkprods">Blink</a> on <a href="https://vimeo.com">Vimeo</a>.</p>
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Sometimes you really can't wait
          |
          |This maze introduces *Dogbot*, an indestructible robot dog for Snobot.
          |
          |Unfortunately, as it is, this maze looks unsolvable...
          |""".stripMargin),
      JSCodable(
        Maze("Dogbot is in the maze!")((8, 8), (8, 8)) { maze =>
          maze.loadFromString(
            """#####*##
              |#####.##
              |Z.S...1G
              |###.####
              |Z..d...#
              |########
              |""".stripMargin)
          maze.additionalFunctions = maze.dogbotFunctions
        }
      )(tilesMode = false, asyncify = true)
    ))
    .markdownSlide(
      """## Snobot and Dogbot
        |
        |If Snobot and Dogbot are different robots, surely we should be able to...
        |
        |* Ask them both to do something *at once*?
        |* Tell one to do something *when the other one's finished*?
        |
        |We ought to be able to instruct them asynchronously.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Game render loops
        |
        |In fact, every program we've written (in the Lava Maze, Rescue Line, and MicroRat) has had to do more than
        |one thing at once.
        |
        |The program isn't just *your code*. It's also *the game itself*. While Snobot is running its routine, the
        |game has to keep drawing (even if Snobot gets stuck in a loop)
        |
        |To do this, we have two *threads* going on:
        |
        |* The game is running in the page
        |* Your code is being run in a *WebWorker* (a background thread). When your code stops, the game doesn't.
        |* Whenever you called `canGoRight()`, `up()`, or any other function I gave you, these threads had to send
        |  messages between each other.
        |* When the game receives your message, the browser might be busy repainting the canvas. It might not get to it
        |  straight away.
        |
        |The game and the worker need a way of signalling to each other when they've got a response.
        |""".stripMargin)
    .markdownSlide(
      """## Promises... magical boxes
        |
        |Imagine we were programming Father Christmas as a magical present box...
        |
        |![Christmas present](images/async/lambdagift.png)
        |
        |Little children write to Santa and ask him for their favourite toy.
        |
        |* Until Christmas morning, if they look in the box, it'll be *empty*
        |* On Christmas night, Santa comes
        |* On Christmas morning, if they look in the box, it'll be *filled*
        |
        |Effectively, it's a state machine with two states: *empty*, and *filled with (something)*
        |""".stripMargin)
    .markdownSlide(
      """## Promises...
        |
        |Many programming languages use `Promises` as a means of making writing asynchronous code easier.
        |
        |* A Promise starts out empty
        |* It can be filled, with success or an error
        |
        |So we're going to need three things:
        |
        |1. The promise object
        |2. A function that'll resolve it successfully (fill the box)
        |3. A function that'll reject it (fill the box with an error)
        |
        |""".stripMargin)
    .markdownSlide(
      """## Promises - resolving and rejecting
        |
        |In ECMAScript, the `Promise` constructor gives you the two functions (as values) in a slightly obscure way.
        |To test this, I'm going to need to write some arcane-looking code:
        |
        |```js
        |function makeAPromise() {
        |  // A couple of variables to hang on to the resolve and reject functions in
        |  let resolve, reject
        |
        |  // When I call `new Promise`, I pass in two functions saying
        |  // what I want to do with the resolve and reject functions
        |  // Let's put them in local variables so we can return them
        |  let promise = new Promise(
        |    (res) => { resolve = res },
        |    (rej) => { reject = rej }
        |  );
        |
        |  // Now we can return the three things: a promise, its resolve function, and its reject function
        |  return [promise, resolve, reject]
        |}
        |
        |let [promise, resolve, reject] = makeAPromise()
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## On Christmas morn...
        |
        |Once we have the magical box, we can start talking about what we want to happen when it completes: `then` we
        |would like to unwrap it...
        |
        |```js
        |let [promise, resolve, reject] = makeAPromise()
        |
        |promise.then(
        |  (value) => { console.log(`I opened it to find ${value}` },
        |  (err) => { console.log(`Oh no, something went wrong: ${value}` },
        |)
        |```
        |
        |* `resolve("a jumper from my Mum")`
        |* `reject("I was on Santa's naughty list after all.")`
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## Turning off the lies...
          |
          |To make the communication between your program and the game work, we've had to turn *all your code*
          |asynchronous. We parsed your program into a tree, and ran through the tree modifying *every function definition*
          |and *every function call*.
          |
          |Let's turn that off...
          |""".stripMargin),
      JSCodable(
        Maze("Dogbot is in the maze!")((8, 8), (8, 8)) { maze =>
          maze.loadFromString(
            """#####*##
              |#####.##
              |Z.S...1G
              |###.####
              |Z..d...#
              |########
              |""".stripMargin)
          maze.additionalFunctions = maze.dogbotFunctions
        }
      )(tilesMode = false, asyncify = false)
    ))
    .markdownSlide(
      """## Async and Await
        |
        |Trying to write all our code using `then` is a miserable experience. More recent versions of ECMAScript
        |include two keywords to help: `async` and `await`.
        |
        |* If we declare a function to be `async` then instead of returning a value, it returns a `Promise`
        |* If we call a function using `await`, the compiler will treat the code afterwards as being in a `then`.
        |
        |Instead of
        |
        |```
        |right().then(
        |  (success) => { right() }
        |)
        |```
        |
        |We can just write
        |
        |```
        |await right()
        |await right()
        |```
        |""".stripMargin)
    .veautifulSlide(<.div(
      Common.markdown(
        """## How your code is processed in Will's "codables"
          |
          |When you've run code in the Lava Maze, to let you write synchronous code (even though the environment is
          |asynchronous), the "codables" convert it into code that uses `async` and `await` keywords. For example:
          |
          |""".stripMargin),
      Challenge.split(
        Common.markdown(
          """We converted...
            |
            |```js
            |function goRight(n) {
            |  for (let i = 0; i < n; i++) {
            |    right()
            |  }
            |}
            |
            |goRight(5)
            |while (canGoDown()) {
            |  down()
            |}
            |
            |```
            |""".stripMargin
      ))(
        Common.markdown(
        """...to
          |
          |```js
          |async function goRight(n) {
          |  for (let i = 0; i < n; i++) {
          |    await right();
          |  }
          |}
          |
          |await goRight(5);
          |while (await canGoDown()) {
          |  await down();
          |}
          |
          |```
          |""".stripMargin
      )),
      Common.markdown(
        """This has to be done, because `right()` and `canGoRight()` are themselves really `async` functions that send a
          |"remote procedure call" (RPC) from your script (Snobot's logic) to the game in the page.
          |
          |Notice that we can use `await` with values - for instance inside the `while` condition.
          |""".stripMargin
      )
    ))
    .markdownSlide(
      """## Error handling with `await`
        |
        |When we used promises raw, we were able to specify two conditions to `then`:
        |
        |```js
        |let [promise, resolve, reject] = makeAPromise()
        |
        |promise.then(
        |  (value) => { console.log(`I opened it to find ${value}` },
        |  (err) => { console.log(`Oh no, something went wrong: ${value}` },
        |)
        |```
        |
        |But `await` only gives us a single value. How do we say what to do if it fails?
        |
        |Using `await` on a promise that fails throws a runtime error. We can do two things with it:
        |
        |* We can `catch` the error
        |* We can let the async function we're in fail (handle the error further out)
        |""".stripMargin)
    .veautifulSlide(Challenge.split(
      Common.markdown(
        """## Catching errors
          |
          |Many languages (e.g. JavaScript, Java, Scala) have a syntax for catching "runtime errors" or "exceptions"
          |
          |In JavaScript, this is `try ... catch`
          |
          |On the right, we've turned the code synchronous again so that you can see how exceptions work.
          |Normally, asking Snobot to go down would cause an exception: `Snobot is blocked in that direction`.
          |
          |In this case, we've *caught* the error and told Snobot to recover by going right.
          |""".stripMargin)
    )(LoggingPrefabCodable(
      """try {
        |  down()
        |} catch (error) {
        |  println("Ok, let's go right instead")
        |  right()
        |}
        |println("And we finished seemingly without an error (because we caught it")
        |""".stripMargin,
      Maze("Dogbot is in the maze!")((8, 4), (8, 4)) { maze =>
        maze.loadFromString(
          """#####*##
            |#####.##
            |S.....1G
            |########
            |""".stripMargin)
        maze.additionalFunctions = maze.dogbotFunctions
      }, asyncify = true
    )
    ))
    .veautifulSlide(Challenge.split(
      Common.markdown(
        """## Propagating errors
          |
          |We can decide at what level of the program we *want* to handle the errors.
          |In the example on the right:
          |
          |1. When Snobot reaches the gate, the call to `right()` will produce a promise
          |that fails. (Snobot will reply that he's blocked in that direction.)
          |
          |2. The call is `await`ed, so it'll cause an error inside `keepGoingRight()`
          |
          |3. The error isn't caught inside `keepGoingRight()`, but `keepGoingRight` is an `async` function, so the
          |   promise it returns will be failed.
          |
          |4. The call to `keepGoingRight()` is `await`ed, so when that promise fails, it'll cause an error
          |
          |5. But this call was a `try ... catch`, letting Snobot recover from the error and start going up.
          |""".stripMargin)
    )(LoggingPrefabCodable(
        """async function keepGoingRight() {
          |    while (true) {
          |        await right()
          |    }
          |}
          |
          |try {
          |    await keepGoingRight()
          |} catch(err) {
          |    println("That's as far as I can go!")
          |}
          |
          |await up()
          |await up()
          |""".stripMargin,
        Maze("Dogbot is in the maze!")((8, 4), (8, 4)) { maze =>
          maze.loadFromString(
            """#####*##
              |#####.##
              |S.....1G
              |########
              |""".stripMargin)
          maze.additionalFunctions = maze.dogbotFunctions
        }, asyncify = false
      )
    ))
    .markdownSlide(Common.willCcBy).withClass("bottom")

  def deck = builder.renderNode


}
