[NAME]Hello scablo[/NAME]
[SHORT]In the case of razem.io this is the first rendered html based on markdown. [/SHORT]
[TAGS]TAG1 TAG2 TAG3[/TAGS]
# Hello scablo
In the case of razem.io this is the first rendered html based on markdown. 

There are a lot of static compilers out there but 

## Let us take a test drive
To test
* list
* table
* code
* links

### List
Normal List
* Entry A
* Entry B

Ordered List
1. One
1. Two
1. Three
1. Four

Task List
- [X] Done
- [ ] Yet to do

### Table
|Test Case|Results|
|---|---|
|list|ok|
|table|ok|
|code|ok|
|link|ok|

### Code
```nohighlight
no highlight test
```
```scala
//let the rainbow explode
case class BlogEntry (name: String, fileName: String, date: DateTime)(implicit blogService: BlogService) {
  val url: String = BlogEntry.genUrl(this)
  def html: String = BlogService.blogEntryToMarkdown(this).getOrElse("")
}
```
### Link
[Github](http://github.com)