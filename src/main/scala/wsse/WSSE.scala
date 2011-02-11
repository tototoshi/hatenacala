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

package com.tototoshi.wsse

import java.util.Date
import java.text.SimpleDateFormat
import java.security._

import com.tototoshi.base64._

class WSSEUser(val name: String, val password: String)

class WSSE(val userName: String, val password: String) {
  def this(user: WSSEUser) = this(user.name, user.password)
  lazy val created: String = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).format(new Date)
  lazy val nonce: String = {
    var bytes = new Array[Byte](16)
    SecureRandom.getInstance("SHA1PRNG").nextBytes(bytes)
    bytes.toString
  }
  def digest: Array[Byte] = {
    import java.security.MessageDigest
    val algorithm = "SHA-1"
    val from = nonce + created + password
    MessageDigest.getInstance(algorithm).digest(from.getBytes)
  }
  def header: String = {
    def quote(str: String): String = "\"" + str + "\""
    "UsernameToken " +
    List(("Username", userName),
         ("PasswordDigest", Base64.encode(digest)),
         ("Nonce", Base64.encode(nonce.getBytes)),
         ("Created", created))
    .map{case (k, v) => k + "=" + quote(v)}.mkString(", ")
  }
}
