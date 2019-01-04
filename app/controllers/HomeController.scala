package controllers

import com.typesafe.config.Config
import javax.inject._
import models.StaticRoute
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {
  def staticRoutes: Seq[StaticRoute] = config.get[Seq[Config]]("routes.static").map(StaticRoute(_))

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Redirect("/blog", 302)
  }

  def unknownRoute(path: String) = Action { implicit request: Request[AnyContent] =>
    staticRoutes.find(_.route == path).map(s => {
      Redirect(s.redirect)
    }).getOrElse(NotFound("Unknown Route"))
  }
}
