package plugins

import models.BlogEntry

object YoutubeVideoPlugin extends Plugin {
  override val name: String = "youtubevideo"

  def apply(blogEntry: BlogEntry, args: Array[String]): String = {
    val videoId = args.head

    s"""<figure class="image is-3by1">
      |  <iframe class="has-ratio" width="100%" height="auto" src="https://www.youtube.com/embed/$videoId" frameborder="0" allowfullscreen></iframe>
      |</figure>""".stripMargin
  }
}
