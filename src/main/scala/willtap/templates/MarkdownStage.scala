package willtap.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlNode}
import com.wbillingsley.veautiful.templates.Challenge
import org.scalajs.dom.{Element, Node}
import willtap.Common

case class MarkdownStage(left: () => String)(implicit val nextButton: () => VHtmlNode) extends Challenge.Stage {

  val leftContent = Common.markdown(left())

  override def completion: Challenge.Completion = Challenge.Open

  override def kind: String = "text"

  override protected def render: DiffNode[Element, Node] = <.div(
    Challenge.textAndEx(<.div(leftContent, nextButton()))(<.div())
  )

}
