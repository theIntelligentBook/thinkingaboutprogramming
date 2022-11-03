package willtap.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.Open
import org.scalajs.dom.{Element, Node}

def echo360embed(videoId:String, size:(Int, Int)) = {
  val (width, height) = size

  <.div(
    <("iframe")(
      ^.attr("height") := height, ^.attr("width") := width, ^.attr("allowfullscreen") := "true", ^.attr("frameborder") := "0",
      ^.src := s"https://echo360.org.au/media/$videoId/public?autoplay=false&automute=false"
    )
  )
}

case class Echo360Stage(videoId:String, size:(Int, Int) = (1600, 960), altLinks:Map[String, String] = Map.empty) extends Challenge.Stage  {

  override protected def render: DiffNode[Element, Node] = {
    val (width, height) = size

    <.div(
      <("iframe")(
        ^.attr("height") := height, ^.attr("width") := width, ^.attr("allowfullscreen") := "true", ^.attr("frameborder") := "0",
        ^.src := s"https://echo360.org.au/media/$videoId/public?autoplay=false&automute=false"
      ),

      for {
        (name, link) <- altLinks
      } yield {
        <.span(<.a(^.attr("target") := "_altPage", ^.href := link, name))
      }
    )
  }

  override def completion: Challenge.Completion = Open

  override def kind: String = "video"
}
