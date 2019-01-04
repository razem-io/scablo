package utilities

import java.nio.file.Path

import scala.reflect.io.File

object IoUtils {
  def readFile(file: Path): String = {
    File(file.toString).slurp()
  }
}
