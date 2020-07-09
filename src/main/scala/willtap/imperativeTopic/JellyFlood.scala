package willtap.imperativeTopic

import com.wbillingsley.veautiful.{DiffComponent, DiffNode}
import com.wbillingsley.veautiful.html.{<, VHtmlComponent, ^}
import org.scalajs.dom.{Element, Node}

import scala.collection.mutable

case class JellyFlood() extends VHtmlComponent {

  val w = 8
  val h = 8
  val maze = mutable.Map.empty[(Int, Int), Boolean]
  val distance = mutable.Map.empty[(Int, Int), Int]
  var tick = 0

  private def check(p:(Int, Int), dist:Int):Unit = {
    distance(p) = dist
    val (x, y) = p

    for {
      (dx, dy) <- Seq((x+1, y), (x-1, y), (x, y+1), (x, y-1)) if (
        distance.getOrElse((dx, dy), Int.MaxValue) > dist + 1 &&
          maze.getOrElse(p, false)
        )
    } check((dx, dy), dist + 1)
  }

  def reset(): Unit = {
    tick = 0
    maze.clear()
    distance.clear()
    for { x <- 0 to 1; y <- 0 to 1 } {
      maze((x,y)) = true
      maze((w - x - 1, h - y - 1)) = true
    }
    for { i <- 0 until 8 } {
      maze(4 -> i) = true
      maze(6 -> i) = true
      maze(i -> 4) = true
      maze(i -> 1) = true
    }
    check((w - 1, h - 1), 0)
  }

  reset()

  override protected def render: DiffNode[Element, Node] = <.div(
    <.div(^.cls := "jelly-grid",
      for {
        y <- 0 until h
      } yield <.div(^.cls := "jelly-row",
        for {
          x <- 0 until w
        } yield {
          val d =  distance.getOrElse(x -> y, Int.MaxValue)

          <.div(^.cls := "jelly-cell",
            if (maze.getOrElse(x -> y, false)) {
              if (tick > d) <.div(^.cls := "jelly", d.toString)
              else if (tick == d) <.div(^.cls := "jelly active", d.toString)
              else <.div(^.cls := "floor")
            } else <.div(^.cls := "lava")
          )
        }
      )
    ),
    <.div(^.cls := "btn-group",
      <.button(^.cls := "btn btn-outline-secondary", ^.onClick --> { tick = 0; rerender() }, "Reset"),
      <.button(^.cls := "btn btn-outline-primary", ^.onClick --> {
        tick = tick + 1
        rerender()
      }, "Step")
    )
  )
}
