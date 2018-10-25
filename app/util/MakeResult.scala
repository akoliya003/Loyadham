package util

import play.api.http.Writeable
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Result, Results}

object MakeResult {
  def fromStatus[C](code: Int, content: C = null)(implicit writeable: Writeable[C]): Result = {
    val statusResult = Results.Status(code)

    Option(content) match {
      case Some(c) => statusResult(c)
      case None => statusResult
    }
  }

  def errorFromStatus(code: Int, errorMessage: String = "Unknown error"): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.Status(code)(jsonErrorMessage)
  }

  // 200 OK: The request has succeeded.
  def ok(jsOkResult: JsValue): Result = {
    Results.Ok(jsOkResult)
  }

  // 204 No Content: There is no content to send for this request, but the headers may be useful.
  def noContent: Result = {
    Results.NoContent
  }

  // 400 Bad Request: This response means that server could not understand the request due to invalid syntax.
  def badRequest(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.BadRequest(jsonErrorMessage)
  }

  // 401 Unauthorized: Unauthorized (i.e. non-admin user trying to access admin-only data) or unathenticated (i.e. failing to provide Authorization header)
  def unauthorized(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.Unauthorized(jsonErrorMessage)
  }

  // 403 Forbidden The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort.
  def forbidden(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.Forbidden(jsonErrorMessage)
  }

  // 404 Not Found: The server can not find requested resource.
  def notFound(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.NotFound(jsonErrorMessage)
  }

  // 409 Conflict: The request could not be completed due to a conflict with the current state of the resource. This code is only allowed in situations where it is expected that the user might be able to resolve the conflict and resubmit the request.
  def conflict(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.Conflict(jsonErrorMessage)
  }

  // 413 Payload Too Large: Request entity is larger than limits defined by server; the server might close the connection or return an Retry-After header field.
  def entityTooLarge(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.EntityTooLarge(jsonErrorMessage)
  }

  // 500 Internal Server Error: The server encountered an unexpected condition which prevented it from fulfilling the request.
  def internalServerError(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.InternalServerError(jsonErrorMessage)
  }

  // 502 Bad Gateway: This error response means that the server, while working as a gateway to get a response needed to handle the request, got an invalid response.
  def badGateway(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.BadGateway(jsonErrorMessage)
  }

  // 504 Gateway Timeout: This error response is given when the server is acting as a gateway and cannot get a response in time.
  def gatewayTimeout(errorMessage: String): Result = {
    val jsonErrorMessage = Json.toJson(Error(errorMessage))
    Results.GatewayTimeout(jsonErrorMessage)
  }
}
