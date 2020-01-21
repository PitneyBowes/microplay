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

package com.pb.microplay.controllers.components

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
