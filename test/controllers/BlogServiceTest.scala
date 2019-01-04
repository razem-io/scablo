package controllers

import org.scalatest.FunSuite
import services.BlogService

class BlogServiceTest extends FunSuite {
  test ("check file name regex") {
    val validName1 = "170101_Test.md"
    val validName2 = "991231_Test.md"
    val validName3 = "001212_Test.md"

    val invalidName1 = "000012_Test.md"
    val invalidName2 = "991312_Test.md"
    val invalidName3 = "991242_Test.md"
    val invalidName4 = "170101_Test.mdd"
    val invalidName5 = "170101_Test_mdd"

    val regexString = BlogService.fileNameRegex.toString

    assert(validName1.matches(regexString))
    assert(validName2.matches(regexString))
    assert(validName3.matches(regexString))

    assert(!invalidName1.matches(regexString))
    assert(!invalidName2.matches(regexString))
    assert(!invalidName3.matches(regexString))
    assert(!invalidName4.matches(regexString))
    assert(!invalidName5.matches(regexString))
  }
}
