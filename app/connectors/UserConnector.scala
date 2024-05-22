package connectors

import models.{APIError, User}
import play.api.libs.json.OFormat
import cats.data.EitherT

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.{WSClient, WSResponse}

  class UserConnector @Inject()(ws: WSClient) {
  def get[Response](username: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, User] = {
    val url = s"https://api.github.com/users/$username"
    val request = ws.url(url)
    val response = request.get()
    EitherT {
      response
        .map {
          result =>
            Right(result.json.as[User])
        }
        .recover { case _: WSResponse =>
          Left(APIError.BadAPIResponse(500, "Could not connect"))
        }
    }
  }
}