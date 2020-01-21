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

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/6/2017
  * Time: 10:41 AM
  */
object FutureUtils
{
  /**
    * Implicitly add to Option[Future] types a method - toFutureOption - to convert itself into Future[Option]
    */
  implicit class OptionalFuture[A](val optFuture: Option[Future[A]]){
    def toFutureOption(implicit ec: ExecutionContext): Future[Option[A]] =
      optFuture match {
        case Some(f) => f.map(Some(_))
        case None    => Future.successful(None)
      }
  }
}
