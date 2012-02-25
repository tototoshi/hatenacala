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

import java.io.{File, FileInputStream}
import java.util.Properties

object Hatena {
  def main(args: Array[String]) {
    args.toList match {
      case List() => println(usage)
      case "draft" :: xs => xs match {
        case "list"   :: Nil      => Draft.list.foreach(println)
        case "add"    :: filename :: Nil => Draft.add(filename)
        case "rm"     :: id       :: Nil => Draft.rm(id)
        case "get"    :: id       :: Nil => Draft.get(id)
        case "update" :: id       :: filename :: Nil => Draft.update(id, filename)
        case _ => println(usage)
      }
      case _ => println(usage)
    }
  }

  val usage ="""|usage:
                |  hateda draft list
                |  hateda draft get 'id'
                |  hateda draft add 'filename'
                |  hateda draft update 'id' 'filename'
                |  hateda draft rm 'id'""".stripMargin

  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
}
