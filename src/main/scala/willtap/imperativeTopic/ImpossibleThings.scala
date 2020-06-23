package willtap.imperativeTopic

import com.wbillingsley.veautiful.{DiffNode, MutableArrayComponent}
import com.wbillingsley.veautiful.html.{<, SVG, VHtmlComponent, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder, ScatterPlot}
import org.scalajs.dom
import org.scalajs.dom.{Element, Node, html, svg}
import willtap.Common

import scala.collection.mutable

object ImpossibleThings {

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Making Impossible Things
        |
        |An intro to computational thinking
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """
        |## Let's run a physical experiment
        |
        |* This is a little experiment in *diffusion* that you can do at home.
        |  We're going to make up a clear jelly, put food colouring in the middle, and over a day we'll see it
        |  *diffuse* through the jelly. You will need:
        |
        |    * Boiling water
        |
        |    * Some clear or white dishes
        |
        |    * Gelatine
        |
        |    * Food colouring
        |
        |    * Something to make a hole in the gelatine
        |""".stripMargin)
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/diffusion-equipment.jpg"), <("figcaption")("Equipment")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-measurement.jpg"), <("figcaption")("Makes 500ml jelly")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-before-pouring.jpg"), <("figcaption")("Take it somewhere cool to pour")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-poured.jpg"), <("figcaption")("Pour jelly mix into petri dishes")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-red-added.jpg"),
        <.img(^.src := "images/makingimpossible/gelatine-green-added.jpg"),
        <("figcaption")("Pour jelly mix into petri dishes")
      )
    ).withClass("image-slide pp cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-fridge.jpg"), <("figcaption")("Put one of each in the fridge")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-garage.jpg"), <("figcaption")("Put one of each in the garage")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/gelatine-outside.jpg"), <("figcaption")("Leave one of each outside")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/later-fridge.jpg"), <("figcaption")("4 hours later... (fridge)")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/later-garage.jpg"), <("figcaption")("4 hours later... (garage)")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/later-outside.jpg"), <("figcaption")("4 hours later... (outside)")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/later2-fridge.jpg"), <("figcaption")("24 hours later... (fridge)")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/later2-garage.jpg"), <("figcaption")("24 hours later... (garage)")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/later2-outside.jpg"), <("figcaption")("24 hours later... (outside)")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """
        |## What's going on...
        |
        |* All the dye molecules start in the middle, but they're not stationary. They move randomly.
        |
        |* The longer you leave it, the further some of the molecules could have travelled. It starts to "diffuse"
        |  outwards into the jelly.
        |
        |* At higher temperatures, it should diffuse more (faster movement).
        |
        |* Smaller molecules should diffuse more easily (red diffused more than green).
        |
        |But...
        |""".stripMargin
    )
    .markdownSlide(
      """
        |## In a physical experiment, you have the whole of physics
        |
        |* Takes time
        |
        |* Green diffuses less than red
        |
        |* Need locations at a range of temperatures
        |
        |* Too hot: it melts; Too cold: it freezes
        |
        |What if we wanted to explore *just* how a model of random movement would create diffusion?
        |""".stripMargin
    )
    .markdownSlide(
      """
        |## Let's write a simulation!
        |
        |Just the parts of physics we choose to model - "impossible" in the real world
        |
        |It's not the same as an experiment - this is exploring *our model* of reality, not the real world - but we
        |can start to explore the effects of how we think things work.
        |""".stripMargin
    )
    .veautifulSlide(<.div(
      <.h2("Simulated diffusion"), Particles
    ))
    .veautifulSlide(<.div(
      <.h2("A lecture for seven year olds"),
      Challenge.split(
        Common.markdown(
          """Some years ago, we were asked to give a "university experience" to year 1 and 2 primary school students.
            |
            |* Some comedy involving clickers
            |
            |* An Angry Birds style sim, with three lines of code exposed
            |  <img src="images/makingimpossible/angry-birds.png" style="height: 400px; margin-bottom: 1rem;"/>
            |
            |* "Set it to a million!", "Make it like the moon!"
            |
            |""".stripMargin)
      )(
        <.img(^.src := "images/makingimpossible/seated.jpg", ^.attr("style") := "float: right;")
      )
    ))
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/void.jpg"), <("figcaption")("A playground for your creation...")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "images/makingimpossible/model.png"), <("figcaption")("Then our models can be much more fun...")
      )
    ).withClass("image-slide contain")
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode



  object Particles extends VHtmlComponent {

    val width = 640

    val height = 480

    val centre = Vec2(width / 2, height / 2)

    val boundaryRadius = 50

    private def resetParticles() = {
      for { i <- particles.indices } {
        if (i < 200) {
          particles(i) = Vec2.randomPolar(Vec2(width / 2, height / 2), 20) -> true
        } else {
          particles(i) = Vec2.randomCartesian(width, height) -> false
        }
      }
    }

    private val particles:Array[(Vec2, Boolean)] = {
      Array.fill(200)(Vec2.randomPolar(Vec2(width / 2, height / 2), 20) -> true) ++
        Array.fill(1000)(Vec2.randomCartesian(width, height) -> false)
    }

    val tableData:mutable.Buffer[(Double, Double)] = mutable.Buffer.empty

    private var animating = false

    private var heat = 5
    private var tick = 0

    def start() = {
      animating = true
      rerender()
      dom.window.requestAnimationFrame(_ => animationLoop())
    }

    def stop() = {
      animating = false
      rerender()
    }

    def reset() = {
      animating = false
      tick = 0
      resetParticles()
      rerender()
    }

    private def animationLoop():Unit = {
      tick += 1
      updateParticles()
      rerender()
      if (animating) dom.window.requestAnimationFrame(_ => animationLoop())
    }

    private def updateParticles():Unit = {
      for { i <- particles.indices } {
        val (p, track) = particles(i)
        particles(i) = (p + Vec2.randomDir(0.225 * heat)).boundWithin(Vec2(0, 0), Vec2(width, height)) -> track
      }

      // If we've reached tick 500, we need to record the result in the table
      if (tick == 500) {
        tableData.append(heat.toDouble -> particles.count {
          case (v, track) => track && (v - Vec2(width / 2, height / 2)).magnitude > boundaryRadius
        })
      }
    }

    private def outsideBoundary:Int = particles.count { case (p, track) =>
      track && (p - centre).magnitude > boundaryRadius
    }

    val particlePlot = new MutableArrayComponent[dom.Element, dom.Node, svg.Circle, (Vec2, Boolean)](
      SVG.g(^.cls := "particles"), particles
    )(
      onEnter = { case ((_, track), i) =>
        if (i < 200) {
          SVG.circle(^.cls := "molecule tracked", ^.attr("r") := "3")
        } else {
          SVG.circle(^.cls := "molecule ordinary", ^.attr("r") := "3")
        }
      },
      onUpdate = { case ((p, _), _, v) =>
        for { circle <- v.domNode } {
          circle.setAttribute("cx", p.x.toInt.toString)
          circle.setAttribute("cy", p.y.toInt.toString)
        }
      }
    )

    def simControls():VHtmlNode = {
      import com.wbillingsley.veautiful.html.EventMethods

      <.div(^.cls := "form-row",
        <.div(^.cls := "input-group col-md-5",
          <.div(^.cls := "input-group-prepend", <.span(^.cls := "input-group-text", "Heat")),
          <("input")(^.attr("type") := "number", ^.cls := "form-control",
            ^.prop("value") := heat.toString,
            if (tick > 0) {
              ^.attr("disabled") := "true"
            } else ^.attr("enabled") := "true",
            ^.on("input") ==> {
              event =>
                heat = event.inputValue match {
                  case Some(s) => Math.max(0, try {
                    s.toInt
                  } catch {
                    case _: Throwable => 0
                  })
                  case _ => 0
                }
            }
          ),
          if (animating) {
            <.div(^.cls := "input-group-append",
              <("button")(
                ^.cls := "btn btn-sm btn-secondary", ^.onClick --> stop(),
                <("i")(^.cls := "fa fa-pause")
              ),
              <("button")(
                ^.cls := "btn btn-sm btn-secondary", ^.attr("disabled") := "true", ^.onClick --> reset(), "Reset"
              )
            )
          } else {
            <.div(^.cls := "input-group-append",
              <("button")(
                ^.cls := "btn btn-sm btn-secondary", ^.onClick --> start(),
                <("i")(^.cls := "fa fa-play")
              ),
              <("button")(
                ^.cls := "btn btn-sm btn-secondary", ^.onClick --> reset(), "Reset"
              )
            )
          }
        )
      )
    }

    def simFooter():VHtmlNode = {
      <.p(s"Tick: $tick  Outside boundary: $outsideBoundary")
    }

    /** Graph the trials we've done so far */
    def experimentGraph:VHtmlNode = ScatterPlot(
      600, 300, "Heat", "Outside boundary",
      _.toString, _.toString, 30, 200
    ).plot(tableData.toSeq)

    /** Tabulate the trials we've done so far */
    def experimentTable:VHtmlNode = <.div(^.cls := "results",
      <.table(^.cls := "table table-bordered results-table",
        <.tbody(
          <.tr(
            <.th(^.attr("scope") := "row", "Trial"),
            tableData.indices.map { i => <.th(s"${i+1}")}
          ),
          <.tr(
            <.th(^.attr("scope") := "row", "Heat"),
            tableData.map({ case (heat, _) => <.td(heat.toString) })
          ),
          <.tr(
            <.th(^.attr("scope") := "row", "Outside"),
            tableData.map({ case (_, num) => <.td(num.toString) })
          )
        )
      )
    )

    override protected def render: DiffNode[Element, Node] = {

      <.div(
        Challenge.split(
          <.div(^.cls := "card",
            <.div(^.cls := "card-header", "Simulation"),
            <.div(^.cls := "card-image",
              <("link")(^.attr("rel") := "stylesheet", ^.href := "stylesheets/diffusion.css"),
              <.svg(^.attr("width") := 640, ^.attr("height") := 480,
                SVG.circle(^.attr("cx") := 320, ^.attr("cy") := 240, ^.attr("r") := boundaryRadius, ^.cls := "ring"),
                particlePlot
              )
            ),
            <.div(^.cls := "card-footer",
              simControls(), simFooter()
            )
          )
        )(<.div(^.cls := "card",
          <.div(^.cls := "card-header", "Results"),
          <.div(^.cls := "card-body",
            Particles.experimentTable, Particles.experimentGraph
          ),
          <.div(^.cls := "card-footer",
            <("button")(
              ^.cls := "btn btn-sm btn-secondary", ^.onClick --> { tableData.clear(); rerender() },
              "Clear plot data"
            )
          )
        ))
      )

    }
  }
}