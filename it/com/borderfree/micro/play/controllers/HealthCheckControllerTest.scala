package com.pb.microplay.controllers

import com.pb.microplay.common.PlayFakeAppSpecification
import play.api.test._

class HealthCheckControllerTest extends PlayFakeAppSpecification{

  "HealthCheckController" should {
    "health end point" in new WithApplication(getFakeApp) {
      val Some(result) = route(app, FakeRequest(GET, "/health"))
      status(result) mustEqual OK
      contentAsString(result) mustEqual "I'm Ok. Thanks for checking."
    }

    "env end point" in new WithApplication(getFakeApp) {
      val Some(result) = route(app, FakeRequest(GET, "/env"))
      status(result) mustEqual OK
      contentAsString(result) mustEqual "this is test env"
    }
    "build info status end point" in new WithApplication(getFakeApp) {
      val Some(result) = route(app, FakeRequest(GET, "/status"))
      status(result) mustEqual OK
      contentAsString(result) must contain("\"builtAtMillis\"")
    }
  }

}
