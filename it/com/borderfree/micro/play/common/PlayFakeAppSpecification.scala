package com.borderfree.micro.play.common

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
