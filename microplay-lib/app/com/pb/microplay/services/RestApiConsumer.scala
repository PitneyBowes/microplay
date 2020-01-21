/*
 * Copyright (c) 2020 Pitney Bowes Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.pb.microplay.services

import java.util.Calendar
import java.util.concurrent.TimeUnit

import com.pb.microplay.error.RestClientException
import com.pb.microplay.logging.{LogSupport, MDCManager}
import play.api.http.{HeaderNames, Status}
import play.api.libs.json._
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}
import play.mvc.Http.MimeTypes

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import net.logstash.logback.argument.StructuredArguments._

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

  protected def executePOST[REQ <: Any : Writes, RES <: Any : Reads](apiMethodUriSuffix: String, anyReq: REQ): Future[RES] = {
    executeRequestWithBody("POST",apiMethodUriSuffix, anyReq)
  }

  protected def executePUT[REQ <: Any : Writes, RES <: Any : Reads](apiMethodUriSuffix: String, anyReq: REQ): Future[RES] = {
    executeRequestWithBody("PUT",apiMethodUriSuffix, anyReq)
  }

  private def executeRequestWithBody[RES <: Any : Reads, REQ <: Any : Writes](httpMethod: String, apiMethodUriSuffix: String, anyReq: REQ) = {
    logger.debug("api", keyValue(httpMethod +" "+apiMethodUriSuffix+" request: ",anyReq))
      execute(httpMethod, apiMethodUriSuffix, prepareRequest(apiMethodUriSuffix).withBody[REQ](anyReq))
  }

  protected def execute[RES <: Any : Reads](httpMethod: String, apiMethodUriSuffix: String, wsRequest: WSRequest): Future[RES] = {
    val start = Calendar.getInstance().getTimeInMillis
    wsRequest.execute(httpMethod)
      .map[RES] {
      response: WSResponse =>
        val callDuration = Calendar.getInstance().getTimeInMillis - start
        response.status match {
          case status if ValidHttpResponses.contains(status) =>
            val successResponseMsg = s"for $httpMethod to endpoint $apiMethodUriSuffix - received api response within $callDuration millis"
            val jsonResponse = Json.parse(response.body).as[RES]
            if (shouldTraceResponseBody(apiMethodUriSuffix))
              logger.debug(successResponseMsg , keyValue("response", jsonResponse))
            else
              logger.debug(successResponseMsg)
            jsonResponse
          case httpStatus =>
            logger.debug(s"error calling api endpoint - $apiMethodUriSuffix. got response within $callDuration millis. {} {}",keyValue("status",httpStatus), keyValue("response",response.body))
            throw createExceptionFromErrorResponse(httpStatus, response, apiMethodUriSuffix)
        }
    }
  }

  protected def shouldTraceResponseBody(apiMethodUriSuffix: String): Boolean = true

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
