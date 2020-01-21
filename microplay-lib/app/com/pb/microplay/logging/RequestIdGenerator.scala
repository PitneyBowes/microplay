package com.pb.microplay.logging

import java.util.UUID


/**
 * Created by IntelliJ IDEA
 * User: Anatoly.Libman
 * Date: 1/17/16
 * Time: 3:08 PM
 */
trait RequestIdGenerator {
  def generate(): String
}

object RequestIdGenerator extends RequestIdGenerator
{
  val idLength = 16

  def generate(): String =
  {
    var generatedString = UUID.randomUUID().getMostSignificantBits.toHexString
    for (i <- generatedString.length until idLength)
    {
      generatedString = generatedString + "0"
    }
    generatedString
  }
}
