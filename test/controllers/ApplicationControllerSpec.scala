package controllers


import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers._
import models.{DataModel, GoogleBook}
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import repositories.{DataRepository, mockRepository}
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future


class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(component, mockRepository, repoService, service)

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100
  )

  private val dataModel1: DataModel = DataModel(
    "abcd",
    "update test",
    "update description",
    200
  )


  "ApplicationController .index" should {

    "return 200 OK" in {
      val result = TestApplicationController.index()(FakeRequest())
      status(result) shouldBe Status.OK
    }
  }


  "ApplicationController .create" should {

    "create a book in the database" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)

      val requestGet: FakeRequest[JsValue] = buildGet("/read/abcd").withBody[JsValue](Json.toJson(dataModel))
      val readResult = TestApplicationController.read("abcd")(requestGet)

      status(readResult) shouldBe Status.OK
      status(createdResult) shouldBe Status.CREATED
    }
  }

  "ApplicationController .read()" should {

    "find a book in the database by id" in {
      //beforeEach()
      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
      status(createdResult) shouldBe Status.CREATED

      val request: FakeRequest[JsValue] = buildGet("/read/abcd").withBody[JsValue](Json.toJson(dataModel))
      val readResult = TestApplicationController.read("abcd")(request)

      status(readResult) shouldBe Status.OK
      contentAsJson(readResult) shouldBe Json.toJson(dataModel)
    }
    "find book in the database by name" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
      status(createdResult) shouldBe Status.CREATED

      val request: FakeRequest[JsValue] = buildGet("/read/test name").withBody[JsValue](Json.toJson(dataModel))
      val readResult = TestApplicationController.readByName("test name")(request)

      status(readResult) shouldBe Status.OK
      contentAsJson(readResult) shouldBe Json.toJson(dataModel)
    }

  }

  "ApplicationController .update()" should {
    //beforeEach()

    "update an item in the database with id" in {

      val requestPut: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](Json.toJson(dataModel1))
      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)

      status(createdResult) shouldBe Status.CREATED

      val updatedResult: Future[Result] = TestApplicationController.update("abcd")(requestPut)


      status(updatedResult) shouldBe Status.ACCEPTED
      contentAsJson(updatedResult).as[DataModel] shouldBe dataModel1
    }

    "update an item in the database only providing id, field name and change" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
      status(createdResult) shouldBe Status.CREATED

      val requestPut: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](Json.toJson(dataModel))
      val updatedResult: Future[Result] = TestApplicationController.newUpdate("abcd", "new name")(requestPut)

      status(updatedResult) shouldBe Status.ACCEPTED
      contentAsJson(updatedResult).as[DataModel] shouldBe dataModel
    }


    "return bad request if structure is missing information" in {

      val invalidJson = Json.obj("id" -> "abcd",
        "name" -> "updated test",
        "description" -> "update description"
      )

      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
      status(createdResult) shouldBe Status.CREATED

      val requestPut: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](invalidJson)
      val updatedResult: Future[Result] = TestApplicationController.update("abcd")(requestPut)

      status(updatedResult) shouldBe Status.BAD_REQUEST

    }
  }


  "ApplicationController .delete()" should {

    "delete an item in the database by id" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val cResult: Future[Result] = TestApplicationController.create()(requestPost)
      status(cResult) shouldBe Status.CREATED

      val request: FakeRequest[JsValue] = buildDelete("/delete/abcd").withBody[JsValue](Json.toJson(dataModel))
      val deletedResult: Future[Result] = TestApplicationController.delete("abcd")(request)

      status(deletedResult) shouldBe Status.ACCEPTED
    }


  }

}