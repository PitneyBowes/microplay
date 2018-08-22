package com.borderfree.microplay.logging

import javax.inject.Inject

import com.borderfree.microplay.configuration.AppConfiguration
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
  def putCorrelationId(maybeCorrelationId: Option[String]):String
  def getCorrelationId(): String
  def getCorrelationKey(): String
  def getForwardCorrelationIdHeaderName(): String
  def getReturnedCorrelationIdHeaderName(): String
  def clear(): Unit
}

class Slf4JMDCManager @Inject()(appConfiguration: AppConfiguration) extends MDCManager
{
  val CORRELATION_ID_KEY = "CORRELATION_ID"
  lazy val ForwardCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.forward.header-name")
  lazy val ReturnedCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.returned.header-name")

  override def putCorrelationId(maybeCorrelationId: Option[String]) = {
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

  override def clear() = {
    MDC.clear()
  }

  override def getForwardCorrelationIdHeaderName() = ForwardCorrelationIdHeaderName
  override def getReturnedCorrelationIdHeaderName() = ReturnedCorrelationIdHeaderName

  override def getCorrelationKey() = CORRELATION_ID_KEY
}
