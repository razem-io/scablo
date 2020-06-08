package plugins

import better.files.File
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.nio.JpegWriter
import models.BlogEntry
import org.zeroturnaround.zip.ZipUtil
import play.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GalleryPlugin extends Plugin {
  override val name: String = "gallery"

  def apply(blogEntry: BlogEntry, args: Array[String]): String = {
    val galleryNumber = args.head.toInt
    val galleryFolder = File(blogEntry.assetFolder + "/gallery/" + galleryNumber)
    val rawFolder = File(galleryFolder.pathAsString + "/raw")
    val fullFolder = File(galleryFolder.pathAsString + "/full")
    val thumbFolder = File(galleryFolder.pathAsString + "/thumb")
    val zipFile = File(galleryFolder.pathAsString + s"/${blogEntry.encodedName}.zip")

    for {
      _ <- if(!fullFolder.exists) convertImages(rawFolder, fullFolder, 3840, 3840) else Future.successful()
      _ <- if(!thumbFolder.exists) convertImages(rawFolder, thumbFolder, 400, 400) else Future.successful()
      _ <- if(!zipFile.exists) createZipFromDirectory(fullFolder, zipFile) else Future.successful()
    } yield ()

    if(!fullFolder.exists) convertImages(rawFolder, fullFolder, 3840, 3840)
    if(!thumbFolder.exists) convertImages(rawFolder, thumbFolder, 400, 400)

    val rows = thumbFolder.children.toSeq.map(_.name).sorted.grouped(10000).map(row => {
      row.map(image => {
        """<div class="column is-narrow">""" +
        s"""<a data-fancybox="gallery" href="${blogEntry.url.split("/").last}/assets/gallery/$galleryNumber/full/$image">"""+
        s"""<img src="${blogEntry.url.split("/").last}/assets/gallery/$galleryNumber/thumb/$image">""" +
        "</a>" +
        """</div>"""
      }).mkString("""<div class="columns is-multiline is-centered">""", "\n", """</div>""")
    })

    val downloadThis = s"""<a href="${blogEntry.url.split("/").last}/assets/gallery/$galleryNumber/${zipFile.name}" class="button is-dark">Gallery Download</a>"""

    rows.mkString("\n") + downloadThis
  }

  def createZipFromDirectory(sourceFolder: File, zipFile: File): Future[Unit] = Future {
    Logger.info("Creating zip file from folder " + sourceFolder + ".")
    ZipUtil.pack(sourceFolder.toJava, zipFile.toJava)
    Logger.info("Creating zip file from folder " + sourceFolder + " finished.")
  }

  def convertImages(sourceFolder: File, destFolder: File, maxW: Int, maxH: Int, compression: Int = 80): Future[Unit] = {
    Future {
      destFolder.createDirectoryIfNotExists()

      val fullDirectoryPathAsString = destFolder.pathAsString
      val imageFiles = sourceFolder.children.toSeq
      val imageCount = imageFiles.size
      val leadingZeros = imageCount.toString.length

      Logger.info(s"Starting converting $imageCount images in $sourceFolder to max ${maxW}x$maxH.")

      imageFiles.sortBy(_.pathAsString).map(_.path).zipWithIndex.foreach { case (path, index) =>
        Image
          .fromPath(path)
          .bound(maxH, maxH)
          .output(fullDirectoryPathAsString + s"/${formatIndexName(index, leadingZeros)}.jpg")(JpegWriter().withCompression(compression))
      }

      Logger.info(s"Done converting $imageCount images in $sourceFolder to max ${maxW}x$maxH.")
    }
  }

  def formatIndexName(index: Int, leadingZeros: Int): String = {
    //f"$index%09d"
    s"%0${leadingZeros}d".format(index)
  }
}
