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

import com.pb.microplay.configuration.AppConfiguration
import com.pb.microplay.logging.{LogSupport, LoggingAction}
import com.pb.microplay.logging.{LogSupport, LoggingAction}

import scala.concurrent.ExecutionContext
//import com.mohiva.play.silhouette.api.Silhouette

import play.api.libs.json
import play.api.mvc._

import scala.concurrent.Future
import scala.reflect.ClassTag

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 1/7/2016
 * Time: 2:53 PM
 */
trait ApiControllerActions[ERR] extends LogSupport with HttpContentNegotiator with MediaConverter with ErrorHandler[ERR]
{
  this: BaseController =>
  implicit val ec: ExecutionContext
  val loggingAction:LoggingAction
//  val silhouette: Silhouette[JWTEnv]
  val configuration: AppConfiguration

  protected def apiAction[RES: json.Writes : ClassTag](apiCall: () => Future[RES]): Action[AnyContent] = {
    loggingAction.async { implicit request: Request[Any] => handleRequest(() => apiCall()) }
  }

  protected def apiAction[REQ: json.Reads : ClassTag, RES: json.Writes : ClassTag](apiCall: REQ => Future[RES]): Action[Any] = {
    loggingAction.async(bodyParser()) { implicit request: Request[Any] => handleRequest{() => apiCall(bodyConverter(request))} }
  }

//  object SecuredAction extends ActionBuilder[Request] {
//    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
//      request.headers.get("X-AUTH-TICKET") match {
//        case Some(dummyTicket) if "dummy" == dummyTicket && configuration.isDevEnv => block(request)
//        case _ => silhouette.SecuredAction.invokeBlock(request,block)
//      }
//    }
//  }
//
//  protected def securedApiAction[REQ: json.Reads : ClassTag, RES: json.Writes : ClassTag](apiCall: REQ => Future[RES]): Action[Any] = {
//    (loggingAction andThen SecuredAction).async(bodyParser) { implicit request: Request[Any] => handleRequest{ () => apiCall(bodyConverter(request))} }
//  }
//
  protected def handleRequest[RES: json.Writes : ClassTag](invokeService: () => Future[RES])(implicit request: Request[Any]): Future[Result] = {
    invokeService() map {apiResponse=> renderResult(Ok, apiResponse)} recover {
        case err: Throwable => renderErrorResponse(err)
      }
    }
}