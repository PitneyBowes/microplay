package com.pb.microplay.configuration

import controllers.ApiHelpController
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}
import play.modules.swagger.SwaggerPlugin

class CustomSwaggerModule extends Module {

  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] =  Seq(
    bind[SwaggerPlugin].to[SwaggerPluginExt].eagerly(),
    bind[ApiHelpController].toSelf.eagerly()
  )

}
