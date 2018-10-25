package operations

import java.sql.Connection
import models.kirtans.{Kirtan, Kirtans, Letter}
import play.api.Configuration
import play.api.db.Database
import play.api.libs.json.{JsResultException, Json}
import play.api.mvc.{AnyContent, Request, Result}
import scala.concurrent.{ExecutionContextExecutor, Future}
import util._

object GetKirtansTitleByLetterOperation {
  def apply(configuration: Configuration,
            auxiliaryExecutor: ExecutionContextExecutor,
            db: Database,
            dbExecutor: ExecutionContextExecutor,
            restExecutor: ExecutionContextExecutor,
            request: Request[AnyContent]): Future[Result] = {
    new GetKirtansTitleByLetterOperation(configuration, auxiliaryExecutor, db, dbExecutor, restExecutor, request).execute()
  }
}

class GetKirtansTitleByLetterOperation(private val configuration: Configuration,
                                       private val auxiliaryExecutor: ExecutionContextExecutor,
                                       private val db: Database,
                                       private val dbExecutor: ExecutionContextExecutor,
                                       private val restExecutor: ExecutionContextExecutor,
                                       private val request: Request[AnyContent]) {
  def execute(): Future[Result] = {
    Future {
      request.body.asJson.get.as[Letter]
    }(auxiliaryExecutor).map { letter =>
      db.withConnection(implicit connection => doExecute(letter))
    }(dbExecutor).recover {
      case e: JsResultException =>
        MakeResult.badRequest(e.errors.mkString(","))
      case e: Exception =>
        MakeResult.internalServerError(e.getMessage)
    }(auxiliaryExecutor)
  }

  private def doExecute(letter: Letter)(implicit connection: Connection): Result = {
    val sql =
      """Select * from Kirtans
        |WHERE category = ? or
        |category = ?""".stripMargin
    val statement = connection.prepareStatement(sql)
    statement.setString(1, letter.letter.toLowerCase)
    statement.setString(2, letter.letter.toUpperCase)
    val resultSet = statement.executeQuery()
    val kirtans = collection.mutable.ArrayBuffer[Kirtan]()
    while (resultSet.next()) {
      kirtans += Kirtan(
        title = resultSet.getString("title"),
        kirtan = resultSet.getString("kirtan"),
        category = resultSet.getString("category"))
    }
    MakeResult.ok(Json.toJson(Kirtans(kirtans= kirtans)))
  }
}
