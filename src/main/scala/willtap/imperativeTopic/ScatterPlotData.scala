package willtap.imperativeTopic

import scala.collection.mutable

class ScatterPlotData(x: => Option[Double], y: => Option[Double]) {

  val data:mutable.Buffer[(Double, Double)] = mutable.Buffer.empty

  def update():Unit = {
    for {
      xx <- x; yy <- y
    } data.append(xx -> yy)
  }

  def dataValues():Seq[(Double, Double)] = data.toSeq
}
