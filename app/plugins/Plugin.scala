package plugins

import models.BlogEntry

trait Plugin {
  val name: String

  def apply(blogEntry: BlogEntry, args: Array[String]): String
}
