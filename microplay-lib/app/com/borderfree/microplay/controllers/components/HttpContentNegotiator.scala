package com.borderfree.microplay.controllers.components

import java.util.Locale

import play.api.libs.json
import play.api.libs.json.{JsValue, Json, Reads, Writes}
import play.api.mvc._

import scala.reflect.ClassTag
import scala.xml.NodeSeq

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 1/14/2016
 * Time: 6:13 PM
 */
trait HttpContentNegotiator extends AcceptExtractors
{
  this: Results with MediaConverter =>

  private val bodyParsers: PlayBodyParsers = play.api.mvc.BodyParsers.parse
  private val jsonAndXmlBodyParser: BodyParser[Any] = bodyParsers.using {
    request =>
      request.contentType.map(_.toLowerCase(Locale.ENGLISH)) match
      {
        case Some("application/xml") | Some("text/xml") => bodyParsers.xml
        case _ => bodyParsers.tolerantJson;
      }
  }

  def bodyParser(): BodyParser[Any] = jsonAndXmlBodyParser

  def bodyConverter[REQ: json.Reads : ClassTag](request: Request[Any]): REQ = deSerialize(request.body)

  def deSerialize[REQ: Reads : ClassTag](parsedMsg: Any): REQ =
  {
    parsedMsg match
    {
      case json: JsValue => jsonToObject[REQ](json)

      case xml: NodeSeq =>
        val rootName: String = extractXmlRootName(scala.reflect.classTag[REQ].runtimeClass)
        jsonToObject[REQ](xml2json(xml).\(rootName).toOption match {//deSerialize xml via json, an inefficient workaround for lacking auto conversions support in Play. assuming json is the main transport medium
          case Some(jsObj)=>jsObj
          case _ => throw new Exception("Expecting XML request with root element: "+rootName)
        })
    }
  }

  def renderResult[T: Writes](httpStatus: Status, response: T)(implicit request: RequestHeader): Result = serialize(response, request).fold(x => httpStatus(x), x => httpStatus(x))

  def serialize[T: Writes](response: T, request: RequestHeader): Either[JsValue, NodeSeq] =
  {
    val json: JsValue = Json.toJson(response)
    request match
    {
      case Accepts.Json() => Left(json) //Json option should precede xml since 'Accept */*' - also falls into Accept.Xml/Json()
      case Accepts.Xml() => Right(json2xml(response, json))
      case _ => Left(json)
    }
  }
}
