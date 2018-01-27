import java.net.{URI, URL}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import gossiper.GossiperActor
import info.folone.scala.poi.StringCell
import info.folone.scala.poi.impure.load
import messages.LinkMessage
import types.DefaultType.{DataMining, Domain, Link, XPath}

import scala.collection.JavaConversions._

object Main {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val gossiper = system.actorOf(
    GossiperActor.props(ConfigFactory.load().getStringList("crawler.urls").toList.map(x => new Link(x)),
      ConfigFactory.load().getInt("crawler.deepness")
    ), "gossiper")

  def main(args: Array[String]): Unit = {

    ConfigFactory.load().getStringList("crawler.urls").toList.map(x => new Link(x)).foreach(s => {
      gossiper ! LinkMessage(s, 1)
    })

    scala.io.StdIn.readLine()
  }
}




