package willtap.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlContent, ^}
import com.wbillingsley.veautiful.doctacular.Challenge
import org.scalajs.dom.{Element, Node}
import willtap.Common

case class VNodeStage(n: () => VHtmlContent) extends Challenge.Stage {

  override def completion: Challenge.Completion = Challenge.Open

  override def kind: String = "text"

  override protected def render = Challenge.textColumn(
    n(),
  )

}

object VNodeStage {

  def twoColumn(title:String)(left: () => VHtmlContent, right: () => VHtmlContent):VNodeStage = {
    VNodeStage(
      () => <.div(
        <.h2(title),
        Challenge.split(left())(right()),
      )
    )
  }

  def card(n:VHtmlContent) = <.div(^.cls := "card",
    <.div(^.cls := "card-body", n)
  )


}
