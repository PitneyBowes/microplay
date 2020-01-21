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

import javax.inject.{Inject, Singleton}

import com.pb.microplay.configuration.AppConfiguration
import com.pb.microplay.logging.LoggingAction
import com.pb.microplay.services.BuildInfoLoader
import io.swagger.annotations._
import play.api.mvc._

@Singleton
@Api(value = "Health/Status checks")
class HealthCheckController @Inject()(val loggingAction:LoggingAction,appConfiguration: AppConfiguration,buildInfoLoader:BuildInfoLoader, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {

  @ApiOperation(nickname = "health", value = "get health status", notes = "Returns health status", response = classOf[String], httpMethod = "GET")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "OK status")))
  def health() = loggingAction {
    Ok("I'm Ok. Thanks for checking.")
  }

  @ApiOperation(nickname = "env", value = "get env type message", notes = "Returns env type welcome message", response = classOf[String],httpMethod = "GET", produces = "text/plain")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "environment type")))
  def env() = loggingAction {
    Ok(appConfiguration.getString("micro.env.message"))
  }

  @ApiOperation(nickname = "status", value = "get build information", notes = "Returns build information", response = classOf[String],httpMethod = "GET", produces = "application/json")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "build info")))
  def status() = loggingAction {
    Ok(buildInfoLoader.getBuildInfo.toJson).as(JSON)
  }
}
