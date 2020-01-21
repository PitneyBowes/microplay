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

package com.pb.microplay.services

import com.pb.microplay.configuration.PlayAppConfiguration
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/1/2018
  * Time: 4:07 PM
  */
class BuildInfoLoaderImplTest extends Specification with Mockito
{
  val playAppConfiguration: PlayAppConfiguration = mock[PlayAppConfiguration]
  val playEnv: play.Environment = mock[play.Environment]
  playAppConfiguration.getOptional[String]("micro.build.info") returns None
  playEnv.classLoader() returns this.getClass.getClassLoader
  
  val buildInfoLoaderImpl = new BuildInfoLoaderImpl(playAppConfiguration,playEnv)
  "BuildInfoLoaderImpl" should
    {
      "get Build Info" in
        {
          val result = buildInfoLoaderImpl.getBuildInfo
          result.toJson must contain("\"builtAtMillis\"")
          result.toMap.size must beGreaterThanOrEqualTo(5)
        }
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme