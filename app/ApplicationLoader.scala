import play.api.ApplicationLoader.Context
import play.api.inject.guice._

class ApplicationLoader extends GuiceApplicationLoader {
  override protected def builder(context: Context): GuiceApplicationBuilder = {
    java.security.Security.setProperty("networkaddress.cache.ttl", "60")
    super.builder(context)
  }
}
