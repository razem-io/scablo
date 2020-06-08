package models

import org.joda.time.DateTime
import services.BlogService


  case class BlogEntry (name: String, short: String, tags: Seq[String], pathToFile: String, fileName: String, date: DateTime)(implicit blogService: BlogService) {
    val url: String = BlogEntry.genUrl(this)
    def html: String = BlogService.blogEntryToMarkdown(this).getOrElse("")

    val assetFolder: String = pathToFile.dropRight(3)

    val prettyTags: String = tags.mkString(" ")
    val prettyDate: String = s"${date.getYear}/${date.getMonthOfYear}/${date.getDayOfMonth}"

    def encodedName: String = BlogEntry.encodeName(name)
  }

object BlogEntry {
  private def zeroIfShort(number: Int): String = {
    val stringNumber = number.toString

    if(stringNumber.length < 2) s"0$stringNumber"
    else stringNumber
  }

  private def shortYear(date: DateTime): String = {
    date.getYear.toString.splitAt(2)._2
  }

  def encodeName(name: String): String = {
    name
      .replaceAll(" ", "_-_")
  }

  def decodeName(name: String): String = {
    name
      .replaceAll("_-_", " ")
  }

  def genUrl(blogEntry: BlogEntry): String = {
    val year = shortYear(blogEntry.date)
    val month = zeroIfShort(blogEntry.date.getMonthOfYear)
    val day = zeroIfShort(blogEntry.date.getDayOfMonth)
    val prettyName = blogEntry.fileName.splitAt(7)._2.replace(".md", "")

    s"/blog/$year/$month/$day/$prettyName"
  }
}
