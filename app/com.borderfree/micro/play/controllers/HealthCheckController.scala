package com.borderfree.micro.play.controllers

import javax.inject.{Inject, Singleton}

import com.borderfree.micro.play.configuration.AppConfiguration
import com.borderfree.micro.play.logging.LoggingAction
import com.borderfree.micro.play.services.BuildInfoLoader
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
