package com.borderfree.microplay.utils

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
  implicit def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value]
  {
    def reads(json: JsValue): JsResult[E#Value] = json match
    {
      case JsString(s) =>
      {
        try
        {
          JsSuccess(enum.withName(s))
        }
        catch
          {
            case _: NoSuchElementException => JsError(s"expecting one of: ${enum.values.mkString(",")} - but instead got: '$s'")
          }
      }
      case _ => JsError("String value expected")
    }
  }
  implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }
  implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumReads(enum), enumWrites)
  }
  def findEnum[T <: Enumeration](s: String, enum: T): Option[T#Value] = enum.values.find(_.toString == s)
  def toEnum[T <: Enumeration](s: String, enum: T): T#Value = enum.withName(s)

}
