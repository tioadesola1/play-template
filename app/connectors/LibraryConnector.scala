package connectors

import models.APIError
import play.api.libs.json.OFormat
import cats.data.EitherT
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.{WSClient, WSResponse}

class LibraryConnector @Inject()(ws: WSClient) {
  def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, Response] = {
    val request = ws.url(url)
    val response = request.get()
    EitherT {
      response
        .map {
          result =>
            Right(result.json.as[Response]) // at some point convert to data model, somewhere in code
        }
        .recover { case _: WSResponse =>
          Left(APIError.BadAPIResponse(500, "Could not connect"))
        }
    }
  }
}
