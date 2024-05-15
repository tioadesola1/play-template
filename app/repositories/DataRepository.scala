package repositories

import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model._
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DataRepository @Inject()(mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] = {
    collection.find().toFuture().map {
      case books: Seq[DataModel] => Right(books)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }
  }

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] = {
    collection
      .insertOne(book)
      .toFuture()
      .map(_ => book).map {
        case book => Right(book)
        case _ => Left(APIError.BadAPIResponse(500, "Failed to insert data"))
      }
  }
  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    collection.replaceOne(
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true) //What happens when we set this to false?
    ).toFuture().map {
      case updatedBook => Right(updatedBook)
      case _ => Left(APIError.BadAPIResponse(500, "Failed to insert data"))
    }

  def newUpdate(id: String, name: String): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    collection.updateOne(
      filter = byID(id),
      update = set("name", name),
      options = new UpdateOptions().upsert(true) //What happens when we set this to false?
    ).toFuture().map {
      case updatedBook => Right(updatedBook)
      case _ => Left(APIError.BadAPIResponse(500, "Failed to insert data"))
    }

  def delete(id: String): Future[Either[APIError.BadAPIResponse,result.DeleteResult]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFuture().map {
      case deletedBook => Right(deletedBook)
      case _ => Left(APIError.BadAPIResponse(500, "Failed to delete data"))
    }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  private def byName(name: String): Bson =
    Filters.and(
      Filters.equal("name", name)
    )

  def read(id: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.find(byID(id)).headOption flatMap {
      case Some(data) =>
        Future.successful(Right(data))
      case _ =>
        Future.successful(Left(APIError.BadAPIResponse(404, "Books cannot be found")))
    }

  def readByName(name: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.find(byName(name)).headOption flatMap {
      case Some(data) =>
        Future.successful(Right(data))
      case _ =>
        Future.successful(Left(APIError.BadAPIResponse(404, "Books cannot be found")))
    }

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //Hint: needed for tests

}

