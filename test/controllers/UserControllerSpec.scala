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
}
