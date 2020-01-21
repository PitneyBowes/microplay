package com.pb.microplay.configuration

import com.typesafe.config.ConfigFactory
import play.api.inject.guice._
import play.api.{ApplicationLoader, Configuration}

/**
 * Created by IntelliJ IDEA
 * User: Anatoly.Libman
 * Date: 12/28/2015
 * Time: 11:06 PM
 */

class CustomApplicationLoader extends GuiceApplicationLoader() {

  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
    val environment = Option(System.getProperty("ENV")).getOrElse(Option(System.getenv("ENV")).getOrElse("dev"))
    val environmentSpecificConfig = Configuration(ConfigFactory.load(s"application-$environment.conf"))
    val conf = context.initialConfiguration ++
      environmentSpecificConfig ++
      Configuration.from(Map("playRunningMode" -> context.environment.mode.toString))
    initialBuilder
      .in(context.environment)
      .loadConfig(conf)
      .overrides(overrides(context): _*)
  }
}
