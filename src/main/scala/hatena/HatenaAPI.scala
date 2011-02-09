package jp.ddo.ttoshi.hatena

class HatenaAPI (val user: HatenaUser){
  val base: URL = new URL("http://d.hatena.ne.jp")
  val userID = user.name
  val atom = "atom"
  val blog = "blog"
  val draft = base / userID / atom / "draft"
  val list = base / userID / atom / blog
}

class URL(val is: String) {
  def /(child: String) = new URL(is + "/" + child)
}
