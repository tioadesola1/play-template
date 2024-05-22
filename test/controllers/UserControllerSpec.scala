package controllers

import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers._
import models.{DataModel, GoogleBook, User}
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result

import scala.concurrent.Future
class UserControllerSpec extends BaseSpecWithApplication {

  val TestUserController = new UserController(component, userRepository, userService)

  private val user: User = User(
    1,
    "testUsername",
    "16/05/2024",
    "London",
    100,
    43
  )

  private val user1: User = User(
    2,
    "testUsername2",
    "19/06/2020",
    "Manchester",
    67,
    21
  )


  //  "UserController .getUser" should {
  //
  //    "return 200 OK" in {
  //      val result = TestUserController.getUser(user)(FakeRequest())
  //      status(result) shouldBe Status.OK
  //    }
  //  }

  "UserController .create" should {

    "create a user in the database" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/createUser").withBody[JsValue](Json.toJson(user))
      val createdResult: Future[Result] = TestUserController.createUser()(requestPost)

      val requestGet: FakeRequest[JsValue] = buildGet("/readUser/testUsername").withBody[JsValue](Json.toJson(user))
      val readResult = TestUserController.readUser("testUsername")(requestGet)

      status(readResult) shouldBe Status.OK
      status(createdResult) shouldBe Status.CREATED

    }
  }

  "UserController .read()" should {

    "find a user in the database by username" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/createUser").withBody[JsValue](Json.toJson(user))
      val createdResult: Future[Result] = TestUserController.createUser()(requestPost)
      status(createdResult) shouldBe Status.CREATED

      val request: FakeRequest[JsValue] = buildGet("/read/testUsername").withBody[JsValue](Json.toJson(user))
      val readResult = TestUserController.readUser("testUsername")(request)

      status(readResult) shouldBe Status.OK
      contentAsJson(readResult) shouldBe Json.toJson(user)
    }
  }

  "UserController .update()" should {
    //beforeEach()

    "update a user in the database with username" in {

      val requestPut: FakeRequest[JsValue] = buildPut("/updateUser/testUsername").withBody[JsValue](Json.toJson(user1))
      val requestPost: FakeRequest[JsValue] = buildPost("/createUser").withBody[JsValue](Json.toJson(user))
      val createdResult: Future[Result] = TestUserController.createUser()(requestPost)

      status(createdResult) shouldBe Status.CREATED

      val updatedResult: Future[Result] = TestUserController.updateUser("testUsername")(requestPut)


      status(updatedResult) shouldBe Status.ACCEPTED
      contentAsJson(updatedResult).as[User] shouldBe user1
    }
  }

  "UserController .delete()" should {

    "delete a user in the database by username" in {

      val requestPost: FakeRequest[JsValue] = buildPost("/createUser").withBody[JsValue](Json.toJson(user))
      val cResult: Future[Result] = TestUserController.createUser()(requestPost)
      status(cResult) shouldBe Status.CREATED

      val request: FakeRequest[JsValue] = buildDelete("/deleteUser/testUsername").withBody[JsValue](Json.toJson(user))
      val deletedResult: Future[Result] = TestUserController.deleteUser("testUsername")(request)

      status(deletedResult) shouldBe Status.ACCEPTED
    }
  }
}
