package juicer

import java.net.{URI, URL}


import scala.collection.JavaConversions._
import akka.actor.{Actor, ActorLogging, Props}
import configuration.ConfigurationManager
import messages.{LinkMessage, SqueezeLinkMessage}
import org.openqa.selenium.{By}
import types.DefaultType._

import scala.concurrent.duration._

class JuicerActor(mapping:Map[Domain,Seq[(DataMining,XPath)]]) extends Actor with ActorLogging  {
  val gossiperActor = context.actorSelection("akka://default/user/gossiper").resolveOne(60 seconds)
  implicit val executionContext = context.dispatcher
    override def receive: Receive = {
      case SqueezeLinkMessage(linkInfo,page) => {

        val info = mapping(linkInfo._1.getHost)
              .map{
                  case (field,xpath)=>(field,page.findElements(By.xpath(xpath))
                                                 .map(z=>z.getAttribute("innerText").trim)
                                                 .filter(!_.isEmpty)
                                                 .reduceLeftOption(_+" "+_))

              }

        println(info)

         page.findElements(By.tagName("a"))
                       .map(a=> a.getAttribute("href"))
          .foreach{
            link => {
              gossiperActor.map(x => x ! LinkMessage(new URL(link), linkInfo._2 + 1))
            }
          }


        println(info)
         }
    }
  }


object JuicerActor{
  def props(): Props = Props(new JuicerActor(ConfigurationManager.domainInformation))
}