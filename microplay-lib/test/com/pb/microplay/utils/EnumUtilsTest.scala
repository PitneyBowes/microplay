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

package com.pb.microplay.utils

import org.specs2.mutable.Specification
import play.api.libs.json.{Format, JsString, JsSuccess}
/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/24/2019
  * Time: 11:35 AM
  */
class EnumUtilsTest extends Specification
{

  object Weekday extends Enumeration {
    val Monday = Value("MONDAY")
    val Other = Value("OTHER")
  }
  "findEnum " should {
    "find some exists num value if string matched" in {
      EnumUtils.findEnum("MONDAY", Weekday) === Some(Weekday.Monday)
    }
    "return None if string does not match any value" in {
      EnumUtils.findEnum("Monday", Weekday) === None
    }
  }
  "enumFormat" should {
    "return enum formatter" in {
      val result  = EnumUtils.enumFormat(Weekday)
      result.isInstanceOf[Format[Weekday.Value]] === true
    }
    "write enum" in {
      implicit val result: Format[Weekday.Value] = EnumUtils.enumFormat(Weekday)
      result.writes(Weekday.Monday) === JsString("MONDAY")
    }
    "read enum" in {
      implicit val result: Format[Weekday.Value] = EnumUtils.enumFormat(Weekday)
      result.reads(JsString("MONDAY")) === JsSuccess(Weekday.Monday)
    }
  }

}
