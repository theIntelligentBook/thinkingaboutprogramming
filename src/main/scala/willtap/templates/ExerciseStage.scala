package willtap.templates

import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.doctacular.Challenge.{Complete, Stage}

abstract class ExerciseStage() extends Stage {

  val kind = "exercise"

  def isComplete:Boolean = completion match {
    case Complete(_, _) => true
    case _ => false
  }

  def ocu() = {
    rerender()
  }

}
