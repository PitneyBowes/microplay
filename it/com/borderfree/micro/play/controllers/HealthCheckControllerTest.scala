package com.borderfree.micro.play.controllers

import com.borderfree.micro.play.common.PlayFakeAppSpecification
import play.api.test._

class HealthCheckControllerTest extends PlayFakeAppSpecification{

  "HealthCheckController" should {
//    "health end point" in new WithApplication(getFakeApp) {
    "health end point" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/health"))
      status(result) mustEqual OK
      contentAsString(result) mustEqual "I'm Ok. Thanks for checking."
    }

    "env end point" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/env"))
      status(result) mustEqual OK
      contentAsString(result) mustEqual "default env"
    }
    "build info status end point" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/status"))
      status(result) mustEqual OK
      contentAsString(result) must contain("\"builtAtMillis\"")
    }
  }

}
