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

package com.pb.microplay.controllers

import com.pb.microplay.common.PlayFakeAppSpecification
import play.api.test._

class HealthCheckControllerTest extends PlayFakeAppSpecification{
  sequential
  "HealthCheckController" should {
    "health end point" in new WithApplication(getFakeApp) {
      val Some(result) = route(app, FakeRequest(GET, "/health"))
      status(result) mustEqual OK
      contentAsString(result) mustEqual "I'm Ok. Thanks for checking."
    }

    "env end point" in new WithApplication(getFakeApp)  {
      val Some(result) = route(app, FakeRequest(GET, "/env"))
      status(result) mustEqual OK
      contentAsString(result) mustEqual "this is test env"
    }
    "build info status end point" in new WithApplication(getFakeApp)  {
      val Some(result) = route(app, FakeRequest(GET, "/status"))
      status(result) mustEqual OK
      contentAsString(result) must contain("\"buildTime\"")
    }
  }

}
