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
class AuthHeadersMaskerTest extends Specification {

  "AuthHeadersMasker" should {

    "mask Headers with Authorization" in {
      val result = AuthHeadersMasker.maskHeaders(Map("some_header"->"someVal","Authorization"-> "hideMe"))
      result === Map("some_header"->"someVal","Authorization"-> "******")
    }
    "mask Headers with Basic Authorization" in {
      val result = AuthHeadersMasker.maskHeaders(Map("some_header"->"someVal","Authorization"-> "Basic dXNlcl9uYW1lX3RvX2JlX3Nob3duOnBhc3N3b3JkX3RvX2hpZGU="))
      result === Map("some_header"->"someVal","Authorization"-> "Basic user_name_to_be_shown:****************")
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme