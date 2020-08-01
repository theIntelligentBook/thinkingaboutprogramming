package willtap.control

import canvasland.{CanvasLand, LineTurtle, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, SVG, VHtmlComponent, VHtmlDiffNode, ^, EventMethods}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import com.wbillingsley.wren.Orientation.{East, North}
import com.wbillingsley.wren.{Circuit, Component, Constraint, ConstraintPropagator, Ground, Junction, LogicInput, NMOSSwitch, Terminal, VoltageSource, Wire}
import org.scalajs.dom
import org.scalajs.dom.{Element, Node}
import willtap.Common

import scala.scalajs.js
import scala.util.Random

object SensorsAndMotors {

  case class PWM() extends VHtmlComponent {

    private var input:Int = 0

    private val canvasWidth = 510
    private val canvasHeight = 155
    private val low = 150
    private val high = 5

    private val canvas = <.canvas(^.attr("width") := canvasWidth, ^.attr("height") := canvasHeight)

    private def paintCanvas() = for { c <- canvas.domNode } {
      val ctx = c.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

      ctx.clearRect(0, 0, canvasWidth, canvasHeight)
      ctx.save()
      ctx.setLineDash(js.Array(5d, 5d))

      val avg = input / 255d * (high - low) + low
      ctx.strokeStyle = "green"
      ctx.beginPath()
      ctx.moveTo(0, avg)
      ctx.lineTo(canvasWidth, avg)
      ctx.stroke()

      ctx.strokeStyle = "blue"
      ctx.beginPath()
      ctx.moveTo(0, low)
      ctx.lineTo(canvasWidth, low)
      ctx.stroke()
      ctx.strokeStyle = "red"
      ctx.beginPath()
      ctx.moveTo(0, high)
      ctx.lineTo(canvasWidth, high)
      ctx.stroke()
      ctx.restore()
      ctx.strokeStyle = "black"
      ctx.beginPath()
      if (input > 0) ctx.moveTo(0, high) else ctx.moveTo(0, low)
      for { x <- 0 to canvasWidth } {
        if (x % 255 < input) {
          ctx.lineTo(x, high)
        } else {
          ctx.lineTo(x, low)
        }
      }
      ctx.stroke()


    }


    override def render: DiffNode[Element, Node] = <.div(
      <.div(canvas),
      <.input(^.attr("type") := "range", ^.attr("min") := 0, ^.attr("max") := 255, ^.prop("value") := input.toString,
        ^.on("input") ==> { e =>
            try {
              e.inputValue.foreach(d => input = d.toInt)
              paintCanvas()
              rerender()
            } catch {
              case x:Throwable => // ignore
            }
        }
      ), <.span(input.toString)
    )

    override def afterAttach(): Unit = {
      super.afterAttach()
      paintCanvas()
    }

  }


  class DCMotor(pos:(Int, Int)) extends Component {

    val (x, y) = pos

    val width = 100

    val ta = new Terminal((x - width/2, y ), Some(0))
    val tb = new Terminal((x + width/2, y ), Some(0))

    override def terminals: Seq[Terminal] = Seq(ta, tb)

    override def constraints: Seq[Constraint] = ta.constraints ++ tb.constraints

    private def backArrow = SVG.path(^.attr("d") := "M 20 0 A 20 20 0 1 0 0 20 M -5 15 L 0 20 L -5 25")

    private def forwardArrow = SVG.path(^.attr("d") := "M 20 0 A 20 -20 0 1 1 0 -20 M -5 -15 L 0 -20 L -5 -25")

    private def coast = SVG.circle(^.attr("cx") := 0, ^.attr("cy") := 0, ^.attr("r") := 5)

    private val vt = 1d

    private def forward = (ta.potential - tb.potential).exists(_ > vt)
    private def backward = (tb.potential - ta.potential).exists(_ > vt)

