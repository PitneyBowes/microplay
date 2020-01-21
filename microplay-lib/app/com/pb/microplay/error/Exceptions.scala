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

package com.pb.microplay.error

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/13/2017
  * Time: 3:26 PM
  */
case class RestClientException(httpStatusCode: Int, uriSuffix: String, message: String, errors:Option[List[AppError]], ex: Option[Throwable]=None) extends Exception(message)

/**
  * Internal error description. should not be exposed by external api's
  */
case class AppError(errorCode: String, errorMessage: String, fieldPath: Option[String]=None, fieldValue:Option[String]=None)