
package com.borderfree.microplay.logging

import org.specs2.mutable.Specification

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/17/2017
  * Time: 12:04 PM
  */
class RequestIdGeneratorTest extends Specification
{

  "RequestIdGenerator" should
    {

      "generate id with 16 chars" in
        {
          val result = RequestIdGenerator.generate()
          result.length === 16
        }

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme