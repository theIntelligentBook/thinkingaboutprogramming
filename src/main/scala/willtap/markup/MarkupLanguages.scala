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
        |>```
        |>'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into
        |>the fields or down the lane, but don't go into Mr. McGregor's garden:
        |>your Father had an accident there; he was put in a pie by Mrs.
        |>McGregor.'
        |>
        |>[Illustration]
        |>
        |>'Now run along, and don't get into mischief. I am going out.'
        |>```
        |
        |Because the illustrations aren't text, they can't be included in a text-only file, so there's just a special
        |placeholder to say where they go.
        |
        |What if we could also mark what image should go there, and render it so it appears?
        |""".stripMargin)
    .markdownSlide(
      """## Markdown
        |
        |**Markdown** is a text format that looks readable in plain text files, but can also be rendered to something
        |that includes images, bold text, etc.
        |
        |Let's just alter that Project Gutenberg text very slightly, and we have it in Markdown:
        |
        |>```
        |>'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into
        |>the fields or down the lane, but don't go into Mr. McGregor's garden:
        |>your Father had an accident there; he was put in a pie by Mrs.
        |>McGregor.'
        |>
        |>![Illustration](https://www.gutenberg.org/files/14838/14838-h/images/peter11.jpg)
        |>
        |>'Now run along, and don't get into mischief. I am going out.'
        |>```
        |
        |`![title](url)` is the Markdown format for including an image.
        |
        |""".stripMargin)
    .markdownSlide(
      """## Our rendered Markdown:
        |
        |'Now my dears,' said old Mrs. Rabbit one morning, 'you may go into
        |the fields or down the lane, but don't go into Mr. McGregor's garden:
        |your Father had an accident there; he was put in a pie by Mrs.
        |McGregor.'
        |
        |![Illustration](https://www.gutenberg.org/files/14838/14838-h/images/peter11.jpg)
        |
        |'Now run along, and don't get into mischief. I am going out.'
        |""".stripMargin)
    .markdownSlide(
      """<pre>
        |## Markdown in text format
        |
        |There are several things we can say about **Markdown**:
        |
        |* It's designed so that if you are reading it "raw", as text, its layout still makes sense
        |* For instance, this bulleted list
        |* Or if I wanted to *emphasize* something
        |
        |But if I had three things I should really say:
        |
        |1. It is a plain text format
        |2. It was originally just defined in a program by John Gruber, which is often still used as the format's
        |   [documentation](https://daringfireball.net/projects/markdown/)
        |3. More recently, **CommonMark** have tried to standardise it. They have a very useful page called
        |   [Learn Markdown in 60 Seconds](https://commonmark.org/help/)
        |
        |It's also used to write these notes...
        |</pre>
        |""".stripMargin)
    .markdownSlide(
      """## Markdown in rendered format
        |
        |There are several things we can say about **Markdown**:
        |
        |* It's designed so that if you are reading it "raw", as text, its layout still makes sense
        |* For instance, this bulleted list
        |* Or if I wanted to *emphasize* something
        |
        |But if I had three things I should really say:
        |
        |1. It is a plain text format
        |2. It was originally just defined in a program by John Gruber, which is often still used as the format's
        |   [documentation](https://daringfireball.net/projects/markdown/)
        |3. More recently, **CommonMark** have tried to standardise it. They have a very useful page called
        |   [Learn Markdown in 60 Seconds](https://commonmark.org/help/)
        |
        |It's also used to write these notes...
        |""".stripMargin)
    .markdownSlide(
      """## How do we convert from one to the other?
        |
        |The process of converting from a text format into data is called *parsing*.
        |
        |How its done depends on the language. Originally, Markdown didn't have a formally specified "grammar". It
        |was just parsed by a program to make it work.
        |
        |For instance, to extract `*italic*` text, it would look for text surrounded by asterisks. In its source-code,
        |this was:
        |
        |```perl
        |$text =~ s{ (\*|_) (?=\S) (.+?) (?<=\S) \1 }
        |		{<em>$2</em>}gsx;
        |```
        |
        |That probably looks like an alien language, because it's not JavaScript. That was Perl.
        |""".stripMargin)
    .markdownSlide(
      """## Finding an emphasized string in JavaScript
        |
        |In JavaScript, we could define a *regular expression* to describe the pattern we're looking for.
        |
        |Let's say it's "an asterisk, followed by some letters/numbers, followed by an asterisk"
        |
        |That might look like:
        |
        |```js
        |let string = "This is *italic*, it is!"
        |let regex = /\*(\w+)\*/
        |str.match(regex)
        |```
        |
        |If we ran this, we'd get this return value:
        |
        |```
        |["*italic*", "italic", index: 8, input: "This is *italic*, it is!", groups: undefined]
        |```
        |
        |""".stripMargin)
    .markdownSlide(
      """## Regular expressions
        |
        |At their simplest, a regular expression can just be a string you're looking for:
        |
        |```js
        |let regex = /boo/
        |```
        |
        |But we could alter it to say we're interested in that, no matter how many 'o's there are at the end of "boo":
        |
        |```js
        |let regex = /bo+/
        |```
        |
        |The `+` means "this occurs one or more times". An `*` would mean "this occurs zero or more times"
        |""".stripMargin)
    .markdownSlide(
      """## Wildcards
        |
        |`"This course is ...."`
        |
        |What do the "...." stand for?  "Cool"? "Fast"? "Slow"? Something ruder?
        |
        |Just as we have placeholders or wildcards in our string above, regular expressions also let you define some
        |*character classes*:
        |
        |* `.` matches any character
        |* <code>\d</code> matches any digit, `0` to `9`
        |* <code>\D</code> matches anything that's *not* a digit
        |* <code>\w</code> matches any "word" character. Letters and numbers.
        |* <code>\w</code> matches anything that's not a "word" character.
        |* <code>\s</code> matches whitespace: space, tab, newline, etc.
        |* <code>\n</code> matches a newline
        |""".stripMargin)
    .markdownSlide(
      """## Escapes
        |
        |But what if we really wanted to match a `\` character? Or something else that has a special meaning?
        |
        |We can *escape* the character in the expression by putting `\` before it.
        |
        |So, if we wanted to search for `\`, our regular expression would be:
        |
        |```
        |/\\/
        |```
        |""".stripMargin)
    .markdownSlide(
      """## Capturing groups
        |
        |Sometimes, you also want the search to extract part of the content of what it matched. For instance, when
        |looking for `*italics*` we'd like to get the text we're making italic.
        |
        |We can define a group in the expression by wrapping it in parentheses `( )`
        |
        |So, our regular expression came out as:
        |
        |```
        |/\*(\w+)\*/
        |```
        |
        |That is:
        |
        |* We escaped an asterisk
        |* A group consisting of one or more word characters
        |* We escaped another asterisk
        |
        |""".stripMargin)
    .markdownSlide(
      """## Markdown's processing
        |
        |Markdown's parsing was a custom program. It would:
        |
        |* Break text up into blocks, e.g. paragraphs
        |
        |* Within those blocks, it would look for regular expressions for "inline" items it needed to replace (e.g.
        |  text to italicise, things to render as links)
        |
        |It's useful, but it's not a "formal grammar". There's not an unambiguous set of rules.
        |""".stripMargin)
    .markdownSlide(
      """## HTML
        |
        |When Markdown is parsed, it usually outputs **HTML** - the markup language of the World Wide Web.
        |
        |This is a different format that's more verbose and not quite as easy to read.
        |
        |* *Elements* in the document are marked out with *tags* in angle-brackets. e.g.
        |
        |    ```js
        |    <p>This is a paragraph</p>
        |    ```
        |
        |* Text can have elements nested inside it. e.g.
        |
        |    ```js
        |    <p>This is <em>emphasized</em> in this paragraph</p>
        |    ```
        |
        |    This is <em>emphasized</em> in this paragraph
        |
        |* Tags can be given *attributes*. e.g.
        |
        |    ```js
        |    <p>This is <em style="color: red">emphasized and red</em> in this paragraph</p>
        |    ```
        |
        |    This is <em style="color: red">emphasized and red</em> in this paragraph
        |""".stripMargin)
    .markdownSlide(
      """## Useful elements
        |
        |We're not going to teach you much HTML in this - this isn't a web course, just an intro to the format. But
        |here are some useful ones:
        |
        |* `<p>` - paragraph
        |
        |* `<h1>` - a level 1 heading (big heading)
        |
        |    <h1>Level 1 heading</h1>
        |
        |* `<h2>` - a level 2 heading (slightly smaller heading)
        |
        |    <h2>Level 2 heading</h2>
        |
        |* `<h3>` - a level 3 heading (slightly smaller again heading)
        |
        |   <h3>Level 3 heading</h3>
        |
        |""".stripMargin)
    .markdownSlide(
      """## Widgets
        |
        |We can also include simple widgets in HTML, and set events handlers on them from attributes:
        |
        |* ```html
        |  <button onclick="javascript: window.alert('boo')">I'm a button</button>
        |  ```
        |
        |  <button onclick="javascript: window.alert('boo')">I'm a button</button>
        |
        |* ```html
        |  <input type='text' placeholder="I'm a text input" />
        |  ```
        |
        |  <input type='text' placeholder="I'm a text input" />
        |
        |""".stripMargin)
    .markdownSlide(
      """## Forms vs applications
        |
        |In early versions of the Web, this was used to create *forms* that could be submitted. Those are still
        |supported, but it's now more common to write programs that use the web as an application user interface -
        |loading JavaScript programs that will alter what's on the page depending on what the user does.
        |
        |To support this, we need more special kinds of tag:
        |
        |* `<script>`, which loads the program
        |
        |* `<link>`, which is used to load in fonts and stylesheets
        |
        |Let's take a look at the HTML of this deck...
        |
        |(Right click -> View page source)
        |
        |There's not a lot in the HTML! It's all added by a program!
        |""".stripMargin)
    .markdownSlide(
      """## Inspecting the page
        |
        |We can see what's in the document at any moment from the Developer Tools.
        |
        |For instance, open up one of the Lava Maze puzzles and see what's in the HTML by opening the *inspector*
        |(Ctrl-shift-I)
        |
        |We might find many more elements:
        |
        |* `<canvas>` for rendering raster graphics
        |* `<svg>` for rendering vector graphics
        |* `<div>` defining different sections of the page
        |* `<nav>` defining navigation elements
        |* Elements that are scaled to fit the screen
        |
        |Even though the page contents are inserted and updated programmatically, the browser tracks what the
        |equivalent HTML would be and can show it in the inspector.
        |""".stripMargin)
    .markdownSlide(Common.willCcBy).withClass("bottom")

  val deck = builder.renderNode


}
