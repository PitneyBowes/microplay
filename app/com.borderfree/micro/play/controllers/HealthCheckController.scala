package com.borderfree.micro.play.controllers

import javax.inject.{Inject, Singleton}

import com.borderfree.micro.play.configuration.AppConfiguration
import com.borderfree.micro.play.logging.LoggingAction
import io.swagger.annotations._
import play.api.Configuration
import play.api.mvc._

import scala.reflect.ManifestFactory

@Singleton
@Api(value = "Health/Status checks")
class HealthCheckController @Inject()(val loggingAction:LoggingAction,configuration:Configuration,appConfiguration: AppConfiguration, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {

  lazy val buildInfoLoader:BuildInfoLoader= new BuildInfoLoader(appConfiguration.getOptional[String]("micro.build.info").getOrElse("com.borderfree.micro.play.BuildInfo"))

  @ApiOperation(nickname = "health", value = "get health status", notes = "Returns health status", response = classOf[String], httpMethod = "GET")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "OK status")))
  def health() = loggingAction {
    Ok("I'm Ok. Thanks for checking.")
  }

  @ApiOperation(nickname = "env", value = "get env type message", notes = "Returns env type welcome message", response = classOf[String],httpMethod = "GET", produces = "text/plain")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "environment type")))
  def env() = loggingAction {
    Ok(appConfiguration.getString("env.message"))
  }

  @ApiOperation(nickname = "status", value = "get build information", notes = "Returns build information", response = classOf[String],httpMethod = "GET", produces = "application/json")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "build info")))
  def status() = loggingAction {
    Ok(buildInfoLoader.getBuildInfoJson).as(JSON)
  }
}

class BuildInfoLoader(buildInfoClassName:String){
  def getBuildInfoJson: String =
  {
    val buildInfoClass = this.getClass.getClassLoader.loadClass(buildInfoClassName)
    val buildInfoObj = companionObj(ManifestFactory.classType(buildInfoClass))
    buildInfoClass.getMethod("toJson").invoke(buildInfoObj).toString
  }
  private def companionObj[T](implicit man: Manifest[T]): AnyRef = {
    val c = Class.forName(man.erasure.getName + "$")
    c.getField("MODULE$").get(c)
  }
}