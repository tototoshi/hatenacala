package com.github.tototoshi.hatena

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class HatenaPropsSpec extends FlatSpec with ShouldMatchers {

  it should "get hatena.username from config file" in {
    HatenaProps.name should equal ("me")
  }

  it should "get hatena.password from config file" in {
    HatenaProps.password should equal ("pass")
  }

}
