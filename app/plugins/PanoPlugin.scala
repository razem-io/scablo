package plugins

import models.BlogEntry

object PanoPlugin extends Plugin {
  override val name: String = "pano"

  def apply(blogEntry: BlogEntry, args: Array[String]): String = {
    val filename = args.head
    val id = filename.split('.').head

    s"""
       |<figure class="image is-16by9">
       |  <div class="has-ratio" width="100%" height="auto" id="$id"></div>
       |</figure>
       |<script>
       |pannellum.viewer('$id', {
       |    "type": "equirectangular",
       |    "panorama": "${blogEntry.url.split("/").last}/assets/$filename",
       |    "autoLoad": false
       |});
       |</script>
       |""".stripMargin
  }
}
