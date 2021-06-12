package willtap

import com.wbillingsley.veautiful.PathDSL
import PathDSL.Extract._
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.{HistoryRouter, VSlides}

import scala.collection.mutable

sealed trait Route {
  def path:String
  def render: VHtmlNode
}
object Route {
  val introPath = /# / "home"
  val deckPath = /# / "deck" / stringParam / stringParam
  val challengePath = /# / "challenge" / stringParam / stringParam / stringParam
}

case object IntroRoute extends Route {
  val path = Route.introPath.mkString(start)
  def render = Intro.frontPage.layout
}
case class DeckRoute(name:String, page:Int) extends Route {
  val path = Route.deckPath.mkString((page.toString, (name, start)))
  def render = Common.showDeck(name, page)
}

case class ChallengeRoute(name:String, level:Int, stage:Int) extends Route {
  val path = Route.challengePath.mkString((stage.toString, (level.toString, (name, start))))
  def render = Common.showChallenge(name, level, stage)
}

object Router extends HistoryRouter[Route] {

  var route:Route = IntroRoute

  def rerender() = renderElements(render)

  def render = route.render

  override def path(route: Route): String = route.path

  def parseInt(s:String, or:Int):Int = {
    try {
      s.toInt
    } catch {
      case n:NumberFormatException => or
    }
  }
  override def routeFromLocation(): Route = PathDSL.hashPathList() match {
    case Route.introPath(start) => IntroRoute
    case Route.deckPath(((page, (name, _)), _))  => {
      val p = try { page.toInt } catch { case _:Throwable => 0 }
      DeckRoute(name, p)
    }
    case Route.deckPath(((name, _), _))  => {
      DeckRoute(name, 0)
    }
    case Route.challengePath(((stage, (level, (name, _))), _))  => {
      val l = try { level.toInt } catch { case _:Throwable => 0 }
      val s = try { stage.toInt } catch { case _:Throwable => 0 }
      ChallengeRoute(name, l, s)
    }
    case Route.challengePath(((level, (name, _)), _))  => {
      val p = try { level.toInt } catch { case _:Throwable => 0 }
      ChallengeRoute(name, p, 0)
    }
    case Route.challengePath(((name, _), _))  => {
      ChallengeRoute(name, 0, 0)
    }
    case x =>
      IntroRoute
  }

}
