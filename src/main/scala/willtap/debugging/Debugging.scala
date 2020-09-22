package willtap.debugging

import coderunner.JSCodable
import com.wbillingsley.veautiful.html.<
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import lavamaze.Maze
import willtap.Common
import willtap.imperativeTopic.{LoggingPrefabCodable, PrefabCodable}

object Debugging {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Debugging
        |
        |Keeping things elementary, my dear Watson
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## Things will go wrong...
        |
        |Much of a programmer's time goes into debugging. You've probably already experienced that with your programs
        |in the challenges.
        |
        |Debugging is largely detective work:
        |
        |* Looking at the evidence (*what happened*)
        |
        |* Building a theory (*a mental model of how your code works*)
        |
        |* Testing your theory
        |
        |However, there are a lot of tools to help you
        |""".stripMargin)
    .markdownSlide(
      """## Logging
        |
        |At various stages, we've printed text out to the console so we can see what the value of a variable is.
        |
        |This isn't just something that beginners do. Professional programmers do this fairly often. However...
        |
        |* A print statement is always executed in your code (and clutters up the console)
        |
        |* Programmers often want to leave the print code in the program, but switch it on or off.
        |
        |This lets us do *logging*.
        |
        |Browsers natively support logging via the console we've just seen `console.log`, but there are also functions
        |that take a *log level*:
        |
        |* `console.trace("Something very minor")`
        |
        |* `console.info("Something more major, but it's not a problem")`
        |
        |* `console.warn("Warning, user is not logged in entering this code")`
        |
        |* `console.error("Something's gone wrong")`
        |
        |""".stripMargin)
    .markdownSlide(
      """## Beyond browser logging
        |
        |In JavaScript, any of our messages turn up in the browser console.
        |
        |In other languages, it's more common to have a programmable switch that will set the logging level, so that
        |messages "less important" than we're looking for won't show up.
        |
        |It's also common to prefix the message with the name of the class that left the message. For instance:
        |
        |```
        |Info com.wbillingsley.veautiful.PathDSL$Extract$PathStrParam 4:31:03 PM: Checking on PathStrParam(PathStrParam(PathString(deck,/#))), with List()
        |```
        |
        |After `Logger.setTrace()` we might see:
        |
        |```
        |Trace com.wbillingsley.veautiful.reconcilers.DefaultReconciler 4:37:30 PM: Left empty
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Logging libraries
        |
        |As projects get bigger, they start to import *libraries* - open source modules written by other developers.
        |
        |There are several logging libraries for JavaScript that can do more advanced things, e.g. log messages
        |over *transports*. Rather than just print the message, send it over the network
        |
        |Some logging libraries you might come across
        |
        |* [LogLevel](https://github.com/pimterry/loglevel)
        |
        |* [WinstonJS](https://github.com/winstonjs/winston)
        |
        |* [Log4js](https://stritti.github.io/log4js/)
        |
        |""".stripMargin)
    .markdownSlide(
      """## Stack traces
        |
        |Sometimes, a problem in your code will cause an error to be thrown. Or, you might want to throw one yourself:
        |
        |```js
        |let primes = []
        |
        |function isPrime(i) {
        |    if (typeof(i) !== "number") {
        |        throw new Error("isPrime called but it wasn't a number")
        |    }
        |
        |    // A number is prime if no smaller prime divides it without
        |    // leaving a remainder
        |    return primes.filter((x) => i % x ==0).length == 0
        |}
        |
        |function generatePrimes() {
        |   for (let i = 2; i < 1000; i++) {
        |       if (isPrime(i)) {
        |           primes.push(i)
        |       }
        |   }
        |}
        |
        |generatePrimes()
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Stack trace
        |
        |Let's break the code by starting from `"2"`. When the error is thrown, there is a *stack trace* that shows
        |the path by which the program reached the line where the error was:
        |
        |```
        |VM341:5 Uncaught Error: isPrime called but it wasn't a number
        |    at isPrime (<anonymous>:5:15)
        |    at generatePrimes (<anonymous>:3:12)
        |    at <anonymous>:1:1
        |```
        |
        |We can use this to navigate through the code by which we reached the error, to help build our hypothesis of
        |what went wrong.
        |""".stripMargin)
    .markdownSlide(
      """## Breakpoints and the Debugger
        |
        |Errors can cause our code to stop. But we can also *ask the browser* to pause our code.
        |
        |In the developer tools, if you open the *Sources* tab, it'll show you the JavaScript files that are loaded.
        |
        |If you click in the left gutter (whether the numers are), it'll set a *breakpoint*. These pause your program
        |so that you can inspect what's happening in the variables.
        |
        |When your code has hit a breakpoint you can also "step" the code to see it in action.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Advice
        |
        |Debugging tools are instruments that let you see into your code. They don't do the *debugging* for you however.
        |
        |To try to make it easier to narrow down where a bug is:
        |
        |* Make functions small. Smaller functions are easier to think about.
        |
        |* Make functions have a clear purpose. So that if you call them with some test data, you know what they should
        |  come out with.
        |
        |Will's adage: *Half the art of good program design is making life easy for yourself*.
        |
        |* If you don't like debugging long, complex, tangly code, *don't write long complex, tangly code*
        |
        |* Good naming is usually better than good commenting. A comment can be wrong!
        |
        |* Write-test-write-test-write-test-write-test. If you've only made a small change, you've got a better idea
        |  where your bug must be.
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")

  def deck = builder.renderNode


}
