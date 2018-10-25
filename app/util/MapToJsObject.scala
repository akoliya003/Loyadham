package util

import play.api.libs.json._
import scala.language.implicitConversions // remove compiler warning that implicit conversion is being created

package object conversions {
  implicit def mapToJsObject(m: Map[String, Any]) = new MapToJsObject(m)
}

class MapToJsObject(m: Map[String, Any]) {
  import conversions._
  def toJsObject(): JsObject = {
    JsObject(m.mapValues {
      case v: String => JsString(v)
      case v: Int => JsNumber(v)
      case v: Long => JsNumber(v)
      case v: Float => JsNumber(v)
      case v: Double => JsNumber(v)
      case v: Boolean => JsBoolean(v)
      case v: Map[_, _] => mapToJsObject(v.asInstanceOf[Map[String, Any]]).toJsObject
      case null => JsNull
    })
  }
}
