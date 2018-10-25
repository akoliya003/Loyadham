package controllers.staticResources

import java.io.File

import controllers.Assets
import javax.inject.Inject
import play.Environment
import play.api.Logger
import play.api.mvc.{Action, AnyContent}
import play.mvc.Controller

class StaticAssetsController @Inject()(private val assets: Assets,
                                       environment: Environment)
  extends Controller {

  private val logger = Logger(this.getClass.getName)

  private val publicDirectory = "/public"
  private val indexFile = "index.html"

  // this is used to check for the existence of 'physical' files in dev mode
  private val physicalPublicDirectory = s".${File.separator}public${File.separator}"

  // this is used to check for the existence of files in a jar in prod mode
  private val streamPublicDirectory = "public/"

  // https://stackoverflow.com/a/38816414/1011953
  private val fileExists: (String) => Boolean =
    if (environment.isProd) prodFileExists else devFileExists

  def index: Action[AnyContent] = AddNoCacheHeaders {
    serve(indexFile)
  }

  def frontEndPath(path: String): Action[AnyContent] = AddNoCacheHeaders {
    serve(path)
  }

  private def serve(path: String) = {
    if (fileExists(path)) {
      logger.debug(s"Serving physical resource: '$path'")
      assets.at(publicDirectory, path, aggressiveCaching = true) // use "aggressive caching" because React should generate filenames with a hash for each build
    } else {
      // serve up the contents of index.html without rewriting the url in the browser, so that React routes can work
      logger.debug(s"Serving virtual resource: '$path'")
      assets.at(publicDirectory, indexFile, aggressiveCaching = false) // don't use "aggressive caching" in case we want to update our index.html sometimes (since that filename can't change)
    }
  }

  private def devFileExists(path: String): Boolean = {
    var exists = PhysicalResource.exists(path)

    if (!exists) {

      val file = new File(physicalPublicDirectory + path)
      exists = file.exists

      if (exists)
        PhysicalResource.setExists(path)
    }

    exists
  }

  private def prodFileExists(path: String): Boolean = {
    var exists = PhysicalResource.exists(path)

    if (!exists) {
      // https://stackoverflow.com/a/43756053/1011953
      // https://stackoverflow.com/a/12133643/1011953
      val streamPath = streamPublicDirectory + path
      val stream = getClass.getClassLoader.getResourceAsStream(streamPath)

      exists = stream != null

      if (exists)
        PhysicalResource.setExists(path)
    }

    exists
  }
}
