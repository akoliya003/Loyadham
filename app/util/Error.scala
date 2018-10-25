package util

import play.api.libs.json._

case class Error(error: String)

object Error {
  implicit val format = Json.format[Error]
}