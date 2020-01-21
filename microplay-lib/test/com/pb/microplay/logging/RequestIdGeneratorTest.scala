
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

import org.specs2.mutable.Specification

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/17/2017
  * Time: 12:04 PM
  */
class RequestIdGeneratorTest extends Specification
{

  "RequestIdGenerator" should
    {

      "generate id with 16 chars" in
        {
          val result = RequestIdGenerator.generate()
          result.length === 16
        }

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme