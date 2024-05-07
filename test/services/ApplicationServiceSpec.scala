package services

import baseSpec.BaseSpec
import connectors.LibraryConnector
import models.{DataModel, GoogleBook}
import models.GoogleBook.formats
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, OFormat}
import play.api.mvc.Results.Status

import scala.concurrent.{ExecutionContext, Future}
import scala.tools.nsc.interactive.Response

class ApplicationServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {

  val mockConnector = mock[LibraryConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new ApplicationService(mockConnector)

  val gameOfThrones: JsValue = Json.obj(
    "_id" -> "someId",
    "name" -> "A Game of Thrones",
    "description" -> "The best book!!!",
    "pageCount" -> 100
  )

  "getGoogleBook" should {
    val url: String = "testUrl"

    "return a book" in {
      (mockConnector.get[DataModel](_: "testUrl")(_: OFormat[DataModel], _: ExecutionContext))
        .expects(*, *, *)
        .returning(Future.successful(gameOfThrones.as[DataModel]))
        .once()

      val result: Future[Option[DataModel]] = testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").map(Some(_))

      // Assert the result using whenReady
      whenReady(result) { res =>
        res shouldBe Some(gameOfThrones.as[DataModel])
      }
    }

    "return an error" in {
      val url: String = "testUrl"
      val errorMessage: String = "Something isn't right here"

      (mockConnector.get[DataModel](_: "testUrl")(_: OFormat[DataModel], _: ExecutionContext))
        .expects(*, *, *)
        .returning(Future.failed(new Exception(errorMessage)))// How do we return an error?
        .once()

      val result: Future[Option[DataModel]] = testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").map(Some(_))

      whenReady(result.failed) { throwable =>
        throwable shouldBe a[Exception] // Check if the result is an Exception
        throwable.getMessage shouldBe errorMessage // Check if the error message matches expected
      }

    }

  }
}
