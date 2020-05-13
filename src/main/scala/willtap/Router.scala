package willtap

import com.wbillingsley.veautiful.PathDSL
import PathDSL.Compose._
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.HistoryRouter

sealed trait Route
case object IntroRoute extends Route

object Router extends HistoryRouter[Route] {

  var route:Route = IntroRoute

  def rerender() = renderElements(render())

  def render() = {
    route match {
      case IntroRoute => Intro.frontPage.layout
    }
  }

  override def path(route: Route): String = {

    route match {
      case IntroRoute => (/# / "").stringify
    }
  }


  def parseInt(s:String, or:Int):Int = {
    try {
      s.toInt
    } catch {
      case n:NumberFormatException => or
    }
  }
  override def routeFromLocation(): Route = PathDSL.hashPathArray() match {
    case Array("") => IntroRoute
    case x =>
      println(s"path was ${x}")
      IntroRoute
  }

}
