package models.kirtans

import play.api.libs.json.Json

case class Letter(letter: String)

object Letter {
  implicit val format = Json.format[Letter]
}
