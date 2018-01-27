package juicer

import java.net.{URI, URL}

import scala.collection.JavaConversions._
import akka.actor.{Actor, ActorLogging, Props}
import configuration.ConfigurationManager
import info.folone.scala.poi.StringCell
import info.folone.scala.poi.impure.load
import messages.SqueezeLinkMessage
import org.openqa.selenium.{By, WebElement}
import types.DefaultType._


class JuicerActor(mapping:Map[Domain,Seq[(DataMining,XPath)]]) extends Actor with ActorLogging  {
    override def receive: Receive = {
      case SqueezeLinkMessage(linkInfo,page) => {

        val info = mapping(linkInfo._1.getHost)
              .map{
                  case (field,xpath)=>(field,page.findElements(By.xpath(xpath))

                                                 .map(z=>z.getAttribute("innerText").trim)
                                                 .filter(!_.isEmpty)
                                                 .reduceLeft(_+" "+_))

              }
        val link = page.findElements(By.tagName("a"))
                       .map(a=> a.getAttribute("href"))

        println(info)
         }
    }
  }


object JuicerActor{
  def props(): Props = Props(new JuicerActor(ConfigurationManager.domainInformation))
}