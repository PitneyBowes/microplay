package com.borderfree.microplay.logging

import akka.stream._
import com.borderfree.microplay.configuration.AppConfiguration
import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by IntelliJ IDEA
  * User: Anatoly.Libman
  * Date: 1/14/16
  * Time: 1:23 PM
  */
class LoggingAction @Inject()(appConfiguration: AppConfiguration,parser: BodyParsers.Default, mdcManager:MDCManager)(implicit val mat: Materializer , implicit val ec: ExecutionContext) extends ActionBuilderImpl(parser) with LogSupport
{
  val REQUEST_BODY = "REQUEST_BODY"
  val RESPONSE_BODY = "RESPONSE_BODY"
  lazy val RequestedCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.requested.header-name")
  lazy val ExcludedUrisForResponseBodyTracing: Set[String] = appConfiguration.getOptional[String]("micro.trace.response-body.exclude-uris").map(_.split(",").toSet).toSet.flatten
  lazy val ExcludedUrisForRequestBodyTracing: Set[String] = appConfiguration.getOptional[String]("micro.trace.request-body.exclude-uris").map(_.split(",").toSet).toSet.flatten

  val FormatMsg: (String, String, String) => String = (action: String, actionType: String, data: String)=>{
    s"action=$action actionType=$actionType data=$data"
  }

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
  {
    mdcManager.putCorrelationId(request.headers.get(RequestedCorrelationIdHeaderName)) //todo add a configuration option to ignore received correlationId and set a fresh one instead ( might be useful in external apis)
    if(shouldTraceRequestBody(request)){
      logAction(request,REQUEST_BODY, request.body match {
        case AnyContentAsEmpty => ""
        case body => body.toString
      })
    }
    val resultWithCorrelationIdHeader = block(request) andThen {
      case Success(result) =>
        result.withHeaders((mdcManager.getReturnedCorrelationIdHeaderName(), mdcManager.getCorrelationId()))
      case Failure(ex) =>
        logger.error("exception while invoking request handler", ex)
        mdcManager.clear()
    }
    resultWithCorrelationIdHeader foreach{ result =>
      if (shouldTraceResponseBody(request)){
        result.body.consumeData map { bytesString =>
          logAction(request,actionType = RESPONSE_BODY, data = bytesString.decodeString(resolveCharset(result)))
        }
      }
    }
    mdcManager.clear()
    resultWithCorrelationIdHeader
  }

  protected def shouldTraceResponseBody[A](request: Request[A]): Boolean = {
    logger.isDebugEnabled && !ExcludedUrisForResponseBodyTracing.contains(request.uri)
  }

  protected def shouldTraceRequestBody[A](request: Request[A]): Boolean = {
    logger.isDebugEnabled && !ExcludedUrisForRequestBodyTracing.contains(request.uri)
  }

  protected def logAction[A](request: Request[A], actionType: String, data: String): Unit =
  {
    logger.debug(FormatMsg(request.uri, actionType, data))
  }

  protected def resolveCharset(result: Result): String =
  {
    val header = result.header.headers.getOrElse("Content-Type", "")
    if (header != "" && header.contains("; charset=")) {
      header.substring(header.indexOf("; charset=") + 10, header.length()).trim()
    }
    else {
      "UTF-8"
    }
  }

}
