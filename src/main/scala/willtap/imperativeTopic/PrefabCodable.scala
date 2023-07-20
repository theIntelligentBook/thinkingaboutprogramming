package willtap.imperativeTopic

import coderunner.{Codable, CodePlayControls, WorkerCodeRunner}
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, DHtmlComponent, ^}
import org.scalajs.dom.{Element, Node}

case class PrefabCodable(code:String, codable:Codable, codeStyle:Option[String] = None, asyncify:Boolean = true) extends DHtmlComponent {

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
