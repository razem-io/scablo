package utilities

import com.vladsch.flexmark.Extension
import com.vladsch.flexmark.ast.Document
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet

import scala.collection.JavaConverters._


object MarkdownUtils {


  val options = new MutableDataSet()

  val extensions: List[Extension] = List(TablesExtension.create, TaskListExtension.create)

  options.set(Parser.EXTENSIONS, extensions.asJava)


  val parser: Parser = Parser.builder(options).build
  val renderer: HtmlRenderer = HtmlRenderer.builder(options).build

  def parse(markdown: String): String = {
    val doc: Document = parser.parse(markdown)
    renderer.render(doc)
  }
}
