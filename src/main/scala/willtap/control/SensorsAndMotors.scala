package willtap.control

import canvasland.{CanvasLand, LineTurtle, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.html.{<, VHtmlComponent, ^}
import com.wbillingsley.veautiful.templates.{Challenge, DeckBuilder}
import willtap.Common

import scala.util.Random

object SensorsAndMotors {

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
    .markdownSlide(
      """## H-Bridge motor drivers
        |
        | Still being written...
        |
        |
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode

}
