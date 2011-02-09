package jp.ddo.ttoshi.hatena

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
        case "save" => Draft.save(args(2))
        case "add"  => Draft.add(args(2))
        case "rm"  => Draft.rm(args(2))
      }
    }
  }

  val usage ="""|usage:
                |  hatena draft list
                |  hatena draft add  filename
                |  hatena draft save id""".stripMargin

  val user: HatenaUser = new HatenaUser(HatenaProps.name, HatenaProps.password)
}
