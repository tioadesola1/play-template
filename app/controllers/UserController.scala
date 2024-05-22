package controllers

import models.{DataModel, User}
import models.User.formats
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.{DataRepository, UserRepository}
import services.{ApplicationService, RepositoryService, UserService}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
@Singleton
class UserController @Inject()(
                                val controllerComponents: ControllerComponents,
                                val userRepository: UserRepository,
                                val userService: UserService)
                              (implicit val ec: ExecutionContext) extends BaseController {


  def getUser(username: String): Action[AnyContent] = Action.async { implicit request =>
    userRepository.getUser(username).map {
      case Right(user: Seq[User]) => Ok(Json.toJson(user))
      case _ => NotFound(Json.obj("error" -> "User not found"))
    }
  }

  def readUser(username: String): Action[AnyContent] = Action.async { implicit request =>
    userRepository.readUser(username).map {
      case Right(user: User) => Ok {
        Json.toJson(user)
      }
      case Left(error) => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def createUser(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(users, _) =>
        userRepository.createUser(users)
        Future(Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def updateUser(username: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(user, _) =>
        userRepository.updateUser(username, user)
        Future(Accepted(Json.toJson(user)))
      case JsError(_) => Future(BadRequest)
    }
  }

  def deleteUser(username: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(user, _) =>
        userRepository.deleteUser(username)
        Future(Accepted(Json.toJson(user)))
      case JsError(_) => Future(BadRequest)
    }
  }
}
