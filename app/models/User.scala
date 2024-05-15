package models

import play.api.libs.json.{Json, OFormat}

case class User(id: Int,
                username: String,
                createdAt: String,
                location: String,
                followers: Int,
                following: Int)

object User {
  implicit val formats: OFormat[User] = Json.format[User]
}