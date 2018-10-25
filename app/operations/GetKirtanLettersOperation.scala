package operations

import java.sql.Connection
import models.kirtans.{Letter, Letters}
import play.api.Configuration
import play.api.db.Database
import play.api.libs.json.{JsResultException, Json}
import play.api.mvc.{AnyContent, Request, Result}
import scala.concurrent.{ExecutionContextExecutor, Future}
import util._

object GetKirtanLettersOperation {
  def apply(configuration: Configuration,
            auxiliaryExecutor: ExecutionContextExecutor,
            db: Database,
            dbExecutor: ExecutionContextExecutor,
            restExecutor: ExecutionContextExecutor,
            request: Request[AnyContent]): Future[Result] = {
    new GetKirtanLettersOperation(configuration, auxiliaryExecutor, db, dbExecutor, restExecutor, request).execute()
  }
}

class GetKirtanLettersOperation(private val configuration: Configuration,
                                private val auxiliaryExecutor: ExecutionContextExecutor,
                                private val db: Database,
                                private val dbExecutor: ExecutionContextExecutor,
                                private val restExecutor: ExecutionContextExecutor,
                                private val request: Request[AnyContent]) {
  def execute(): Future[Result] = {
    Future {
      db.withConnection(implicit connection => doExecute())
    }(dbExecutor).recover {
      case e: JsResultException =>
        MakeResult.badRequest(e.errors.mkString(","))
      case e: Exception =>
        MakeResult.internalServerError(e.getMessage)
    }(auxiliaryExecutor)
  }

  private def doExecute()(implicit connection: Connection): Result = {
    val sql = "Select * from TitleIndex"
    val statement = connection.prepareStatement(sql)
    val resultSet = statement.executeQuery()
    val letters = collection.mutable.ArrayBuffer[Letter]()
    while (resultSet.next()) {
      letters += Letter(
        letter = resultSet.getString("Title"))
    }
    MakeResult.ok(Json.toJson(Letters(letters= letters)))
  }
}
