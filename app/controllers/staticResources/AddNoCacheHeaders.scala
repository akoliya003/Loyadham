package controllers.staticResources

import play.api.http.HeaderNames
import play.api.mvc.{Action, Request, Result}

import scala.concurrent.Future

case class AddNoCacheHeaders[A](action: Action[A]) extends Action[A] with HeaderNames {
  def apply(request: Request[A]): Future[Result] = {
    import scala.concurrent.ExecutionContext.Implicits.global

    // some special files (i.e. index.html) cannot use "cache busting" hash characters in their filename as other React files do. We need to treat them specially and set no-cache headers on them, so that when we push a new build browsers get it without browser caching getting in the way
    if (request.path == "/" || request.path == "/index.html" || !PhysicalResource.exists(request.path.dropWhile(_ == '/'))) {
      action(request).map(_.withHeaders(
        (CACHE_CONTROL -> "no-cache, no-store, must-revalidate"),
        (PRAGMA -> "no-cache"),
        (EXPIRES -> "0")
      ))
    } else if (request.path == "/service-worker.js") {
      action(request).map(_.withHeaders(
        (CACHE_CONTROL -> "no-cache, no-store, must-revalidate"),
        (PRAGMA -> "no-cache"),
        (EXPIRES -> "0")
      ))
    } else {
      action(request)
    }
  }

  override def parser = action.parser
  override def executionContext = action.executionContext
}
