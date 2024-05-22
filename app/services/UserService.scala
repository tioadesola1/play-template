package services

import cats.data.EitherT
import connectors.UserConnector
import models.{APIError, DataModel, User}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject() (connector: UserConnector) {

  def getUser(urlOverride: Option[String] = None, username: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, User] =
    connector.get[User](urlOverride.getOrElse(s"https://api.github.com/users/$username"))
}

