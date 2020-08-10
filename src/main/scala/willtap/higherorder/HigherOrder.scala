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
      """## Little Data
        |
        |Mostly, this is going to be a demonstration of a different style of programming that's used in data. But
        |there are three methods to know about that we'll meet on arrays:
        |
        |* `filter(f)` - to produce a new array containing only *some* of the elements of the original array.
        |
        |  ```js
        |  [1, 2, 3, 4, 5].filter((i) => i % 2 == 0) // [2, 4]
        |  ```
        |
        |* `map(f)` - to produce a new array of elements that have been transformed somehow
        |
        |  ```js
        |  [1, 2, 3, 4, 5].map((i) => i * 2)) // [2, 4, 6, 8, 10]
        |  ```
        |
        |* `reduce(f)` - to progressively combine elements from an array into a single result.
        |
        |  ```js
        |  [1, 2, 3, 4, 5].reduce((a, b) => a + b, 0) // 15
        |  ```
        |
        |Each of these takes a function as an argument - the function they will apply to your array.
        |
        |We'll do the code for this in CodeSandbox. The finished version is available here:
        |
        |[https://codesandbox.io/s/little-data-peter-rabbit-demo1-r5nym](https://codesandbox.io/s/little-data-peter-rabbit-demo1-r5nym)
        |""".stripMargin)
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
        |What we're going to try to produce is a *vector* for each paragraph, showing which words occur.
        |Then, if we find paragraphs that contain the same words as each other, we're going to consider them as being
        |related. (e.g., all the paragraphs mentioning Mr McGregor)
        |
        |""".stripMargin)
    .markdownSlide(
      """## Let's just assume we have the text...
        |
        |```js
        |let peterRabbit = `
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
        |I'll use `paragraphs[3]` as an example
        |
        |> 'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into
        |> the fields or down the lane, but don't go into Mr. McGregor's garden:
        |> your Father had an accident there; he was put in a pie by Mrs.
        |> McGregor.'
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
        |```js
        |["","Now","my","dears","","","said","old","Mrs","","Rabbit","one","morning","","","you","may","go","into","the","fields","or","down","the","lane","","but","don","t","go","into","Mr","","McGregor","s","garden","","your","Father","had","an","accident","there","","he","was","put","in","a","pie","by","Mrs","","McGregor","",""]
        |```
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
        |```js
        |["Now","my","dears","said","old","Mrs","Rabbit","one","morning","you","may","go","into","the","fields","or","down","the","lane","but","don","t","go","into","Mr","McGregor","s","garden","your","Father","had","an","accident","there","he","was","put","in","a","pie","by","Mrs","McGregor"]
        |```
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
        |```js
        |["now","my","dears","said","old","mrs","rabbit","one","morning","you","may","go","into","the","fields","or","down","the","lane","but","don","t","go","into","mr","mcgregor","s","garden","your","father","had","an","accident","there","he","was","put","in","a","pie","by","mrs","mcgregor"]
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Emitting words
        |
        |At the moment, our words are in a flat array in the order they appear. Some words might be repeated
        |(e.g. "mcgregor" appears twice).
        |
        |To make the bags of words from each paragraph more comparable, let's turn them into objects, where the key is
        |the word appears and the value is how often it appears. e.g.,
        |
        |```js
        |{ "now": 1, "mcgregor": 2 ... }
        |```
        |
        |To start, let's just turn each word in the array to a `{ word: 1 }` object. Then we'll have them in a format
        |we can combine. This function can convert a word into an object
        |
        |```js
        |function wordToObj(w) {
        |   let obj = {}
        |   obj[w] = 1
        |   return obj;
        |}
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## An array of word objects
        |
        |Back inside our vector function, let's call our `wordToObj` function.
        |
        |```js
        |function wordVector(paragraph) {
        |  let words = paragraph.split(/\W/)
        |    .filter((s) => s.length > 0)
        |    .map((s) => s.toLowerCase())
        |    .map((w) => wordToObj(w))
        |
        |  return words
        |}
        |```
        |
        |This now gives us:
        |
        |```js
        |[{"now":1},{"my":1},{"dears":1},{"said":1},{"old":1},{"mrs":1},{"rabbit":1},{"one":1},{"morning":1},{"you":1},{"may":1},{"go":1},{"into":1},{"the":1},{"fields":1},{"or":1},{"down":1},{"the":1},{"lane":1},{"but":1},{"don":1},{"t":1},{"go":1},{"into":1},{"mr":1},{"mcgregor":1},{"s":1},{"garden":1},{"your":1},{"father":1},{"had":1},{"an":1},{"accident":1},{"there":1},{"he":1},{"was":1},{"put":1},{"in":1},{"a":1},{"pie":1},{"by":1},{"mrs":1},{"mcgregor":1}]
        |```
        |
        |Our next job is to *reduce* that array of word counts into a single object containing the counts of the words.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Reduce
        |
        |`Array.reduce` is a harder function to get your head around. You give it a function that takes two elements
        |from an array and combines them into one.
        |
        |It then keeps calling your function, combining elements, then combining the results, then combining the
        |results of the results, until you finish up with one object.
        |
        |e.g.
        |
        |```js
        |let myArr = [1, 2, 3, 4, 5, 6, 7]
        |let total = myArr.reduce((a, b) => { return a + b }, 0) // 28
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Adding wordcounts
        |
        |We're going to need a function that can combine two wordCount objects. This should do it:
        |
        |```js
        |function combine(a, b) {
        |  let result = {}
        |
        |  // Clone a
        |  for (key in a) {
        |    result[key] = a[key]
        |  }
        |
        |  // Add elements from b that coincide; just copy elements from b that don't
        |  for (key in b) {
        |    if (result[key] === undefined) {
        |      result[key] = b[key]
        |    } else {
        |      result[key] = result[key] + b[key]
        |    }
        |  }
        |
        |  return result
        |}
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Spread operator as an easy clone
        |
        |Since 2018, JavaScript has had a *spread* operator that can make our clone of a shorter.
        |
        |```js
        |function combine(a, b) {
        |  let result = {}
        |
        |  // Clone a using the spread operator
        |  let result = { ...a }
        |
        |  // Add elements from b that coincide; just copy elements from b that don't
        |  for (key in b) {
        |    if (result[key] === undefined) {
        |      result[key] = b[key]
        |    } else {
        |      result[key] = result[key] + b[key]
        |    }
        |  }
        |
        |  return result
        |}
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Updating our vector function
        |
        |We can now *reduce* our array of word objects using our *combine* function
        |
        |```js
        |function wordVector(paragraph) {
        |  let words = paragraph.split(/\W/)
        |    .filter((s) => s.length > 0)
        |    .map((s) => s.toLowerCase())
        |    .map((w) => wordToObj(w))
        |    .reduce((a, b) => combine(a, b), {})
        |
        |  return words
        |}
        |```
        |
        |This now gives us:
        |
        |```js
        |{"now":1,"my":1,"dears":1,"said":1,"old":1,"mrs":2,"rabbit":1,"one":1,"morning":1,"you":1,"may":1,"go":2,"into":2,"the":2,"fields":1,"or":1,"down":1,"lane":1,"but":1,"don":1,"mr":1,"mcgregor":2,"garden":1,"your":1,"father":1,"had":1,"an":1,"accident":1,"there":1,"he":1,"was":1,"put":1,"in":1,"pie":1,"by":1}
        |```
        |""".stripMargin)
    .markdownSlide(
      """## How much do two paragraphs have in common?
        |
        |Let's just decide that the commonality between two paragraphs is the proportion of their words that they share.
        |i.e.,
        |
        |> (number of words they share) / (total number of words they have)
        |
        |or
        |
        |> (size of the intersection of the vectors) / (size of the union of the vectors)
        |
        |We can already get the *union* of their words just by calling `combine(a, b)` that we defined earlier. But
        |we're going to need to write `intersection(a, b)`.
        |
        |We're also going to need a function that can add up the size of one of our word vectors.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Intersection of two vectors
        |
        |We want to return a vector of words, where a word is included only if it's in both vectors. If one contains
        |a word (e.g., `mcgregor`) twice but the other only contains it once, let's just count it once.
        |
        |```js
        |function intersection(a, b) {
        |  let result = { }
        |
        |  for (let key in a) {
        |    if (b[key] !== undefined) {
        |      if (b[key] < a[key]) {
        |        result[key] = b[key]
        |      } else {
        |        result[key] = a[key]
        |      }
        |    }
        |  }
        |
        |  return result
        |}
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Size of a vectors
        |
        |Our word vectors are in the format
        |
        |```js
        |{ hello: 1, there: 1, foo: 2 } // Hello there, Foo Foo!
        |```
        |
        |To add up the words, we just add up the numbers
        |
        |```js
        |function size(obj) {
        |  let sum = 0
        |  for (let key in obj) {
        |    sum = sum + obj[key]
        |  }
        |  return sum
        |}
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Commonality
        |
        |At last, we can define a measure of the commonality between two paragraphs...
        |
        |```js
        |// What proportion of words do two paragraphs have in common?
        |function commonality(a, b) {
        |  let top = size(intersection(a, b))
        |  if (top === 0) {
        |    // This solves the issue of comparing two empty paragraphs. 0/0 is NaN, but we return 0
        |    return 0
        |  } else {
        |    return top / size(combine(a, b))
        |  }
        |}
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Let's have a way of visualising this
        |
        |Let's just use the browser's API to add the paragraphs to the screen, and colour them according to how
        |relevant they are to a reference paragraph
        |
        |```js
        |let compare = vectors[3] // Colour everything in comparison to para 3
        |let app = document.getElementById("app")
        |for (let i in paragraphs) {
        |  let vector = vectors[i]
        |  let para = paragraphs[i]
        |
        |  let p = document.createElement("p")
        |
        |  // Make the background green, with an opacity (alpha) depending on the commonality
        |  let c = commonality(compare, vector)
        |  let colour = `rgba(0, 255, 0, ${c})`
        |  p.style.backgroundColor = colour
        |
        |  p.innerHTML = para
        |    + JSON.stringify(intersection(compare, vector))
        |    + c
        |
        |  app.append(p)
        |}
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## A problem
        |
        |There's a problem, though.
        |
        |* The most common pagagraphs in our document are all the ones that say "[Illustration]".
        |
        |* There are a lot of words like *"a"*, *"the"*, *"of"*, etc., that are counted as words in common but are so
        |  common they'll just skew our results to how many "the"s were in the paragraphs.
        |
        |We're going to need to introduce a *stop-list* of words we don't want to consider. Let's put it in an array
        |in case we want to tune it
        |
        |```js
        |let stopList = [
        |  "a", "and", "the", "on", "of", "in", "out", "up", "down", "by", "to", "into", "there", "with", "upon",
        |  "am", "be", "is", "was", "were", "for", "at", "after", "no", "or", "but", "had",
        |  "he", "she", "their", "her", "his", "him", "her", "they", "them", "who", "it", "an",
        |  "very", "then", "quite", "that", "so",
        |  "illustration", "mr", "presently", "time",
        |]
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Filtering out the Stop List
        |
        |To remove these words, we just need to insert an extra `filter` stage into our word vector function
        |
        |```js
        |function words(paragraph) {
        |  let words = paragraph
        |    .split(/\W/)
        |    .filter((s) => s.length > 1)
        |    .map((s) => s.toLowerCase())
        |    .filter((s) => stopList.indexOf(s) < 0)
        |    .map((w) => wordToObj(w))
        |    .reduce(combine, {})
        |
        |  return words;
        |}
        |```
        |
        |`stopList.indexOf(s)` will return the word's position in the stop-list if it is present, or `-1` if it isn't.
        |So, to filter out the stop-list words, we accept only the ones that had a negative result (not present).
        |""".stripMargin)
    .markdownSlide(
      """## The most relevant paragraph?
        |
        |Let's see if we can find the paragraph that has the most total relevance. Let's consider a pagraph's *total
        |relevance* as the sum of its relevance to each paragraph.
        |
        |I'm going to write this in a *functional programming* style... Don't worry if this looks alien to you at the
        |moment, the point is that sometimes this style can be very concise / dense.
        |
        |```js
        |function totalRelevance(left) {
        |  return vectors.map((v) => commonality(left, v)).reduce((a, b) => a + b)
        |}
        |```
        |
        |If we clone the vector array, and then sort it by totalRelevance, we can get a list of the most
        |relevant paragraphs overall
        |```js
        |let byRelevance = vectors.slice().sort((a, b) => totalRelevance(b) - totalRelevance(a))
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Our results
        |
        |Probably unsurprisingly, the most relevant paragraphs look like being the ones that contain Mr McGregor and Peter
        |
        |With a slight re-ordering, the top 3 tell most of the story:
        |
        |> But Peter, who was very naughty, ran straight away to Mr. McGregor's garden, and squeezed under the gate!
        |
        |> Presently Peter sneezed--'Kertyschoo!' Mr. McGregor was after him in no time.
        |
        |> Mr. McGregor caught sight of him at the corner, but Peter did not care. He slipped underneath the gate, and was safe at last in the wood outside the garden.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Map-Reduce
        |
        |If we look at our processing function, it is mostly made up of `filter`, `map`, and `reduce` calls
        |
        |```js
        |function words(paragraph) {
        |  let words = paragraph
        |    .split(/\W/)
        |    .filter((s) => s.length > 1)
        |    .map((s) => s.toLowerCase())
        |    .filter((s) => stopList.indexOf(s) < 0)
        |    .map((w) => wordToObj(w))
        |    .reduce(combine, {})
        |
        |  return words;
        |}
        |```
        |
        |Google's infrastructure for big data processing (around 2006) was called `MapReduce` becasue it was designed
        |to distribute this kind of processing across huge data servers around the world so it could run on data that's
        |in different places.
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")


  val deck = builder.renderNode



}
