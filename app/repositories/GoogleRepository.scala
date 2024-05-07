//package repositories
//
//import models.GoogleBook
//import org.mongodb.scala.bson.conversions.Bson
//import org.mongodb.scala.model.Filters.empty
//import org.mongodb.scala.model._
//import org.mongodb.scala.result
//import services.ApplicationService
//import uk.gov.hmrc.mongo.MongoComponent
//import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
//
//import javax.inject.{Inject, Singleton}
//import scala.concurrent.{ExecutionContext, Future}
//@Singleton
//class GoogleRepository @Inject()(
//                                 mongoComponent: MongoComponent
//                               )(applicationService: ApplicationService)(implicit ec: ExecutionContext) extends PlayMongoRepository[GoogleBook] {
//  collectionName = "googleBook",
//  mongoComponent = mongoComponent,
//  domainFormat = GoogleBook.formats,
//  indexes = Seq(IndexModel(
//    Indexes.ascending("_id")
//  )),
//  replaceIndexes = false
//  ) {
//}
