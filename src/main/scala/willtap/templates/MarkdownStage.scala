package willtap.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlContent}
import com.wbillingsley.veautiful.doctacular.Challenge
import org.scalajs.dom.{Element, Node}
import willtap.Common

case class MarkdownStage(left: () => String)(implicit val nextButton: () => VHtmlContent) extends Challenge.Stage {

  val leftContent = Common.markdown(left())

  override def completion: Challenge.Completion = Challenge.Open

  override def kind: String = "text"

  override protected def render = <.div(
    Challenge.textAndEx(<.div(leftContent, nextButton()))(<.div())
  )

}