    override def render: VHtmlDiffNode = SVG.g(^.cls := "wren-component logic-probe", ^.attr("transform") := s"translate($x, $y)",
      SVG.rect(^.attr("x") := -(width/2), ^.attr("y") := "-20", ^.attr("width") := width, ^.attr("height") := 40),
      SVG.circle(^.attr("cx") := 0, ^.attr("cy") := 0, ^.attr("r") := (width/2) - 10),
      if (forward) forwardArrow else if (backward) backArrow else coast,
    )
  }

  case class HBridge() extends VHtmlComponent {

    implicit val wireCol = Wire.voltageColoring
    implicit val nMosCol = NMOSSwitch.voltageColouring

    val vdd = new VoltageSource(400 -> 175, North, Some(5d))
    val gnd = new Ground(400, 340)

    val a1:LogicInput = new LogicInput(100 ->50, East, name="S1", initial = Some(false))({ v => a2.value = v.map(!_); onUpdate() })
    val b1:LogicInput = new LogicInput(300 ->50, East, name="S3", initial = Some(false))({ v => b2.value = v.map(!_); onUpdate() })
    val a2:LogicInput = new LogicInput(100 ->300, East, name="S2", initial = Some(false))({ v => a1.value = v.map(!_); onUpdate() })
    val b2:LogicInput = new LogicInput(300 ->300, East, name="S4", initial = Some(false))({ v => b1.value = v.map(!_); onUpdate() })


    val ltop = new NMOSSwitch(150 -> 50)
    val lbottom = new NMOSSwitch(150 -> 300)
    val rtop = new NMOSSwitch(350 -> 50)
    val rbottom = new NMOSSwitch(350 -> 300)

    val ta = Junction(ltop.source.x -> 175)
    val tb = Junction(rtop.source.x -> 175)

    val m = new DCMotor(250 -> 175)

    import Wire._
    val wires:Seq[Wire] = Seq(
      (a1.t -> ltop.gate).wire,
      (b1.t -> rtop.gate).wire,
      (a2.t -> lbottom.gate).wire,
      (b2.t -> rbottom.gate).wire,
      (ltop.source -> ta.terminal).wire,
      (ta.terminal -> lbottom.drain).wire,
      (ta.terminal -> m.ta).wire,
      (m.tb -> tb.terminal).wire,
      (rtop.source -> tb.terminal).wire,
      (rbottom.drain -> tb.terminal).wire,
      (lbottom.source -> rbottom.source).wire,
      (rbottom.source -> gnd.terminal).wire,
      (ltop.drain -> rtop.drain).wire,
      (rtop.drain -> vdd.t2).wireVia(vdd.t2.x -> rtop.drain.y),
      (gnd.terminal -> vdd.t1).wire,
    )

    val circuit = new Circuit(Seq(
      a1, a2, b1, b2, ltop, lbottom, rtop, rbottom, vdd, gnd, m
    ) ++ wires, 500, 400)
    val propagator = new ConstraintPropagator(circuit.components.flatMap(_.constraints))
    propagator.resolve()

    override def render: DiffNode[Element, Node] = <.div(
      <("style")(
        """.wren-canvas {
          |  font-size: 16px;
          |}
          |
          |.wren-component {
          |  fill: white;
          |}
          |""".stripMargin),
      circuit
    )

    def onUpdate():Unit = {
      propagator.clearCalculations()
      propagator.resolve()
      rerender()
    }
  }

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Sensors and Motors
        |
        |(Covid reduced version)
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## Microbit
        |
        |<div style="float: right">
        |<img src="https://pxt.azureedge.net/blob/3fbfadd2007554af832353d44822d23520f5ea98/static/mb/projects/soil-moisture/soil-moisture.jpg" />
        |<br /><a href="https://makecode.microbit.org/projects/soil-moisture">https://makecode.microbit.org/projects/soil-moisture</a>
        |</div>
        |
        |Approx $30 device with limited memory. Programmable in JavaScript
        |
        |* Just using an analog input, you can make a soil moisture probe.
        |
        |* *A to D converter* - convert an analog voltage level into a digital number.
        |
        |* For Micro:bit, this is `0` to `1023` (i.e. 10 bits)
        |
        |""".stripMargin)
    .markdownSlide(
      """## Microbit remote sensor
        |
        |<div style="float: right">
        |<img src="images/sensors/microbit radio soil.png" />
        |</div>
        |
        |Microbit also includes a radio, so one micro:bit can communicate with another
        |
        |This means you could set up a remote array of soil sensors (one in each pot) and get them to broadcast
        |back to base
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Code for a remote sensor"),
      Challenge.split(
        Common.markdown(
          """#### Sensor
            |
            |```js
            |let id = Math.randomRange(0, 40)
            |
            |basic.showString("Sensor " + id)
            |radio.setGroup(1)
            |radio.setTransmitPower(7)
            |
            |basic.forever(function () {
            |    let v = pins.analogReadPin(AnalogPin.P0)
            |    radio.sendNumber(v)
            |    led.plotBarGraph(v, 1023)
            |})
            |```
            |""".stripMargin)
      )(
        Common.markdown(
          """#### Receiver
            |
            |```js
            |let id = Math.randomRange(0, 40)
            |
            |basic.showString("Receiver")
            |
            |radio.setGroup(1)
            |radio.onReceivedNumber(function (receivedNumber: number) {
            |    led.plotBarGraph(receivedNumber, 1023)
            |})
            |
            |radio.onReceivedValue(function (id: string, receivedNumber: number) {
            |    led.plotBarGraph(receivedNumber, 1023)
            |    serial.writeValue(id, receivedNumber)
            |})
            |```
            |""".stripMargin)
      )
    ))
    .markdownSlide(
      """## Some other micro:bit sensors
        |
        |* Light
        |
        |  ```js
        |  input.onButtonPressed(Button.B, () => {
        |    let level = input.lightLevel()
        |    basic.showNumber(level)
        |  })
        |  ```
        |
        |* Temperature
        |
        |  ```js
        |  basic.forever(() => {
        |    let temp = input.temperature()
        |    basic.showNumber(temp)
        |  })
        |  ```
        |
        |* Accelerometer
        |
        |  ```js
        |  basic.forever(() => {
        |    led.plotBarGraph(input.acceleration(Dimension.X), 1023)
        |  })
        |  ```
        |
        |* Compass
        |
        |  ```js
        |  let degrees = input.compassHeading()
        |  ```
        |
        |""".stripMargin)
    .markdownSlide(
      """## TI SensorTag
        |
        |<div style="float: right">
        |<img src="https://www.ti.com/diagrams/med_tidc-cc2650stk-sensortag_1_main.gif" />
        |<br />
        |<a href="https://www.ti.com/tool/TIDC-CC2650STK-SENSORTAG">https://www.ti.com/tool/TIDC-CC2650STK-SENSORTAG</a>
        |</div>
        |
        |Approx $60. Contains the sorts of sensors you'd find in a phone
        |
        |* Accelerometer.
        |
        |* Compass
        |
        |* Light
        |
        |* Temperature, humidity, pressure
        |
        |Communicates wirelessly back to a device
        |
        |* Internet of Things
        |
        |* Could connect it into smart services...
        |
        |""".stripMargin)
    .markdownSlide(
      """## Edison
        |
        |<div style="float: right">
        |<img src="https://meetedison.com/wp-content/uploads/2015/05/LEGO-robotics-EdDigger.jpg" />
        |<br /><a href="https://meetedison.com/robot-activities/robot-builder/robot-digger/">https://meetedison.com/robot-activities/robot-builder/robot-digger/</a>
        |</div>
        |
        |Designed so you can construct things with it and LEGO.
        |
        |* Digger is two Edisons on top of each other (one for movement, one for the scoop)
        |
        |* Line sensor, range sensor, and clap sensor
        |
        |* Programmable via barcodes
        |
        |""".stripMargin)
    .markdownSlide(
      """## Distance Sensors (for Arduino, Raspberry Pi, Micro:Bit, etc)
        |
        |<div style="float: right">
        |<img src="https://core-electronics.com.au/media/catalog/product/cache/1/image/650x650/fe1bcd18654db18f328c2faaaf3c690a/d/e/device6_1000.jpg" />
        |</div>
        |
        |Approx $4 for a sensor
        |
        |* Put HIGH into the Trig pin for 10 microseconds to trigger a pulse
        |
        |* The sensor times how long the echo takes, and then sets the ECHO pin HIGH for however long it took to sense
        |  the echo.
        |
        |`(duration / 2) / 29.1` gives a distance in cm.
        |
        |Micro:bit code:
        |
        |```js
        |function distance () {
        |    pins.digitalWritePin(DigitalPin.P0, 0)
        |    control.waitMicros(2)
        |    pins.digitalWritePin(DigitalPin.P0, 1)
        |    control.waitMicros(10)
        |    pins.digitalWritePin(DigitalPin.P0, 0)
        |
        |    let time = pins.pulseIn(DigitalPin.P11, PulseValue.High)
        |    return Math.idiv(time, 58)
        |}
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Raspberry Pi Camera Module
        |
        |<div style="float: right">
        |<img src="https://projects-static.raspberrypi.org/projects/getting-started-with-picamera/daaa041609e59f61893be36888d21d5888bcc3fb/en/images/pi-camera-attached.jpg" />
        |<br />
        |<a href="https://projects.raspberrypi.org/en/projects/getting-started-with-picamera">https://projects.raspberrypi.org/en/projects/getting-started-with-picamera</a>
        |</div>
        |
        |Approx $45.
        |
        |Usable as a video or still camera
        |
        |Captures images that can then be put through image processing libraries (e.g. object detection)
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("Motors"),
      Challenge.split(
        Common.markdown(
          """### Stepper motors
            |
            |The input activates magnets, attracting the rotor to move to a particular position.
            |
            |Cycle the inputs rapidly to move.
            |
            |![Stepper motor](images/sensors/stepper.png)
            |
            |Slow, but sometimes used when you want to control the *position* rather than the *speed* of a motor.
            |
            |Still subject to wheel-slip
            |
            |""".stripMargin)
      )(Common.markdown(
        """### DC motors
          |
          |The input magnetises the rotor, causing it to move. As it passes a midway point, the polarity swaps
          |because of the brushes to make it keep spinning.
          |
          |Apply a voltage to control the speed and direction.
          |
          |![Brushed DC motor](images/sensors/dcmotor.png)
          |
          |Fast, but inaccurate for positioning.
          |
          |""".stripMargin
      ))
    ))
    .markdownSlide(
      """## Motor control
        |
        |Most microcontroller boards have *General Purpose IO* pins. Often these can be switched (in code) between
        |
        |* analogue input
        |
        |* digital input
        |
        |* digital output
        |
        |To control a DC motor, we've got two problems:
        |
        |* We've only got DC output
        |
        |* The microcontroller's outputs can't produce enough Amps to drive the motor
        |
        |So, robots need a *motor driver*
        |
        |""".stripMargin)
    .veautifulSlide(<.div(
      <.h2("H Bridge Motor Driver"),
      Challenge.split(
        Common.markdown(
          """
            |The motor driver takes our digital outputs and uses them to let us drive a DC motor in a forward or
            |backward direction.
            |
            |This switches the motor between running *at full speed* forwards or backwards.
            |
            |To do speed control, we're going to need to add *pulse-width modulation*, where we pulse the output
            |so it is being driven for a period in proportion to how fast we want the motor to run.
            |
            |""".stripMargin)
      )(
        <.h3("H Bridge"),
        HBridge(),
        <.h3("Pulse-width modulation"),
        PWM()
      )
    ))
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode

}
