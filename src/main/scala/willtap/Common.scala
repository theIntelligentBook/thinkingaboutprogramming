package willtap

import com.wbillingsley.veautiful.html.{<, Markup, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.{Challenge, VSlides, DefaultVSlidesPlayer}
import willtap.async.AsyncProgramming
import willtap.control.{MicroRatTutorial, OpenAndClosedLoop, RescueLineTutorial, SensorsAndMotors, States}
import willtap.debugging.Debugging
import willtap.higherorder.HigherOrder
import willtap.imperativeTopic.{CommandsAndFunctions, ImpossibleThings, SnobotTutorial, TurtleTutorial}
import willtap.markup.MarkupLanguages
import willtap.nested.NestedStructures
import willtap.typesTopic.{ObjectTypes, PrimitiveTypes}

import scala.collection.mutable
import scala.scalajs.js

/**
  * Common UI components to all the views
  */
object Common {

  val markdownGenerator = new Markup({ (s:String) => js.Dynamic.global.marked(s).asInstanceOf[String] })

  def markdown(s:String):VHtmlNode = markdownGenerator.Fixed(s)

  /** Circuits Up! Logo */
  def symbol = {
    <.span()
  }

  def downloadFromGitHub(project:String, user:String="UNEcosc250"):VHtmlNode = {
    <.a(
      ^.cls := "btn btn-secondary",
      ^.href := s"https://github.com/$user/$project/archive/master.zip",
      ^.attr("aria-label") := s"Download $project as zip",
      <("i")(^.cls := "material-con", "cloud_download"), "Download"
    )
  }

  def downloadGitHubStr(project:String, user:String="UNEcosc250"):String = {
    s"<a href='https://github.com/$user/$project/archive/master.zip' aria-label='Download $project as zip'>Download the project as a zip file</i></a>"
  }

  def cloneGitHubStr(project:String, user:String="UNEcosc250"):String = {
    s"`git clone https://github.com/$user/$project.git`"
  }

  val willCcBy:String =
    """
      |<p>Written by Will Billingsley</p>
      |
      |<a rel="license" href="http://creativecommons.org/licenses/by/3.0/au/">
      |  <img alt="Creative Commons Licence" style="border-width:0" src="https://i.creativecommons.org/l/by/3.0/au/88x31.png" /></a><br />
      |  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/au/">Creative Commons Attribution 3.0 Australia License</a>.
      |""".stripMargin

  /*
   * Slide decks
   */

}
