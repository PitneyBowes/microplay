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
