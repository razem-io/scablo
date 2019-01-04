package models

import com.typesafe.config.Config

case class StaticRoute(route: String, redirect: String)

object  StaticRoute {
  def apply(config: Config): StaticRoute = {
    val route = config.getString("route")
    val redirect = config.getString("redirect")

    StaticRoute(
      route = route,
      redirect = redirect
    )
  }
}
