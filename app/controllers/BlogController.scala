package controllers

import better.files.File
import javax.inject.Inject
import models.BlogEntry
import org.joda.time.DateTime
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import play.twirl.api.Html
import services.BlogService

import scala.concurrent.ExecutionContext.Implicits.global

class BlogController @Inject()(cc: ControllerComponents, blogService: BlogService) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.blog.index(blogService.entries.sortBy(_.date.getMillis).reverse))
  }

  def post(year: String, month: String, day: String, name: String) = Action { implicit request: Request[AnyContent] =>
    blogService
      .findBlogEntry(s"$year$month${day}_$name")
      .map { e =>Ok(views.html.blog.entry(e))}
      .getOrElse(NotFound)
  }

  def postAsset(year: String, month: String, day: String, name: String, file: String) = Action { implicit request: Request[AnyContent] =>
    blogService
      .findBlogEntry(s"$year$month${day}_$name")
      .map { e => Ok.sendFile(File(e.assetFolder + "/" + file).toJava)}
      .getOrElse(NotFound)
  }

  def stackHack() = Action { implicit request: Request[AnyContent] =>
    blogService
      .findBlogEntry("160531_TryCatchLambda")
      .map { e =>Ok(views.html.blog.entry(e))}
      .getOrElse(NotFound)
  }

}
