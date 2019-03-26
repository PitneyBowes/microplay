package com.borderfree.microplay.logging

import play.api.Logger

/**
 * Created by IntelliJ IDEA
 * User: Anatoly.Libman
 * Date: 1/14/16
 * Time: 5:40 PM
 */
object LoggerUtils {

  implicit class LoggerEnhancer(logger: Logger) {
     @Deprecated
    def debug(keyValueList: (String, String)*): Unit = logger.debug(buildMessage(keyValueList:_*))
    @Deprecated
    def info(keyValueList: (String, String)*): Unit = logger.info(buildMessage(keyValueList:_*))
    @Deprecated
    def warn(keyValueList: (String, String)*): Unit = logger.warn(buildMessage(keyValueList:_*))
    @Deprecated
    def error(keyValueList: (String, String)*): Unit = logger.error(buildMessage(keyValueList:_*))
    private def buildMessage(keyValueList: (String, String)*): String = {
      val message = new StringBuilder("")
      for (keyValuePair <- keyValueList) {
        message.append(s"${keyValuePair._1}=${keyValuePair._2} ")
      }
      message.toString()
    }
  }
}
