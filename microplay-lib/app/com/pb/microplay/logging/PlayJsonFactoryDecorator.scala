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

package com.pb.microplay.logging

import com.fasterxml.jackson.databind.MappingJsonFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import net.logstash.logback.decorate.JsonFactoryDecorator
import play.api.libs.json.jackson.PlayJsonModule

// workaround for  registering the PlayJsonModule
// so that we use play json extensions to write pojo's to the log
// See CompositeJsonFormatter.createJsonFactory and usage of findAndRegisterJacksonModules
// https://github.com/logstash/logstash-logback-encoder#registering-jackson-modules

class PlayJsonFactoryDecorator extends JsonFactoryDecorator {
  override def decorate(factory: MappingJsonFactory): MappingJsonFactory =  {
    val mapper = factory.getCodec
    mapper.registerModule(PlayJsonModule)
    mapper.registerModule(DefaultScalaModule)
    factory
  }
}


