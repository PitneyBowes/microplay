package com.borderfree.microplay.logging

import com.fasterxml.jackson.databind.MappingJsonFactory
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
    factory
  }
}


