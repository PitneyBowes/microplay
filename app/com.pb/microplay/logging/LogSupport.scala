package com.pb.microplay.logging

import play.api.Logger

/**
 * Created by IntelliJ IDEA
 * User: Anatoly.Libman
 * Date: 1/18/16
 * Time: 12:03 PM
 */
trait LogSupport {
  lazy val logger = Logger(this.getClass)
}
