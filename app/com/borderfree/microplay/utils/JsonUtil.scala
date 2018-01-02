package com.borderfree.microplay.utils

import play.api.libs.json._

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 2/26/2017
  * Time: 6:43 PM
  */
object JsonUtil {

  def oFormat[T](format:Format[T]) : OFormat[T] = {
    val oFormat: OFormat[T] = new OFormat[T](){
      override def writes(o: T): JsObject = format.writes(o).as[JsObject]
      override def reads(json: JsValue): JsResult[T] = format.reads(json)
    }
    oFormat
  }
}
