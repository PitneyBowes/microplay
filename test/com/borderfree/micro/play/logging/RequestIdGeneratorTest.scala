
package com.borderfree.micro.play.logging

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 12/17/2017
  * Time: 12:04 PM
  */
class RequestIdGeneratorTest extends Specification with Mockito
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