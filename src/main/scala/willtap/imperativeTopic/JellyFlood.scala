package willtap.imperativeTopic

import com.wbillingsley.veautiful.{DiffComponent, DiffNode}
import com.wbillingsley.veautiful.html.{<, DHtmlComponent, ^}
import org.scalajs.dom.{Element, Node}

import scala.collection.mutable

case class JellyFlood(w:Int=8, h:Int=8, goalX:Int = 7, goalY:Int = 7, mazeString:Option[String] = None) extends DHtmlComponent {

  val maze = mutable.Map.empty[(Int, Int), Boolean]
  val distance = mutable.Map.empty[(Int, Int), Int]
  var tick = 0

  def setSquare(x:Int, y:Int, c:Char):Unit = c match {
    case '.' => maze((x, y)) = true
    case '#' => maze((x, y)) = false
    case _ => // do nothing
  }

  def loadFromString(s:String) = {
    for {
      (line, y) <- s.linesIterator.zipWithIndex if y < h
      (char, x) <- line.zipWithIndex if x < w
    } {
      setSquare(x, y, char)
    }
  }


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
    mazeString match {
      case Some(s) =>
        loadFromString(s)
      case _ =>
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
    }
    check((goalX, goalY), 0)
  }

  reset()

  override protected def render = <.div(
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
