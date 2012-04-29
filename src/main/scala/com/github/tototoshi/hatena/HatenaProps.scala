/*
Copyright (c) 2011 Toshiyuki Takahashi.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.
 3. The name of the author may not be used to endorse or promote
    products derived from this software without specific prior
    written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS
 OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.github.tototoshi.hatena

import java.io.{ File, FileInputStream}
import java.util.Properties

trait HatenaPropsFile {
  val propsFileName: String
}

trait DefaultHatenaPropsFile extends HatenaPropsFile {
  lazy val propsFileName: String = scala.util.Properties.userHome + "/.hatena.properties"
}

trait PropsUtil {
  def propsOrNone(props: java.util.Properties, propName: String): Option[String] = Option(props.getProperty(propName))
  def systemPropsOrNone(propName: String) = Option(System.getProperty(propName))
}

trait HatenaProps extends PropsUtil { self: HatenaPropsFile =>
  val props = new Properties
  lazy val propsFile = new File(propsFileName)

  if (propsFile.exists()) {
    props.load(new FileInputStream(propsFile))
  }

  lazy val name = prop("hatena.username")
  lazy val password = prop("hatena.password")

  private def prop(propName: String): String = {
    systemPropsOrNone(propName)
    .orElse(propsOrNone(props, propName))
    .getOrElse(sys.error("Error: " + propName + " is required. Check your settings."))
  }
}

object HatenaProps extends HatenaProps with DefaultHatenaPropsFile
