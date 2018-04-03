package com.borderfree.microplay.logging

import javax.inject.Inject

import akka.stream._
import com.borderfree.microplay.logging.LoggerUtils._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by IntelliJ IDEA
  * User: Anatoly.Libman
  * Date: 1/14/16
  * Time: 1:23 PM
  */
class LoggingAction @Inject()(implicit val mat: Materializer , implicit val ec: ExecutionContext,parser: BodyParsers.Default, mdcManager:MDCManager) extends ActionBuilderImpl(parser) with LogSupport
{

  val REQUEST_BODY = "REQUEST_BODY"
  val RESPONSE_BODY = "RESPONSE_BODY"

  val FormatMsg: (String, String, String) => String = (action: String, actionType: String, data: String)=>{
    s"action=$action actionType=$actionType data=$data"
  }

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] =
  {

    def retrieveCharset(result: Result): String =
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

    def logAction(actionType: String, data: String) =
    {
      if(!request.uri.endsWith( "health")) {
        logger.info(FormatMsg(request.uri, actionType, data))
      }
    }
//    mdcManager.setCorrelationId(request.headers.get(X_CORRELATION_ID_HEADER))     //todo set correlationId as recieved from caller header if exists. add a configuration option to ignore received correlationId ( might be useful in external apis)
    mdcManager.putCorrelationId(None)
    logAction(REQUEST_BODY,
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
                val body = bytesString.decodeString(retrieveCharset(result))
                logAction(actionType = RESPONSE_BODY, data = body)
              case Failure(e) =>
                logger.error("error occurred:", e)
            }
        case Failure(e) =>
          logger.error("error occurred:", e)
      }
    mdcManager.clear()
    resultWithCorrelationIdHeader
  }
}
