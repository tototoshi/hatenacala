package com.github.tototoshi.hatena

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class HatenaPropsSpec extends FlatSpec with ShouldMatchers {

  trait TestPropsFile extends HatenaPropsFile {
    lazy val propsFileName = "src/test/resources/test.properties"
  }
  trait TestPropsFileNotExists extends HatenaPropsFile {
    lazy val propsFileName = "src/test/resources/notfound.properties"
  }
  object TestProps extends HatenaProps with TestPropsFile

  it should "get hatena.username from property file" in {
    TestProps.name should equal ("me")
  }

  it should "get hatena.password from property file" in {
    TestProps.password should equal ("pass")
  }

}
