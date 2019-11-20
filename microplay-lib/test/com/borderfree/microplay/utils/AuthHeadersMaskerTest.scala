package com.borderfree.microplay.utils

import org.specs2.mutable.Specification
class AuthHeadersMaskerTest extends Specification {

  "AuthHeadersMasker" should {

    "mask Headers with Authorization" in {
      val result = AuthHeadersMasker.maskHeaders(Map("some_header"->"someVal","Authorization"-> "hideMe"))
      result === Map("some_header"->"someVal","Authorization"-> "******")
    }
    "mask Headers with Basic Authorization" in {
      val result = AuthHeadersMasker.maskHeaders(Map("some_header"->"someVal","Authorization"-> "Basic dXNlcl9uYW1lX3RvX2JlX3Nob3duOnBhc3N3b3JkX3RvX2hpZGU="))
      result === Map("some_header"->"someVal","Authorization"-> "Basic user_name_to_be_shown:****************")
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme