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
