package willtap.markup

import com.wbillingsley.veautiful.templates.DeckBuilder
import willtap.Common
import willtap.control.OpenAndClosedLoop.builder

object MarkupLanguages {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Markup Languages
        |
        |Writing documents like code?
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## Peter Rabbit again...
        |
        |In the Project Gutenberg version of Peter Rabbit, there were several places where this happened:
        |
        |>
        |
        |
        |""".stripMargin)
    .markdownSlide(
      """## Still being written...
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")

  val deck = builder.renderNode


}
