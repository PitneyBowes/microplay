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

import java.util.UUID


/**
 * Created by IntelliJ IDEA
 * User: Anatoly.Libman
 * Date: 1/17/16
 * Time: 3:08 PM
 */
trait RequestIdGenerator {
  def generate(): String
}

object RequestIdGenerator extends RequestIdGenerator
{
  val idLength = 16

  def generate(): String =
  {
    var generatedString = UUID.randomUUID().getMostSignificantBits.toHexString
    for (i <- generatedString.length until idLength)
    {
      generatedString = generatedString + "0"
    }
    generatedString
  }
}
