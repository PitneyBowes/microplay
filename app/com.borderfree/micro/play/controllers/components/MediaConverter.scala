package com.borderfree.micro.play.controllers.components

import javax.xml.bind.annotation.XmlRootElement

import org.json4s.native.JsonMethods._
import play.api.libs.json
import play.api.libs.json._

import scala.reflect.ClassTag
import scala.xml.NodeSeq

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 1/14/2016
 * Time: 6:00 PM
 */
trait MediaConverter
{

  def jsonToObject[REQ: json.Reads](jsValue: JsValue): REQ =
  {
    jsValue.validate[REQ].map
    {
      case deSerializedJsonReqObj => deSerializedJsonReqObj
    }.recoverTotal
    {
      e :JsError=> throw JsResultException(e.errors )
    }
  }

  def xml2json[REQ: Reads : ClassTag](xml: NodeSeq): JsValue = Json.parse(compact(org.json4s.native.JsonMethods.render(org.json4s.Xml.toJson(xml))))

  def json2xml[T: Writes](response: T, json: JsValue): NodeSeq = <root/>.copy(label = extractXmlRootName(response.getClass), child = org.json4s.Xml.toXml(org.json4s.native.JsonMethods.parse(json.toString())))

  def extractXmlRootName(clazz: Class[_]): String =
  {
    Option(clazz.getAnnotation(classOf[XmlRootElement])) match
    {
      case Some(a) => a.name()
      case None => clazz.getSimpleName match
      {
        case s => s.head.toLower + s.tail
      }
    }
  }
}
