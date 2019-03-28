package services

import java.nio.file.{Files, Path, Paths}
import javax.inject.{Inject, Singleton}

import models.BlogEntry
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.{Configuration, Logger}
import utilities.{BlogUtils, IoUtils, MarkdownUtils}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

case class BlogConfig(title: String, subtitle: String)

@Singleton
class BlogService @Inject()(config: Configuration) {
  import BlogService._

  val blogConfig = BlogConfig(
    title = config.get[String]("blog.title"),
    subtitle = config.get[String]("blog.subtitle")
  )

  val filePath: String = config.get[String]("blog.path")

  val entries: mutable.Buffer[BlogEntry] = Files.walk(Paths.get(filePath), 1).iterator.asScala
    .filter(Files.isRegularFile(_))
    .filter(checkFilename)
    .flatMap(f => fileToBlogEntry(f)(this))
    .toBuffer
    .sortBy(_.date.getMillis)

  def findBlogEntry(name: String): Option[BlogEntry] = entries
    .find(_.fileName == s"$name.md")
}

object BlogService {
  val logger: Logger = Logger(this.getClass)
  val fileNameRegex: Regex = """([0-9]\d{1})((0[1-9]|1[0-2]))((0|1|2|3)\d{1})_\S*(\.md)""".r
  val dateFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyMMdd")

  def checkFilename(file: Path): Boolean = {
    val result = file.getFileName.toString.matches(fileNameRegex.toString)

    if(!result) logger.warn(s"Filename ${file.getFileName} not compatible!")

    result
  }

  def pathToFile(file: Path): Option[String] = {
    if(checkFilename(file)) Some(file.getFileName.toString)
    else None
  }

  def filenameToDate(filename: String): Option[DateTime] = {
    if(filename.matches(fileNameRegex.regex)) {
      Try(DateTime.parse(filename.split("_", 2).head, dateFormat)) match {
        case Success(v) =>
          Some(v)
        case Failure(e) =>
          logger.error("Unable to convert filename to date.", e)
          None
      }
    } else {
      None
    }
  }

  def parseMarkdown(markdown: String): String = {
    MarkdownUtils.parse(markdown)
  }

  def readMarkdown(file: Path): String = {
    parseMarkdown(IoUtils.readFile(file))
  }

  def  fileToBlogEntry(file: Path)(implicit blogService: BlogService): Option[BlogEntry] = {
    pathToFile(file)
      .flatMap(filenameToDate)
      .map(date => {
        // TODO: read online meta data instead of all the data
        val rawText = IoUtils.readFile(file)
        val title = BlogUtils.parseName(rawText).getOrElse("Unknown Title")
        val short = BlogUtils.parseShort(rawText).getOrElse("")
        val tags: Seq[String] = BlogUtils.parseTags(rawText).map(_.split(" ", -1).toSeq).getOrElse(Seq.empty)

        BlogEntry(
          name = title,
          short = short,
          tags = tags,
          fileName = file.getFileName.toString,
          pathToFile = file.toAbsolutePath.toString,
          date = date
        )
      })
  }

  def blogEntryToMarkdown(implicit blogEntry: BlogEntry): Option[String] = {
    Try {
      val rawText = IoUtils.readFile(Paths.get(blogEntry.pathToFile))
      val markdownWithModules = BlogUtils.removeMetaTags(rawText)
      val markdown = BlogUtils.applyModules(markdownWithModules)
      parseMarkdown(markdown)
    }.toOption
  }
}
