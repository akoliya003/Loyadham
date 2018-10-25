package controllers

import javax.inject.Inject
import play.api.Configuration
import play.api.db.Database
import play.api.mvc.{Action, AnyContent, InjectedController}
import operations._
import util._

class KirtanController @Inject()(private val configuration: Configuration,
                                 private val auxiliaryExecutor: AuxiliaryExecutor,
                                 private val db: Database,
                                 private val dbExecutor: DatabaseExecutor,
                                 private val restExecutor: RestExecutor) extends InjectedController {
  def getKirtanLetters(): Action[AnyContent] = Action.async { implicit request =>
    GetKirtanLettersOperation(configuration, auxiliaryExecutor, db, dbExecutor, restExecutor, request)
  }

  def getKirtansTitleByLetter(): Action[AnyContent] = Action.async { implicit request =>
    GetKirtansTitleByLetterOperation(configuration, auxiliaryExecutor, db, dbExecutor, restExecutor, request)
  }
}
