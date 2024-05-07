package controllers

import models.{DataModel, GoogleBook}
import models.DataModel.formats
import models.GoogleBook.formats
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.DataRepository
import services.ApplicationService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val datarepository: DataRepository, val service: ApplicationService, implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    datarepository.index().map {
      case Right(item: Seq[DataModel]) => Ok {
        Json.toJson(item)
      }
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    datarepository.read(id).map {
      case (item: DataModel) => Ok{Json.toJson(item)}
      case _ => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        datarepository.create(dataModel)
        Future(Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        datarepository.update(id, dataModel)
        Future(Accepted(Json.toJson(dataModel)))
      //case JsError(error) => Future(BadRequest(Json.toString(error)))
        case JsError(_) => Future(BadRequest)
    }
  }
    def delete(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
      request.body.validate[DataModel] match {
        case JsSuccess(dataModel, _) =>
          val x = datarepository.delete(id)
        Future(Accepted(Json.toJson(dataModel)))
      }
    }
    def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
      service.getGoogleBook(search = search, term = term).map {
        case (item: DataModel) => Ok{Json.toJson(item)}
        case _ => NotFound(Json.obj("error" -> "Item not found"))
      }
    }

  }



