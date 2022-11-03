package willtap.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import org.scalajs.dom.{Element, Node}
import willtap.Common

case class VNodeStage(n: () => VHtmlNode) extends Challenge.Stage {

  override def completion: Challenge.Completion = Challenge.Open

  override def kind: String = "text"

  override protected def render: DiffNode[Element, Node] = Challenge.textColumn(
    n(),
  )

}

object VNodeStage {

  def twoColumn(title:String)(left: () => VHtmlNode, right: () => VHtmlNode):VNodeStage = {
    VNodeStage(
      () => <.div(
        <.h2(title),
        Challenge.split(left())(right()),
      )
    )
  }

  def card(n:VHtmlNode) = <.div(^.cls := "card",
    <.div(^.cls := "card-body", n)
  )


}
