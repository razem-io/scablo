package controllers

import org.scalatest.FunSuite
import utilities.BlogUtils

class BlogUtilsTest extends FunSuite {
  test("parse name") {
    val nameTagText = "[NAME]Test[/NAME]"

    assert("Test" == BlogUtils.parseName(nameTagText).get)
  }

  test("remove meta tags") {
    val nameTagText = "[NAME]Test[/NAME]"

    assert(BlogUtils.removeMetaTags(nameTagText) == "")
  }
}
