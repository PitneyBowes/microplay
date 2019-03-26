package com.borderfree.microplay.logging

import com.typesafe.scalalogging.LazyLogging
/**
  We provide logging support to our applications via

  scala-logging - https://github.com/lightbend/scala-logging
  and
  logstash-encoder - https://github.com/logstash/logstash-logback-encoder.

  Thus enabling logging capabilities much richer than those offered by
  the default play logger.

  Usage example :

  import net.logstash.logback.argument.StructuredArguments._

  logger.debug("example msg {} {}" , keyValue("response", responseObject))

  see logstash encode documentation for all the capabilties

  **/

trait LogSupport extends LazyLogging {


}
