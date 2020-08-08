package willtap.higherorder

import canvasland.{CanvasLand, LineTurtle, Turtle}
import coderunner.JSCodable
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.DeckBuilder
import willtap.Common

object HigherOrder {

  val scilly = <.img(^.src := "images/control/scilly isles.jpg").create()

  private val builder = new DeckBuilder()
    .markdownSlide(
      """
        |# Passing Functions as Values
        |
        |Little Data
        |
        |""".stripMargin).withClass("center middle")
    .markdownSlide(
      """## Functions as values
        |
        |So far, when we've passed a function as a parameter or set it as a value, it has typically been as an
        |event handler
        |
        |```js
        |button.onclick = (evt) => {
        |  console.log(evt.target.value)
        |}
        |```
        |
        |Another common way this is used is for data processing.
        |
        |You might have heard of "big data"...
        |
        |""".stripMargin)
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "https://datacentermurals.withgoogle.com/static/assets/images/mural-cards/mayes.jpg"), <("figcaption")("datacentremurals.withgoogle.com")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "https://datacentermurals.withgoogle.com/static/assets/images/mural/ghislain/galleries/1/6.jpg"), <("figcaption")("datacentremurals.withgoogle.com")
      )
    ).withClass("image-slide cover")
    .veautifulSlide(
      <.div(^.cls := "wrapper",
        <.img(^.src := "https://datacentermurals.withgoogle.com/static/assets/images/mural-cards/dublin.jpg"), <("figcaption")("datacentremurals.withgoogle.com")
      )
    ).withClass("image-slide cover")
    .markdownSlide(
      """## Big Data
        |
        |When your data is spread across massive buildings on different continents, you can't just write a `for` loop over it.
        |
        |Instead, your program tends to be described in terms of processing stages that you would like to happen to the data.
        |
        |We can't immediately show "big data" in your first programming experience, but we can do "little data" -
        |some of the big data frameworks use techniques that we can apply just to little arrays in JavaScript.
        |
        |""".stripMargin)
    .markdownSlide(
      """## A Little Document...
        |
        |Let's start with the text of *Peter Rabbit*, by Beatrix Potter.
        |
        |["Peter Rabbit" at Project Gutenberg](https://www.gutenberg.org/files/14838/14838-h/14838-h.htm)
        |
        |Suppose we'd like to do some processing where we find out what different paragraphs are about.
        |
        |What we're going to try to produce is a *word vector* for each paragraph, showing which words occur.
        |Then, if we find paragraphs that contain the same words as each other, we're going to consider them as being
        |related. (e.g., all the paragraphs mentioning Mr McGregor)
        |
        |""".stripMargin)
    .markdownSlide(
      """## Let's just assume we have the text...
        |
        |```js
        |let peterRabbit = `
        |
        |Once upon a time there were four little Rabbits, and their names
        |were--
        |
        |          Flopsy,
        |       Mopsy,
        |   Cotton-tail,
        |and Peter.
        |
        |They lived with their Mother in a sand-bank, underneath the root of a
        |very big fir-tree.
        |
        |'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into
        |the fields or down the lane, but don't go into Mr. McGregor's garden:
        |your Father had an accident there; he was put in a pie by Mrs.
        |McGregor.'
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |'Now run along, and don't get into mischief. I am going out.'
        |
        |Then old Mrs. Rabbit took a basket and her umbrella, and went through
        |the wood to the baker's. She bought a loaf of brown bread and five
        |currant buns.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |Flopsy, Mopsy, and Cottontail, who were good little bunnies, went
        |down the lane to gather blackberries:
        |
        |But Peter, who was very naughty, ran straight away to Mr. McGregor's
        |garden, and squeezed under the gate!
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |First he ate some lettuces and some French beans; and then he ate
        |some radishes;
        |
        |And then, feeling rather sick, he went to look for some parsley.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |But round the end of a cucumber frame, whom should he meet but Mr.
        |McGregor!
        |
        |Mr. McGregor was on his hands and knees planting out young cabbages,
        |but he jumped up and ran after Peter, waving a rake and calling out,
        |'Stop thief!'
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |Peter was most dreadfully frightened; he rushed all over the garden,
        |for he had forgotten the way back to the gate.
        |
        |He lost one of his shoes among the cabbages, and the other shoe
        |amongst the potatoes.
        |
        |After losing them, he ran on four legs and went faster, so that I
        |think he might have got away altogether if he had not unfortunately
        |run into a gooseberry net, and got caught by the large buttons on his
        |jacket. It was a blue jacket with brass buttons, quite new.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |Peter gave himself up for lost, and shed big tears; but his sobs were
        |overheard by some friendly sparrows, who flew to him in great
        |excitement, and implored him to exert himself.
        |
        |Mr. McGregor came up with a sieve, which he intended to pop upon the
        |top of Peter; but Peter wriggled out just in time, leaving his jacket
        |behind him.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |And rushed into the tool-shed, and jumped into a can. It would have
        |been a beautiful thing to hide in, if it had not had so much water in it.
        |
        |Mr. McGregor was quite sure that Peter was somewhere in the
        |tool-shed, perhaps hidden underneath a flower-pot. He began to turn
        |them over carefully, looking under each.
        |
        |Presently Peter sneezed--'Kertyschoo!' Mr. McGregor was after him in
        |no time.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |And tried to put his foot upon Peter, who jumped out of a window,
        |upsetting three plants. The window was too small for Mr. McGregor, and
        |he was tired of running after Peter. He went back to his work.
        |
        |Peter sat down to rest; he was out of breath and trembling with
        |fright, and he had not the least idea which way to go. Also he was
        |very damp with sitting in that can.
        |
        |After a time he began to wander about, going lippity--lippity--not
        |very fast, and looking all round.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |He found a door in a wall; but it was locked, and there was no room
        |for a fat little rabbit to squeeze underneath.
        |
        |An old mouse was running in and out over the stone doorstep, carrying
        |peas and beans to her family in the wood. Peter asked her the way to
        |the gate, but she had such a large pea in her mouth that she could not
        |answer. She only shook her head at him. Peter began to cry.
        |
        |Then he tried to find his way straight across the garden, but he
        |became more and more puzzled. Presently, he came to a pond where Mr.
        |McGregor filled his water-cans. A white cat was staring at some
        |gold-fish, she sat very, very still, but now and then the tip of her
        |tail twitched as if it were alive. Peter thought it best to go away
        |without speaking to her; he had heard about cats from his cousin,
        |little Benjamin Bunny.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |He went back towards the tool-shed, but suddenly, quite close to him,
        |he heard the noise of a hoe--scr-r-ritch, scratch, scratch, scritch.
        |Peter scuttered underneath the bushes. But presently, as nothing
        |happened, he came out, and climbed upon a wheelbarrow and peeped over.
        |The first thing he saw was Mr. McGregor hoeing onions. His back was
        |turned towards Peter, and beyond him was the gate!
        |
        |Peter got down very quietly off the wheelbarrow; and started running
        |as fast as he could go, along a straight walk behind some
        |black-currant bushes.
        |
        |Mr. McGregor caught sight of him at the corner, but Peter did not
        |care. He slipped underneath the gate, and was safe at last in the wood
        |outside the garden.
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |Mr. McGregor hung up the little jacket and the shoes for a scare-crow
        |to frighten the blackbirds.
        |
        |Peter never stopped running or looked behind him till he got home to
        |the big fir-tree.
        |
        |He was so tired that he flopped down upon the nice soft sand on the
        |floor of the rabbit-hole and shut his eyes. His mother was busy
        |cooking; she wondered what he had done with his clothes. It was the
        |second little jacket and pair of shoes that Peter had lost in a
        |fortnight!
        |
        |[Illustration]
        |
        |I am sorry to say that Peter was not very well during the evening.
        |
        |His mother put him to bed, and made some camomile tea; and she gave a
        |dose of it to Peter!
        |
        |'One table-spoonful to be taken at bed-time.'
        |
        |[Illustration]
        |
        |[Illustration]
        |
        |But Flopsy, Mopsy, and Cotton-tail had bread and milk and
        |blackberries for supper.
        |
        |THE END
        |`
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Getting the paragraphs
        |
        |We've seen `string.split()` before, for taking a string and cutting it up into an array.
        |
        |This time, let's pass it a *regular expression* that will split the text wherever there is a line that contains
        |no "word" characters (letters or numbers)
        |
        |```js
        |let paragraphs = peterRabbit.split(/\n\W*\n/)
        |```
        |
        |We'll meet regular expressions in a future week, but
        |
        |* `/ /` rather than `" "` means this is a regular expression
        |* `\n\W*\n`, means *match a newline, followed by some number of non-word characters, followed by a newline*.
        |
        |This should give us an array, where for instance `paragraphs[4]` is:
        |
        |> 'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into the fields or down the lane, but don't go into Mr. McGregor's garden: your Father had an accident there; he was put in a pie by Mrs. McGregor.'
        |
        |""".stripMargin)
    .markdownSlide(
      """## Getting the words
        |
        |We're going to need to extract the words from the sentences. Let's put this in a function
        |
        |```js
        |function wordArray(paragraph) {
        |  // let's write this part
        |}
        |```
        |
        |I'll use `paragraphs[4]` as an example
        |""".stripMargin)
    .markdownSlide(
      """## Splitting the sentences
        |
        |First, let's split the paragraph on non-word characters
        |
        |```js
        |function wordArray(paragraph) {
        |  let words = paragraph.split(/\W/)
        |
        |  return words
        |}
        |```
        |
        |> 'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into the fields or down the lane, but don't go into Mr. McGregor's garden: your Father had an accident there; he was put in a pie by Mrs. McGregor.'
        |
        |Splits into this array:
        |
        |> ,Now,my,dears,,,said,old,Mrs,,Rabbit,one,morning,,,you,may,go,into,the,fields,or,down,the,lane,,but,don,t,go,into,Mr,,McGregor,s,garden,,your,Father,had,an,accident,there,,he,was,put,in,a,pie,by,Mrs,,McGregor,,
        |
        |Hmm, we've got a lot of empty strings, from where two non-word character followed each other. e.g. the punctionation in *Now my dears, said old Mrs. Rabbit"
        |""".stripMargin)
    .markdownSlide(
      """## Filter
        |
        |JavaScript arrays come with a `filter` function. This lets you ask the array to return a version of itself that only includes some values.
        |You pass it the filter function.
        |
        |Let's filter out the empty words
        |
        |```js
        |function wordArray(paragraph) {
        |  let words = paragraph.split(/\W/).filter((s) => s.length > 0)
        |
        |  return words
        |}
        |```
        |
        |> 'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into the fields or down the lane, but don't go into Mr. McGregor's garden: your Father had an accident there; he was put in a pie by Mrs. McGregor.'
        |
        |Split and filtered to individual words gives us:
        |
        |> Now,my,dears,said,old,Mrs,Rabbit,one,morning,you,may,go,into,the,fields,or,down,the,lane,but,don,t,go,into,Mr,McGregor,s,garden,your,Father,had,an,accident,there,he,was,put,in,a,pie,by,Mrs,McGregor
        |
        |We've got words with different capitalisations, but we'd probably like "Now" and "now" to be treated the same. Let's turn every word lowercase.
        |""".stripMargin)
    .markdownSlide(
      """## Map
        |
        |JavaScript arrays also come with a function called `map`. The name might not be very intuitive.
        |This takes a function and returns a new array where every element has been transformed by that function.
        |
        |```js
        |let arr = [ "HeLLo", "THerE" ]
        |arr.map((s) => s.toLowercase())
        |```
        |
        |would produce
        |
        |```js
        |[ "hello", "there" ]
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Let's turn our words to lowercase
        |
        |Inside our vector function, let's add a processing step where we turn all the words to lowercase.
        |
        |```js
        |function wordVector(paragraph) {
        |  let words = paragraph.split(/\W/)
        |    .filter((s) => s.length > 0)
        |    .map((s) => s.toLowerCase())
        |
        |  return words
        |}
        |```
        |
        |I've let the code spread onto multiple lines so you can see how we're adding processing steps one after another
        |
        |> 'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into the fields or down the lane, but don't go into Mr. McGregor's garden: your Father had an accident there; he was put in a pie by Mrs. McGregor.'
        |
        |would produce
        |
        |> now,my,dears,said,old,mrs,rabbit,one,morning,you,may,go,into,the,fields,or,down,the,lane,but,don,t,go,into,mr,mcgregor,s,garden,your,father,had,an,accident,there,he,was,put,in,a,pie,by,mrs,mcgregor
        |
        |""".stripMargin)

    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode



}
