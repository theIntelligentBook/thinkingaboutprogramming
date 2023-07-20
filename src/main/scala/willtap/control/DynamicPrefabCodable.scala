package willtap.control

import coderunner.{Codable, CodePlayControls, WorkerCodeRunner}
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlComponent, ^}
import org.scalajs.dom.{Element, Node}

case class DynamicPrefabCodable(name:String)(code: => String, codable:Codable, codeStyle:Option[String] = None, asyncify:Boolean = true) extends VHtmlComponent {

  private val codeRunner = new WorkerCodeRunner(
    ((for { (n, _, f) <- codable.functions() } yield n -> f).toMap),
    Map.empty, asyncify)

  val codePlayControls = CodePlayControls(codeRunner)(
    code,
    start = codable.start _,
    reset = codable.reset _,
  )

  override protected def render = <.div(
    codable.vnode,
    <.pre(^.attr("style") ?= codeStyle, code),
    codePlayControls
  )

}