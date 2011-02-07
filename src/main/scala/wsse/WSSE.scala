package jp.ddo.ttoshi.wsse

import java.util.Date
import java.text.SimpleDateFormat
import java.security._

import jp.ddo.ttoshi.base64._

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
