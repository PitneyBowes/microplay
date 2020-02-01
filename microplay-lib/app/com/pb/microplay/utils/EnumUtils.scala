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

package com.pb.microplay.utils

import play.api.libs.json._

import scala.language.implicitConversions
/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 1/19/2016
 * Time: 4:32 PM
 */
object EnumUtils
{
  implicit def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = {
    case JsString(s) => {
      try {
        JsSuccess(enum.withName(s))
      }
      catch {
        case _: NoSuchElementException => JsError(s"expecting one of: ${enum.values.mkString(",")} - but instead got: '$s'")
      }
    }
    case _ => JsError("String value expected")
  }
  implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = (v: E#Value) => JsString(v.toString)
  implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumReads(enum), enumWrites)
  }
  def findEnum[T <: Enumeration](s: String, enum: T): Option[T#Value] = enum.values.find(_.toString == s)
  def toEnum[T <: Enumeration](s: String, enum: T): T#Value = enum.withName(s)

}
