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

import javax.inject.Inject

import com.pb.microplay.configuration.AppConfiguration
import com.google.inject.ImplementedBy

import scala.reflect.ClassTag

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/1/2018
  * Time: 3:46 PM
  */
@ImplementedBy(classOf[BuildInfoLoaderImpl])
trait BuildInfoLoader{
  def getBuildInfo: BuildInfoMeta
}

class BuildInfoLoaderImpl @Inject()(appConfiguration: AppConfiguration, env:play.Environment ) extends BuildInfoLoader {

  private lazy val buildInfoClassName: String = appConfiguration.getOptional[String]("micro.build.info").getOrElse("com.pb.microplay.BuildInfo")

  def getBuildInfo: BuildInfoMeta ={
    val buildInfoClass = env.classLoader().loadClass(buildInfoClassName)
    companionObj(ClassTag(buildInfoClass)).asInstanceOf[BuildInfoMeta]
  }
  private def companionObj[T](implicit tag : reflect.ClassTag[T] ): AnyRef = {
    val c = env.classLoader().loadClass(buildInfoClassName + "$")
//    val c = env.classLoader().loadClass(tag.runtimeClass.getName + "$")
    c.getField("MODULE$").get(c)
  }
}

trait BuildInfoMeta{
  val toJson: String
  val toMap: Map[String, Any]
}