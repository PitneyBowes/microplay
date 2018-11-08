package com.borderfree.microplay.controllers

import java.net.InetAddress

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
  lazy val HandlingHostHeaderName: String = appConfiguration.getString("micro.handling-host.header.name")
  lazy val HandlingHostHeaderEnabled: Boolean = appConfiguration.getBoolean("micro.handling-host.header.enabled")

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] =
  {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map
    { result =>
      val handlingDurationMillis = System.currentTimeMillis - startTime
      logger.info(s"RequestMethod=${requestHeader.method} ${requestHeader.uri} RequestDuration=${handlingDurationMillis}ms ResponseStatus=${result.header.status} RequestHeaders=${requestHeader.headers.headers.mkString(",")} ResponseHeaders=${result.header.headers.mkString(",")}")
      addHandlingHostHeader(addHandlingTimeHeader(result, handlingDurationMillis))
    }
  }

  private def addHandlingTimeHeader(result: Result, requestTime: Long) = {
    if (ProcessingTimeHeaderEnabled) {
      result.withHeaders(ProcessingTimeHeaderName -> requestTime.toString)
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