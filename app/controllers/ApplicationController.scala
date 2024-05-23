package controllers

import models.DataModel
import models.DataModel.formats
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import repositories.{DataRepository, mockRepository}
import services.{ApplicationService, RepositoryService}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ApplicationController @Inject()(
   val controllerComponents: ControllerComponents,
   val mockRepository: mockRepository,
   val repoService: RepositoryService,
   val service: ApplicationService)
   (implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    repoService.index().map {
      case Right(item: Seq[DataModel]) => Ok{Json.toJson(item)}
      case Left(error) => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    repoService.read(id).map {
      case Right(item: DataModel) => Ok{Json.toJson(item)}
      case Left(error) => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def readByName(name: String): Action[AnyContent] = Action.async { implicit request =>
    repoService.readByName(name).map {
      case Right(item: DataModel) => Ok{Json.toJson(item)}
      case Left(error) => NotFound(Json.obj("error" -> "Item not found"))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
       repoService.create(dataModel)
        Future(Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repoService.update(id, dataModel)
        Future(Accepted(Json.toJson(dataModel)))
        case JsError(_) => Future(BadRequest)
    }
  }

  def newUpdate(id: String, name: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repoService.newUpdate(id, name)
        Future(Accepted(Json.toJson(dataModel)))
      case JsError(_) => Future(BadRequest)
    }
  }
    def delete(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
      request.body.validate[DataModel] match {
        case JsSuccess(dataModel, _) =>
          repoService.delete(id)
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



