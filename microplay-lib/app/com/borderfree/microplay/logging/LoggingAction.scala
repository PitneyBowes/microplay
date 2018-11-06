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
  lazy val ExlucdedUrisForResponseBodyTracing: Set[String] = appConfiguration.getString("micro.trace.response-body.exclude-uris").split(",").toSet

  val FormatMsg: (String, String, String) => String = (action: String, actionType: String, data: String)=>{
    s"action=$action actionType=$actionType data=$data"
  }

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] =
  {
    mdcManager.putCorrelationId(request.headers.get(RequestedCorrelationIdHeaderName)) //todo add a configuration option to ignore received correlationId and set a fresh one instead ( might be useful in external apis)
    logAction(request,REQUEST_BODY,
      {
        request.body match
        {
          case AnyContentAsEmpty => ""
          case body => body.toString
        }
      })

    val resultFuture = try
    {
      block(request)
    }
    catch
    {
      case e: Exception =>
        logger.error("error occurred:", e)
        mdcManager.clear()
        throw e
    }

    val resultWithCorrelationIdHeader = resultFuture map
      { result =>
        result.withHeaders((mdcManager.getReturnedCorrelationIdHeaderName(), mdcManager.getCorrelationId()))
      }

    resultWithCorrelationIdHeader onComplete
      {
        case Success(result) =>
          result.body.consumeData(mat) onComplete
            {
              case Success(bytesString) =>
                if (logger.isDebugEnabled && !ExlucdedUrisForResponseBodyTracing.contains(request.uri)){
                  val body = bytesString.decodeString(retrieveCharset(result))
                  logAction(request,actionType = RESPONSE_BODY, data =  body )
                }
              case Failure(e) =>
                logger.error("error occurred:", e)
            }
        case Failure(e) =>
          logger.error("error occurred:", e)
      }
    mdcManager.clear()
    resultWithCorrelationIdHeader
  }

  private def logAction[A](request: Request[A], actionType: String, data: String) =
  {
    if(!request.uri.endsWith( "health")) {
      logger.debug(FormatMsg(request.uri, actionType, data))
    }
  }
  private def retrieveCharset(result: Result): String =
  {
    val header = result.header.headers.getOrElse("Content-Type", "")
    if (header != "" && header.contains("; charset="))
    {
      header.substring(header.indexOf("; charset=") + 10, header.length()).trim()
    }
    else
    {
      "UTF-8"
    }
  }

}
