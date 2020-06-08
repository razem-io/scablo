package plugins

import models.BlogEntry

object GoogleDriveVideoPlugin extends Plugin {
  override val name: String = "googledrivevideo"

  def apply(blogEntry: BlogEntry, args: Array[String]): String = {
    val videoId = args.head

    s"""<figure class="image is-16by9">
      |  <iframe class="has-ratio" width="100%" height="auto" src="https://drive.google.com/file/d/$videoId/preview" frameborder="0" allowfullscreen></iframe>
      |</figure>""".stripMargin
  }
}
