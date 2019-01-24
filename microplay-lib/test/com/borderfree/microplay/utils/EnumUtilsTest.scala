package com.borderfree.microplay.utils

import org.specs2.mutable.Specification

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 1/24/2019
  * Time: 11:35 AM
  */
class EnumUtilsTest extends Specification
{

  object Weekday extends Enumeration {
    val Monday = Value("MONDAY")
    val Other = Value("OTHER")
  }
  "findEnum " should {
    "find some exists num value if string matched" in {
      EnumUtils.findEnum("MONDAY", Weekday) === Some(Weekday.Monday)
    }
    "return None if string does not match any value" in {
      EnumUtils.findEnum("Monday", Weekday) === None
    }
  }
}
