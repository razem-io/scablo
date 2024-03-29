package utilities

import models.BlogEntry
import play.api.Logging
import plugins._

import scala.util.matching.Regex

object BlogUtils extends Logging {
  val nameRegex: Regex =  """(\[NAME\])(.*)(\[\/NAME\])""".r
  val shortRegex: Regex =  """(\[SHORT\])(.*)(\[\/SHORT\])""".r
  val tagsRegex: Regex =  """(\[TAGS\])(.*)(\[\/TAGS\])""".r
  val moduleRegex: Regex = """\${3}(\w*)#{3}(.*)\${3}""".r

  def parseName(markdown: String): Option[String] = {
    nameRegex.findFirstIn(markdown).flatMap(nameTag => nameTag match {
      case nameRegex(v1, name, v2) =>
        Some(name)
      case _ => None
    })
  }

  def parseShort(markdown: String): Option[String] = {
    shortRegex.findFirstIn(markdown).flatMap(shortTag => shortTag match {
      case shortRegex(v1, short, v2) =>
        Some(short)
      case _ => None
    })
  }

  def parseTags(markdown: String): Option[String] = {
    tagsRegex.findFirstIn(markdown).flatMap(tagsTag => tagsTag match {
      case tagsRegex(v1, tags, v2) =>
        Some(tags)
      case _ => None
    })
  }

  def removeMetaTags(markdown: String): String = {
    markdown
      .replaceFirst(nameRegex.regex, "")
      .replaceFirst(shortRegex.regex, "")
      .replaceFirst(tagsRegex.regex, "")
  }

  def applyModules(markdown : String)(implicit blogEntry: BlogEntry): String = {
    moduleRegex.replaceAllIn(markdown, r => {
      val moduleName = r.group(1)
      val moduleArgs = r.group(2).split(",")

      moduleName match {
        case "picture" => PicturePlugin(blogEntry, moduleArgs)
        case "pano" => PanoPlugin(blogEntry, moduleArgs)
        case "gallery" => GalleryPlugin(blogEntry, moduleArgs)
        case "googledrivevideo" => GoogleDriveVideoPlugin(blogEntry, moduleArgs)
        case "youtubevideo" => YoutubeVideoPlugin(blogEntry, moduleArgs)
        case name => logger.error(s"Unknown module: $name")
          ""
      }
    })
  }
}
