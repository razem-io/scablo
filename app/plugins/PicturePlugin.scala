package plugins

import models.BlogEntry

object PicturePlugin extends Plugin {
  override val name: String = "picture"

  def apply(blogEntry: BlogEntry, args: Array[String]): String = {
    val filename = args.head

    s"""<img class="has-ratio" width="100%" height="auto" src="${blogEntry.url.split("/").last}/assets/$filename">""".stripMargin
  }
}
