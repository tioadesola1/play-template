package repositories

import models.APIError
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import models.{APIError, DataModel, User}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.{Filters, IndexModel, Indexes}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

class UserRepository @Inject()(mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext)
                              extends PlayMongoRepository[User] (
  collectionName = "users",
  mongoComponent = mongoComponent,
  domainFormat = User.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) {
  def getUser(username: String): Future[Either[APIError.BadAPIResponse, Seq[User]]] = {
    collection.find().toFuture().map {
      case users => Right(users)
      case _ => Left(APIError.BadAPIResponse(404, "User cannot be found"))
    }.recover {
      case ex: Exception => Left(APIError.BadAPIResponse(500, s"An error occurred: ${ex.getMessage}"))
    }
  }

    def readUser(username: String): Future[Either[APIError.BadAPIResponse, User]] = {
      collection.find(byUsername(username)).headOption flatMap {
        case Some(data) =>
          Future.successful(Right(data))
        case _ =>
          Future.successful(Left(APIError.BadAPIResponse(404, "Books cannot be found")))
      }
    }

    private def byUsername(username: String): Bson =
      Filters.and(
        Filters.equal("username", username)
        )

  def createUser(user: User): Future[Either[APIError.BadAPIResponse, User]] = {
    collection
      .insertOne(user)
      .toFuture()
      .map(_ => user).map {
        case users => Right(user)
        case _ => Left(APIError.BadAPIResponse(500, "Failed to insert data"))
      }
  }
}
//  def updateUser(username: String): Action[AnyContent] = Action.async { implicit request =>
//    Future.successful(Ok(s"Update user: $username"))
//  }
//  def deleteUser(username: String): Action[AnyContent] = Action.async { implicit request =>
//    Future.successful(Ok(s"Delete user: $username"))
//  }



//  def ensureUniqueIndex(): Future[Boolean] = {
//    userCollection.flatMap { collection =>
//      val index = Index(
//        key = Seq("username" -> Ascending),
//        name = Some("username_unique_idx"),
//        unique = true
//      )
//      collection.indexesManager.ensure(index)
//    }
//  }

