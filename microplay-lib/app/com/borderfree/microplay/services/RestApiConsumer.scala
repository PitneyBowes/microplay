package com.borderfree.microplay.services

import java.util.Calendar
import java.util.concurrent.TimeUnit

import com.borderfree.microplay.error.RestClientException
import com.borderfree.microplay.logging.{LogSupport, MDCManager}
import play.api.http.{HeaderNames, Status}
import play.api.libs.json._
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}
import play.mvc.Http.MimeTypes

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 11/14/2017
  * Time: 10:45 AM
  */
trait RestApiConsumer
{
  this: LogSupport =>

  protected implicit val ec: ExecutionContext
  protected val ws: WSClient
  protected val mdcManager:MDCManager
  protected val apiBaseUri: String
  protected val apiRequestTimeoutMillis: Long
  protected val ValidHttpResponses: Set[Int] = Set(Status.OK)

  implicit def jsWriteable[T](implicit wa: Writes[T], wjs: BodyWritable[JsValue]): BodyWritable[T] = wjs.map(a => Json.toJson(a))

  protected def executeGET[RES <: Any : Reads](apiMethodUriSuffix: String, queryParameters: (String, String)*): Future[RES] = {
    val request = prepareRequest(apiMethodUriSuffix)
    execute("GET", apiMethodUriSuffix, request.withQueryStringParameters(queryParameters: _*))
  }

  protected def executePOST[REQ <: Any : Writes, RES <: Any : Reads](apiMethodUriSuffix: String, Any: REQ): Future[RES] = {
    logger.info(s"api $apiMethodUriSuffix request: " + Json.toJson(Any))
    execute("POST", apiMethodUriSuffix, prepareRequest(apiMethodUriSuffix).withBody[REQ](Any).withHttpHeaders((HeaderNames.CONTENT_TYPE, MimeTypes.JSON)))
  }

  protected def execute[RES <: Any : Reads](httpMethod: String, apiMethodUriSuffix: String, wsRequest: WSRequest): Future[RES] = {
    val start = Calendar.getInstance().getTimeInMillis
    wsRequest.execute(httpMethod)
      .map[RES] {
      response: WSResponse =>
        val callDuration = Calendar.getInstance().getTimeInMillis - start
        response.status match {
          case status if ValidHttpResponses.contains(status) =>
            logger.debug(s"for $httpMethod to endpoint $apiMethodUriSuffix - received api response within $callDuration millis: ${response.body}")
            Json.parse(response.body).as[RES]

          case httpStatus =>
            logger.error(s"error calling api endpoint - $apiMethodUriSuffix. got response within $callDuration millis with status $httpStatus: ${response.body}")
            throw createExceptionFromErrorResponse(httpStatus, response, apiMethodUriSuffix)
        }
    }
  }

  protected def createExceptionFromErrorResponse[RES <: Any : Reads](httpStatus: Int, response: WSResponse, apiMethodUriSuffix: String): Exception =
  {
    RestClientException(httpStatus, apiMethodUriSuffix,s"error calling api endpoint - $apiMethodUriSuffix. got error status $httpStatus with error message ${response.body}",None)
  }

  protected def prepareRequest(apiMethodUriSuffix: String): WSRequest = {
    val wSRequest = ws.url(apiBaseUri + apiMethodUriSuffix)
      .withHttpHeaders((HeaderNames.ACCEPT, MimeTypes.JSON), (mdcManager.getForwardCorrelationIdHeaderName(), mdcManager.getCorrelationId()))
      .withRequestTimeout(FiniteDuration(apiRequestTimeoutMillis, TimeUnit.MILLISECONDS))
    wSRequest
  }

}
