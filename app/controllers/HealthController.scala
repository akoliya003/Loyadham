package controllers

import play.api.mvc.{Action, AnyContent, InjectedController, Results}

class HealthController extends InjectedController {

  def health(): Action[AnyContent] = Action { implicit request =>
    Results.Ok("OK")
  }
}