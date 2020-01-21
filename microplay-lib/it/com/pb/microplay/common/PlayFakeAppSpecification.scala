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

package com.pb.microplay.common

import com.typesafe.config.ConfigFactory
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.PlaySpecification
import play.api.{Configuration, Mode}

/**
  * Created by Rohi Fadlun on 5/16/2017.
  */

trait PlayFakeAppSpecification extends PlaySpecification {

  def getFakeApp = {
    new GuiceApplicationBuilder()
      .in(Mode.Test)
      .loadConfig(Configuration(ConfigFactory.load(s"conf/application-test.conf")))
      .build()
  }


}
