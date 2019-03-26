package com.borderfree.microplay.logging

import akka.stream._
import akka.util.ByteString
import com.borderfree.microplay.configuration.AppConfiguration
import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

/**
  * Created by IntelliJ IDEA
  * User: Anatoly.Libman
  * Date: 1/14/16
  * Time: 1:23 PM
  */
class LoggingAction @Inject()(appConfiguration: AppConfiguration,parser: BodyParsers.Default)(implicit val mat: Materializer , implicit val ec: ExecutionContext) extends ActionBuilderImpl(parser) with LogSupport
{
  val REQUEST_BODY = "REQUEST_BODY"
  val RESPONSE_BODY = "RESPONSE_BODY"

  lazy val ExcludedUrisForResponseBodyTracing: Set[String] = appConfiguration.getOptional[String]("micro.trace.response-body.exclude-uris").map(_.split(",").toSet).toSet.flatten
  lazy val ExcludedUrisForRequestBodyTracing: Set[String] = appConfiguration.getOptional[String]("micro.trace.request-body.exclude-uris").map(_.split(",").toSet).toSet.flatten
  lazy val MaxResponseBodyBytesTraced: Int = appConfiguration.getInt("micro.trace.response-body.max-bytes")

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
  {
    if(shouldTraceRequestBody(request)){
      logAction(request,REQUEST_BODY,
        request.body match {
          case AnyContentAsEmpty => ""
          case body => body.toString
        }
      )
    }
    block(request) andThen {
      case Success(result) =>
        if (shouldTraceResponseBody(request)) {
          consumeResponseBody(result) map { bytesString =>
            logAction(request, actionType = RESPONSE_BODY, data = bytesString.decodeString(resolveCharset(result)))
          }
        }
    }
  }

  protected def shouldTraceResponseBody[A](request: Request[A]): Boolean = {
    logger.underlying.isDebugEnabled && !ExcludedUrisForResponseBodyTracing.contains(request.uri)
  }

  protected def shouldTraceRequestBody[A](request: Request[A]): Boolean = {
    logger.underlying.isDebugEnabled && !ExcludedUrisForRequestBodyTracing.contains(request.uri)
  }
  import net.logstash.logback.argument.StructuredArguments._

  protected def logAction[A](request: Request[A], actionType: String, data: String): Unit = {
    logger.debug("logAction {} {} {}",value("action",request.uri),value("actionType",actionType),keyValue("data",data))
  }


  protected def resolveCharset(result: Result): String = {
    val header = result.header.headers.getOrElse("Content-Type", "")
    if (header != "" && header.contains("; charset=")) {
      header.substring(header.indexOf("; charset=") + 10, header.length()).trim()
    }
    else {
      "UTF-8"
    }
  }
  protected def consumeResponseBody[A](result: Result): Future[ByteString] = {
      result.body.dataStream.map { bts =>
        if (bts.size > MaxResponseBodyBytesTraced) {
          bts.slice(0, MaxResponseBodyBytesTraced) ++ ByteString("...<LARGE_RESPONSE_TRIMMED>")
        }
        else{
          bts
        }
      }.runFold(ByteString.empty)(_ ++ _)
  }
}

