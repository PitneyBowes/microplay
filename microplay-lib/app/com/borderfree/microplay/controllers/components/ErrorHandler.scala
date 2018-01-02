package com.borderfree.microplay.controllers.components

import com.borderfree.microplay.logging.LogSupport
import play.api.libs.json.Writes
import play.api.mvc.{RequestHeader, Result, Results}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/26/2016
  * Time: 9:25 AM
  */


trait ErrorHandler[ERR]{

  this: Results with HttpContentNegotiator with LogSupport =>

  implicit val errWriter: Writes[ERR]

  def mapExceptionToErrorResponse(err: Throwable): (Status, ERR)

  def renderErrorResponse(err: Throwable)(implicit request: RequestHeader): Result =
  {
    logger.error("handling error", err)
    val tuple: (Status, ERR) = mapExceptionToErrorResponse(err)
    tuple match
    {
      case (httpStatus, errorResponse) => renderResult(httpStatus, errorResponse)
    }
  }
}
