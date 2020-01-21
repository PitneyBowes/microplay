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

package com.pb.microplay.controllers

import akka.stream.Materializer
import javax.inject.{Inject, Singleton}
import play.api.http.HeaderNames
import play.filters.gzip.GzipFilter

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 11/6/2018
  * Time: 3:08 PM
  *
  * Returns gzip response only if explicitly requested by api consumer
  */
@Singleton
class GZipContentNegotiationFilter @Inject()(implicit mat: Materializer)  extends GzipFilter(shouldGzip = (request, _) => request.headers.getAll(HeaderNames.ACCEPT_ENCODING).contains("gzip"))(mat)
