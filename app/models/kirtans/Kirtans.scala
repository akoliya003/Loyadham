package models.kirtans

import play.api.libs.json.Json

case class Kirtans(kirtans: Seq[Kirtan])

object Kirtans {
  implicit val format = Json.format[Kirtans]
}
