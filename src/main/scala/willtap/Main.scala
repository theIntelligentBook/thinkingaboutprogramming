package willtap

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular._
import org.scalajs.dom
import willtap.imperativeTopic.{CommandsAndFunctions, ImpossibleThings, TurtleTutorial}
import Medium._
import willtap.async.AsyncProgramming
import willtap.control.{MicroRatTutorial, OpenAndClosedLoop, RescueLineTutorial, SensorsAndMotors, States}
import willtap.debugging.Debugging
import willtap.higherorder.HigherOrder
import willtap.markup.MarkupLanguages
import willtap.nested.NestedStructures
import willtap.typesTopic.{ObjectTypes, PrimitiveTypes}

/** The doctacular site */
val site = Site()

object Main {

  val scaleChallengesToWindow:Boolean = {
    !dom.window.location.search.contains("scale=off")
  }

  def main(args:Array[String]): Unit = {
    val n = dom.document.getElementById("render-here")
    n.innerHTML = ""

    site.toc = site.Toc(
      "Home" -> site.HomeRoute,

      "1. Commands and Functions" -> site.Toc(
        "Making impossible things" -> site.add("impossibleThings",
          Alternative("Slide deck", Deck(() => ImpossibleThings.deck)),
        ),
        "Commands and functions" -> site.add("commandsAndFunctions",
          Alternative("Slide deck", Deck(() => CommandsAndFunctions.deck)),
        ),
        "Tutorial: Turtle graphics" -> site.addChallenge("turtleGraphics", TurtleTutorial.levels),
      ),
      "2. Types" -> site.Toc(
        "Types" -> site.add("types",
          Alternative("Slide deck", Deck(() => PrimitiveTypes.deck)),
        ),
        "Reference Types" -> site.add("referenceTypes",
          Alternative("Slide deck", Deck(() => ObjectTypes.deck)),
        ),
        "Tutorial: Lava Maze" -> site.addChallenge("lavaMaze", TurtleTutorial.levels),
      ),
      "3. Robots & Control" -> site.Toc(
        "Open and Closed Loop" -> site.add("closedLoop",
          Alternative("Slide deck", Deck(() => OpenAndClosedLoop.deck))
        ),
        "Handling state" -> site.add("state",
          Alternative("Slide deck", Deck(() => States.deck))
        ),
        "Tutorial: Rescue Line" -> site.addChallenge("rescueLine", RescueLineTutorial.levels),
        "Handling state" -> site.add("sensorsAndMotors",
          Alternative("Slide deck", Deck(() => SensorsAndMotors.deck))
        ),
      ),
      "4. Little Data" -> site.Toc(
        "Functions as data" -> site.add("higherOrder",
          Alternative("Slide deck", Deck(() => HigherOrder.deck))
        ),
      ),
      "5. Markup Languages" -> site.Toc(
        "Markup Languages" -> site.add("markupLanguages",
          Alternative("Slide deck", Deck(() => MarkupLanguages.deck))
        ),
        "Tutorial: MicroRat" -> site.addChallenge("microRat", MicroRatTutorial.levels),
      ),
      "6. Nested Structures" -> site.Toc(
        "Nested structures" -> site.add("nestedStructures",
          Alternative("Slide deck", Deck(() => NestedStructures.deck))
        ),

      ),
      "7. Asynchronous Programming" -> site.Toc(
        "Async and promises" -> site.add("markupLanguages",
          Alternative("Slide deck", Deck(() => AsyncProgramming.deck))
        ),

      ),
      "8. Debugging" -> site.Toc(
        "Debugging" -> site.add("debugging",
          Alternative("Slide deck", Deck(() => Debugging.deck))
        ),

      )

    )

    site.home = () => site.renderPage(Intro.frontPage)
    site.attachTo(n)
  }

}
