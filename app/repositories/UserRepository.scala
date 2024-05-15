package repositories

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import models.{APIError, User}

class UserRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) {

}
