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

package com.pb.microplay.controllers

import java.net.InetAddress

import akka.stream.Materializer
import com.pb.microplay.configuration.AppConfiguration
import com.pb.microplay.logging.{LogSupport, MDCManager}
import com.pb.microplay.utils.AuthHeadersMasker
import javax.inject.Inject
import net.logstash.logback.argument.StructuredArguments._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 3/19/2018
  * Time: 4:12 PM
  */

class HttpTraceFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext, mdcManager: MDCManager, appConfiguration: AppConfiguration) extends Filter with LogSupport
{
  lazy val ProcessingTimeHeaderName: String = appConfiguration.getString("micro.processing-time.header.name")
  lazy val ProcessingTimeHeaderEnabled: Boolean = appConfiguration.getBoolean("micro.processing-time.header.enabled")

  lazy val HandlingHostHeaderName: String = appConfiguration.getString("micro.handling-host.header.name")
  lazy val HandlingHostHeaderEnabled: Boolean = appConfiguration.getBoolean("micro.handling-host.header.enabled")

  lazy val RequestCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.request.header.name")
  lazy val RequestCorrelationIdHeaderEnabled: Boolean = appConfiguration.getBoolean("micro.correlation.request.header.enabled")

  lazy val ReturnedCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.returned.header.name")
  lazy val ReturnedCorrelationIdHeaderEnabled: Boolean = appConfiguration.getBoolean("micro.correlation.returned.header.enabled")

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] =
  {
    val startTime = System.currentTimeMillis
    mdcManager.putCorrelationId(
      if (RequestCorrelationIdHeaderEnabled) {
        requestHeader.headers.get(RequestCorrelationIdHeaderName)
      }
      else {
        None
      }
    )
    nextFilter(requestHeader)
      .map (
        addHandlingTimeHeader(_,System.currentTimeMillis - startTime)
      )
      .map(addHandlingHostHeader)
      .map(result => {
        val fullResult = addCorrelationId(result, mdcManager.getCorrelationId())
        logger.debug("{} {} {} {}",
          keyValue("requestMethod",requestHeader.method+" "+requestHeader.uri),
          keyValue("requestHeaders", AuthHeadersMasker.maskHeaders(Map(requestHeader.headers.headers: _*))),
          keyValue("responseStatus",fullResult.header.status),
          keyValue("responseHeaders",fullResult.header.headers))
        mdcManager.clear()
        fullResult
      }
      )
  }

  private def addCorrelationId(result: Result, correlationId: String) = {

    if (ReturnedCorrelationIdHeaderEnabled) {
      result.withHeaders(ReturnedCorrelationIdHeaderName -> correlationId)
    } else {
      result
    }
  }

  private def addHandlingTimeHeader(result: Result, handlingDurationMillis: Long) = {
    if (ProcessingTimeHeaderEnabled) {
      result.withHeaders(ProcessingTimeHeaderName -> handlingDurationMillis.toString )
    }
    else {
      result
    }
  }

  private def addHandlingHostHeader(result: Result) = {
    if (HandlingHostHeaderEnabled) {
      result.withHeaders(HandlingHostHeaderName -> InetAddress.getLocalHost.getHostName)
    }
    else {
      result
    }
  }
}