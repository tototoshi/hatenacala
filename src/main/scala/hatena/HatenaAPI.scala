package jp.ddo.ttoshi.hatena

class HatenaAPI (val user: HatenaUser){
  private val userID = user.name
  private val atom = "atom"
  private val blog = "blog"
  private val draft = "draft"
  private val base = "http://d.hatena.ne.jp"
  val list = List(base, userID, atom, blog).mkString("/")
  val drafts = List(base, userID, atom, draft).mkString("/")
  def draft(entryID: String): String = List(base, userID, atom, draft, entryID).mkString("/")
}
