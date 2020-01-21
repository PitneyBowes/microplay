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
