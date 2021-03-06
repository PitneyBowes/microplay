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

package com.pb.microplay.configuration

import javax.inject.{Inject, Singleton}
import com.pb.microplay.logging.LogSupport
import com.google.inject.ImplementedBy
import com.pb.microplay.logging.LogSupport
import com.typesafe.config.Config
import play.api.{ConfigLoader, Configuration}

import scala.concurrent.duration.{Duration, FiniteDuration}
/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 12/30/2015
 * Time: 11:58 AM
 */
@ImplementedBy(classOf[PlayAppConfiguration])
trait AppConfiguration
{
  def isEmpty: Boolean
  def isDevEnv: Boolean
  def getConfig(key: String): AppConfiguration
  def getUnderlyingConfig(key: String): Config
  def getStringList(key: String): Seq[String]
  def getString(key: String): String
  def getInt(key: String): Int
  def getBoolean(key: String): Boolean
  def getMilliseconds(key: String): Long
  def get[A](key: String)(implicit loader: ConfigLoader[A]): A
  def getOptional[A](key: String)(implicit loader: ConfigLoader[A]): Option[A]
  def getDuration(key: String): Duration
  def getFiniteDuration(key: String): FiniteDuration
}

//Thin wrapper around Play configuration. motivation: decouple from play configuration api as much as possible.
@Singleton
class PlayAppConfiguration @Inject()(configuration: Configuration) extends AppConfiguration with LogSupport {
  import play.api.ConfigLoader._

  override def isEmpty: Boolean = configuration == null

  override def getConfig(key: String): AppConfiguration = new PlayAppConfiguration(configuration.get[Configuration](key))

  override def getUnderlyingConfig(key: String): Config = configuration.get[Configuration](key).underlying

  override def getString(key: String): String = configuration.get[String](key)

  override def getBoolean(key: String): Boolean = configuration.get[Boolean](key)

  override def getInt(key: String): Int = configuration.get[Int](key)

  override def getStringList(key: String): Seq[String] = configuration.get[Seq[String]](key)

  override def isDevEnv: Boolean = this.getBoolean("micro.is-dev-env")

  override def getMilliseconds(key: String): Long = configuration.getMillis(key)

  override def get[A](key: String)(implicit loader: ConfigLoader[A]): A = configuration.get[A](key)

  override def getOptional[A](key: String)(implicit loader: ConfigLoader[A]): Option[A] = configuration.getOptional[A](key)

  override def getDuration(key: String): Duration = configuration.get[Duration](key)
  override def getFiniteDuration(key: String): FiniteDuration = configuration.get[FiniteDuration](key)
}
