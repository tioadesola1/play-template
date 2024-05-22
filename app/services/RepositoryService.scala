package services

import models.DataModel
import repositories.DataRepository
import scala.concurrent.Future
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.result


class RepositoryService @Inject()(val repository: DataRepository) {
  def index(): Future[Either[String, Seq[DataModel]]] = {
    repository.index().map {
      case Right(item) => Right(item)
      case Left(_) => Left("Item not found")
    }
  }

  def read(id: String): Future[Either[String, DataModel]] = {
    repository.read(id).map {
      case Right(item) => Right(item)
      case Left(_) => Left("Item not found")
    }
  }

  def readByName(name: String): Future[Either[String, DataModel]] = {
    repository.readByName(name).map {
      case Right(item) => Right(item)
      case Left(_) => Left("Item not found")
    }
  }

  def create(dataModel: DataModel): Future[Either[String, DataModel]] = {
    repository.create(dataModel).map {
      case Right(dataModel) => Right(dataModel)
      case Left(_) => Left("Item could not be created")
    }
  }

  def update(id: String, dataModel: DataModel): Future[Either[String, result.UpdateResult]] = {
    repository.update(id, dataModel).map {
      case Right(updatedDataModel) => Right(updatedDataModel)
      case Left(_) => Left("Item could not be updated")
    }
  }

  def newUpdate(id: String, name: String): Future[Either[String, result.UpdateResult]] = {
    repository.newUpdate(id, name).map {
      case Right(updatedDataModel) => Right(updatedDataModel)
      case Left(_) => Left("Item could not be updated")
    }
  }
  def delete(id: String): Future[Either[String, result.DeleteResult]] = {
    repository.delete(id).map {
      case Right(deletedDataModel) => Right(deletedDataModel)
      case Left(_) => Left("Item could not be deleted")
    }
  }
}

