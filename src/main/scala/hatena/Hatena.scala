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

package com.tototoshi.hatena

import java.io.{File, FileInputStream}
import java.util.Properties

object Hatena {
  def main(args: Array[String]) {
    if (args.isEmpty) {
      println(usage)
      return
    }

    args(0).toLowerCase match {
      case "draft" => args(1).toLowerCase match {
        case "list" => Draft.list.foreach(println)
        case "add"  => Draft.add(args(2))
        case "rm"  => Draft.rm(args(2))
        case "get"  => Draft.get(args(2))
        case "update"  => args.length match {
          case 4 => Draft.update(args(2), args(3))
          case _ => println(usage)
        }
        case _ => println(usage)
      }
      case _ => println(usage)
    }
  }

  val usage ="""|usage:
                |  hatena draft list
                |  hatena draft add 'filename'
                |  hatena draft update 'id' 'filename'
                |  hatena draft rm 'id'""".stripMargin

  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
}
