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

    def debug(keyValueList: (String, String)*) = logger.debug(buildMessage(keyValueList:_*))
    def info(keyValueList: (String, String)*) = logger.info(buildMessage(keyValueList:_*))
    def warn(keyValueList: (String, String)*) = logger.warn(buildMessage(keyValueList:_*))
    def error(keyValueList: (String, String)*) = logger.error(buildMessage(keyValueList:_*))
    private def buildMessage(keyValueList: (String, String)*): String = {
      val message = new StringBuilder("")
      for (keyValuePair <- keyValueList) {
        message.append(s"${keyValuePair._1}=${keyValuePair._2} ")
      }
      message.toString()
    }
  }
}
