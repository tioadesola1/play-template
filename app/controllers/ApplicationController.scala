package controllers

import models.DataModel
import models.DataModel.formats
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.DataRepository
import services.ApplicationService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ApplicationController @Inject()(
   val controllerComponents: ControllerComponents,
   val repository: DataRepository,
   val service: ApplicationService)
   (implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    repository.index().map {
      case Right(item: Seq[DataModel]) => Ok{Json.toJson(item)}
      case Left(error) => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    repository.read(id).map {
      case item: DataModel => Ok{Json.toJson(item)}
      case _ => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def readByName(name: String): Action[AnyContent] = Action.async { implicit request =>
    repository.readByName(name).map {
      case item: DataModel => Ok{Json.toJson(item)}
      case _ => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repository.create(dataModel)
        Future(Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repository.update(id, dataModel)
        Future(Accepted(Json.toJson(dataModel)))
        case JsError(_) => Future(BadRequest)
    }
  }

  def newUpdate(id: String, name: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repository.newUpdate(id, name)
        Future(Accepted(Json.toJson(dataModel)))
      case JsError(_) => Future(BadRequest)
    }
  }
    def delete(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
      request.body.validate[DataModel] match {
        case JsSuccess(dataModel, _) =>
          repository.delete(id)
        Future(Accepted(Json.toJson(dataModel)))
        case JsError(_) => Future(BadRequest)
      }
    }
    def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
      service.getGoogleBook(search = search, term = term).value.map {
        case Right(book) => Ok{Json.toJson(book)}
        case Left(error) => NotFound(Json.obj("error" -> "Item not found"))
      }
    }
  }



