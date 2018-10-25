package models.kirtans

import play.api.libs.json.Json

case class Letters(letters: Seq[Letter])

object Letters {
  implicit val format = Json.format[Letters]
}
