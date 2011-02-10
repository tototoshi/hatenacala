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
        case "get"  => Draft.open(args(2))
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
