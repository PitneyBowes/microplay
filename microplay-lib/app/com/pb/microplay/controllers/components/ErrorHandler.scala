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

import com.pb.microplay.logging.LogSupport
import com.pb.microplay.logging.LogSupport
import play.api.libs.json.Writes
import play.api.mvc.{RequestHeader, Result, Results}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/26/2016
  * Time: 9:25 AM
  */


trait ErrorHandler[ERR]{

  this: Results with HttpContentNegotiator with LogSupport =>

  implicit val errWriter: Writes[ERR]

  def mapExceptionToErrorResponse(err: Throwable): (Status, ERR)

  def renderErrorResponse(err: Throwable)(implicit request: RequestHeader): Result =
  {
    logger.error("handling error", err)
    val (httpStatus, errorResponse) = mapExceptionToErrorResponse(err)
    renderResult(httpStatus, errorResponse)
  }
}
