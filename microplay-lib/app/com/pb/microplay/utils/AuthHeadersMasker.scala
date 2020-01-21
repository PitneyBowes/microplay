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
