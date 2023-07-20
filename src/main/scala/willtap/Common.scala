package willtap

import com.wbillingsley.veautiful.html.{<, Markup, ^}
import com.wbillingsley.veautiful.doctacular.{Challenge, VSlides, DefaultVSlidesPlayer}
import willtap.async.AsyncProgramming
import willtap.control.{MicroRatTutorial, OpenAndClosedLoop, RescueLineTutorial, SensorsAndMotors, States}
import willtap.debugging.Debugging
import willtap.higherorder.HigherOrder
import willtap.imperativeTopic.{CommandsAndFunctions, ImpossibleThings, SnobotTutorial, TurtleTutorial}
import willtap.markup.MarkupLanguages
import willtap.nested.NestedStructures
import willtap.typesTopic.{ObjectTypes, PrimitiveTypes}
import com.wbillingsley.veautiful.doctacular.VideoPlayer

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("marked")
object Marked extends js.Object:
  def parse(s:String):String = js.native

given marked:Markup = new Markup({ (s:String) => Marked.parse(s).asInstanceOf[String] })

/**
  * Common UI components to all the views
  */
object Common {

  def markdown(s:String) = marked.Fixed(s)

  /** Circuits Up! Logo */
  def symbol = {
    <.span()
  }

  def downloadFromGitHub(project:String, user:String="UNEcosc250") = {
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

case class Echo360Video(videoId:String)
case class YouTubeVideo(videoId:String)

given VideoPlayer[Echo360Video] with
  extension (v:Echo360Video) def embeddedPlayer(width:Int, height:Int) =
    <.div(
      <("iframe")(
        ^.attr("height") := height, ^.attr("width") := width, ^.attr("allowfullscreen") := "true", ^.attr("frameborder") := "0",
        ^.src := s"https://echo360.org.au/media/${v.videoId}/public?autoplay=false&automute=false"
      )
    )

given VideoPlayer[YouTubeVideo] with
  extension (v:YouTubeVideo) def embeddedPlayer(width:Int = 560, height:Int = 315) =
    <.div(
      <("iframe")(
        ^.attr("height") := height, ^.attr("width") := width, ^.attr("allowfullscreen") := "true", ^.attr("frameborder") := "0",
        ^.attr("allow") := "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture",
        ^.src := s"https://www.youtube-nocookie.com/embed/${v.videoId}"
      )
    )
