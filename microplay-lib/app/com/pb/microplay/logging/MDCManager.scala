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

import javax.inject.Inject

import com.pb.microplay.configuration.AppConfiguration
import com.google.inject.ImplementedBy
import org.slf4j.MDC

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 11/12/2017
  * Time: 12:22 PM
  */
@ImplementedBy(classOf[Slf4JMDCManager])
trait MDCManager
{
  def putCorrelationId(maybeCorrelationId: Option[String]=None):String
  def getCorrelationId(): String
  def getCorrelationKey(): String
  def getForwardCorrelationIdHeaderName(): String
  /** @return the removed correlationId if any
    */
  def clear(): Option[String]
}

class Slf4JMDCManager @Inject()(appConfiguration: AppConfiguration) extends MDCManager
{
  val CORRELATION_ID_KEY = "CORRELATION_ID"
  lazy val ForwardCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.forwarded.header-name")

  override def putCorrelationId(maybeCorrelationId: Option[String]=None) = {
    val correlationId = maybeCorrelationId.getOrElse(RequestIdGenerator.generate())
    MDC.put(CORRELATION_ID_KEY, correlationId)
    correlationId
  }

  override def getCorrelationId() = {
    Option(MDC.get(CORRELATION_ID_KEY)) match{
      case Some(correlationIdEntry)=>correlationIdEntry
      case None => putCorrelationId(None)
    }
  }

  override def clear(): Option[String] = {
    val optCorrelationId = Option(MDC.get(CORRELATION_ID_KEY))
    MDC.clear()
    optCorrelationId
  }

  override def getForwardCorrelationIdHeaderName() = ForwardCorrelationIdHeaderName

  override def getCorrelationKey() = CORRELATION_ID_KEY
}
