package jp.ddo.ttoshi.hatena

import java.io.{ File, FileInputStream}


object HatenaProps {
  val propsFileName = "/.hatena.properties"
  val props = System.getProperties
  val homeDir = props.getProperty("user.home")
  props.load(new FileInputStream(new File(homeDir + propsFileName)))
  val name = {
    props.getProperty("hatena.username") match {
      case null => error("Error: name is required.")
      case v => v
    }
  }
  val password = {
    props.getProperty("hatena.password") match {
      case null => error("Error: password is required.")
      case v => v
    }
  }
  val draftDir = {
    props.getProperty("hatena.draftdir") match {
      case null => homeDir + "/HatenaDraft"
      case v => v
    }
  }
}
