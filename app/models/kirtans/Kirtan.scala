package models.kirtans

import play.api.libs.json.Json

case class Kirtan(title: String,
                  kirtan: String,
                  category: String)

object Kirtan {
  implicit val format = Json.format[Kirtan]
}
