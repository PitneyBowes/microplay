package com.borderfree.microplay.error

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/13/2017
  * Time: 3:26 PM
  */
case class RestClientException(httpStatusCode: Int, uriSuffix: String, message: String, errors:Option[List[AppError]], ex: Option[Throwable]=None) extends Exception(message)

/**
  * Internal error description. should not be exposed by external api's
  */
case class AppError(errorCode: String, errorMessage: String, fieldPath: Option[String]=None, fieldValue:Option[String]=None)