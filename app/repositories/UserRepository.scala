package repositories

import models.{APIError, DataModel, User}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.{Filters, IndexModel, IndexOptions, Indexes, ReplaceOptions}
import org.mongodb.scala.result

class UserRepository @Inject()(mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext)
                              extends PlayMongoRepository[User] (
  collectionName = "users",
  mongoComponent = mongoComponent,
  domainFormat = User.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id"),
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
  def updateUser(username: String, user: User): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    collection.replaceOne(
      filter = byUsername(username),
      replacement = user,
      options = new ReplaceOptions().upsert(true) //What happens when we set this to false?
    ).toFuture().map {
      case updatedUser => Right(updatedUser)
      case _ => Left(APIError.BadAPIResponse(500, "Failed to insert data"))
    }

  def deleteUser(username: String): Future[Either[APIError.BadAPIResponse,result.DeleteResult]] =
    collection.deleteOne(
      filter = byUsername(username)
    ).toFuture().map {
      case deletedBook => Right(deletedBook)
      case _ => Left(APIError.BadAPIResponse(500, "Failed to delete data"))
    }
}


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

