package willtap.templates

import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge.{Complete, Stage}

abstract class ExerciseStage(using val onCompletionUpdate: () => Unit, val nextButton: () => VHtmlNode) extends Stage {

  val kind = "exercise"

  def isComplete:Boolean = completion match {
    case Complete(_, _) => true
    case _ => false
  }

  def ocu() = {
    rerender()
  }

}
