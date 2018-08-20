package com.borderfree.microplay.controllers

import javax.inject.Inject

import akka.stream.Materializer
import com.borderfree.microplay.configuration.AppConfiguration
import com.borderfree.microplay.logging.{LogSupport, MDCManager}
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

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] =
  {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map
    { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      logger.debug(s"CORRELATION_ID=${result.header.headers.getOrElse(mdcManager.getReturnedCorrelationIdHeaderName(), "")} RequestMethod=${requestHeader.method} ${requestHeader.uri} RequestDuration=${requestTime}ms ResponseStatus=${result.header.status} RequestHeaders=${requestHeader.headers.headers.mkString(",")} ResponseHeaders=${result.header.headers.mkString(",")}")
      ProcessingTimeHeaderEnabled match
      {
        case true => result.withHeaders(ProcessingTimeHeaderName -> requestTime.toString)
        case false => result
      }
    }
  }
}