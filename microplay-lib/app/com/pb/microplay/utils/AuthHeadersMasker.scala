package com.pb.microplay.utils

import scala.util.Try

object AuthHeadersMasker {

  private val AUTHORIZATION_PATTERN = "(?i)(Authorization)".r
  private val BA_PATTERN = "([Bb][Aa][Ss][Ii][Cc]\\s)([A-Za-z0-9+/]+={0,2})".r
  private val BA_DECODED_VAL_PATTERN = "(.+):(.+)".r

  def maskHeaders(headers: Map[String, String]): Map[String,String] = {
    headers.map {
      case (AUTHORIZATION_PATTERN(authHeader),BA_PATTERN(baPrefix,baAuthVal)) => (authHeader, baPrefix+Try(new String(java.util.Base64.getDecoder.decode(baAuthVal))).toOption.map(maskBaDecodedValue).getOrElse(maskString(baAuthVal)))
      case (AUTHORIZATION_PATTERN(authHeader),otherAuthVal) => (authHeader, maskString(otherAuthVal))
      case x => x
    }
  }

  private def maskString(str: String) = str.replaceAll(".", "*")

  private def maskBaDecodedValue(value:String) :String = {
    value match {
      case BA_DECODED_VAL_PATTERN(user,pass) => user+":"+ maskString(pass)
      case otherVal => maskString(otherVal)
    }
  }

}
