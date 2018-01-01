package com.borderfree.micro.play.logging

import javax.inject.Inject

import com.borderfree.micro.play.configuration.AppConfiguration
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
  def getForwardCorrelationIdHeaderName(): String
  def getReturnedCorrelationIdHeaderName(): String
  def clear(): Unit
}

class Slf4JMDCManager @Inject()(appConfiguration: AppConfiguration) extends MDCManager
{
  val CORRELATION_ID = "CORRELATION_ID"
  lazy val ForwardCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.forward.header-name")
  lazy val ReturnedCorrelationIdHeaderName: String = appConfiguration.getString("micro.correlation.returned.header-name")

  override def putCorrelationId(maybeCorrelationId: Option[String]) = {
    val correlationId = maybeCorrelationId.getOrElse(RequestIdGenerator.generate())
    MDC.put(CORRELATION_ID, CORRELATION_ID + "=" + correlationId)
    correlationId
  }

  override def getCorrelationId() = {
    Option(MDC.get(CORRELATION_ID)) match{
      case Some(correlationIdEntry)=>correlationIdEntry.replace(CORRELATION_ID+"=","")
      case None => putCorrelationId(None)
    }
  }

  override def clear() = {
    MDC.clear()
  }

  override def getForwardCorrelationIdHeaderName() = ForwardCorrelationIdHeaderName
  override def getReturnedCorrelationIdHeaderName() = ReturnedCorrelationIdHeaderName
}
