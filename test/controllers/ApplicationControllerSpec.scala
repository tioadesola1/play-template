//package controllers
//
//
//import baseSpec.BaseSpecWithApplication
//import play.api.test.FakeRequest
//import play.api.http.Status
//import play.api.test.Helpers._
//import models.{DataModel, GoogleBook}
//import play.api.libs.json.Format.GenericFormat
//import play.api.libs.json.{JsValue, Json}
//import play.api.mvc.Result
//
//import scala.concurrent.Future
//
//
//class ApplicationControllerSpec extends BaseSpecWithApplication {
//
//  val TestApplicationController = new ApplicationController(component, repository, service, executionContext)
//
//  private val dataModel: DataModel = DataModel(
//    "abcd",
//    "test name",
//    "test description",
//    100
//  )
//
//  private val dataModel1: DataModel = DataModel(
//    "abcd",
//    "update test",
//    "update description",
//    200
//  )
//
//
//  "ApplicationController .index" should {
//    //beforeEach()
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return 200 OK" in {
//      status(result) shouldBe Status.OK
//    }
//    //afterEach()
//  }
//
//
//  "ApplicationController .create" should {
//    //beforeEach()
//    "create a book in the database" in {
//
//      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Result = await(TestApplicationController.create()(requestPost))
//
//      val requestGet: FakeRequest[JsValue] = buildGet("/read/abcd").withBody[JsValue](Json.toJson(dataModel))
//      val readResult = TestApplicationController.read("abcd")(requestGet)
//
//      status(readResult) shouldBe Status.OK
//      createdResult.header.status shouldBe Status.CREATED
//    }
//    //afterEach()
//  }
//
//  "ApplicationController .read()" should {
//
//    "find a book in the database by id" in {
//      //beforeEach()
//      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
//      status(createdResult) shouldBe Status.CREATED
//
//      val request: FakeRequest[JsValue] = buildGet("/read/abcd").withBody[JsValue](Json.toJson(dataModel))
//      val readResult = TestApplicationController.read("abcd")(request)
//
//      //Hint: You could use status(createdResult) shouldBe Status.CREATED to check this has worked again
//
//      status(readResult) shouldBe Status.OK
//      contentAsJson(readResult) shouldBe Json.toJson(dataModel)
//
//      //Ask for further clarification why it is DataModel
//      //afterEach()
//    }
//
//  }
//
//  "ApplicationController .update()" should {
//    //beforeEach()
//
//    "update an item in the database with id" in {
//
//      val requestPut: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](Json.toJson(dataModel1))
//      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
//
//      status(createdResult) shouldBe Status.CREATED
//
//      //Hint: You could use status(createdResult) shouldBe Status.CREATED to check this has worked again
//
//      val updatedResult: Future[Result] = TestApplicationController.update("abcd")(requestPut)
//
//
//      status(updatedResult) shouldBe Status.ACCEPTED
//      contentAsJson(updatedResult).as[DataModel] shouldBe dataModel1
//
//      //afterEach()
//
//
//    }
//
//    "return bad request if structure is missing information" in {
//
//      val invalidJson = Json.obj("id" -> "abcd",
//        "name" -> "updated test",
//        "description" -> "update description"
//      )
//
//      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
//      val createdResult: Future[Result] = TestApplicationController.create()(requestPost)
//      status(createdResult) shouldBe Status.CREATED
//
//      val requestPut: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](invalidJson)
//      val updatedResult: Future[Result] = TestApplicationController.update("abcd")(requestPut)
//
// status(updatedResult) shouldBe Status.BAD_REQUEST
//      //contentAsJson(updatedResult).as[DataModel] shouldBe dataModel
//
//    }
//  }
//
//
//  "ApplicationController .delete()" should {
//    //beforeEach()
//
//    "delete an item in the database by id" in {
//
//      val requestPost: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
//      val cResult: Future[Result] = TestApplicationController.create()(requestPost)
//      status(cResult) shouldBe Status.CREATED
//
//      val request: FakeRequest[JsValue] = buildDelete("/delete/abcd").withBody[JsValue](Json.toJson(dataModel))
//      val deletedResult: Future[Result] = TestApplicationController.delete("abcd")(request)
//
//      //Hint: You could use status(createdResult) shouldBe Status.CREATED to check this has worked again
//
//
//      status(deletedResult) shouldBe Status.ACCEPTED
//      //contentAsJson(deletedResult).as[DataModel] shouldBe ACCEPTED
//      //afterEach()
//    }
//    //afterEach()
//
//  }
//
//}