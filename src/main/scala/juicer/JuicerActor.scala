package juicer

import java.io.{BufferedWriter, File, FileWriter}
import java.net.{URI, URL}

import scala.collection.JavaConversions._
import akka.actor.{Actor, ActorLogging, Props}
import configuration.ConfigurationManager
import messages.{LinkMessage, SqueezeLinkMessage}
import org.openqa.selenium.By
import saver.SaverActor
import saver.SaverActor.SaveJob
import types.DefaultType._

import scala.concurrent.duration._
import scala.io.Source

class JuicerActor(mapping:Map[Domain,Seq[(DataMining,XPath)]]) extends Actor with ActorLogging  {
  val gossiperActor = context.actorSelection("akka://default/user/gossiper").resolveOne(60 seconds)
  val saverActor = context.actorOf(SaverActor.props)
  implicit val executionContext = context.dispatcher
    override def receive: Receive = {
      case SqueezeLinkMessage(linkInfo,page) => {

        val info = mapping(linkInfo._1.getHost)
              .map{
                  case (field,xpath)=>(field,page.findElements(By.xpath(xpath))
                                                 .map(z=>z.getAttribute("innerText"))
                                                 .filter(x=>x!= null && x.nonEmpty)
                                                 .map(_.trim)
                                                 .reduceLeftOption((a,b)=>a+" "+b))

              }


         page.findElements(By.tagName("a"))
                       .map(a=> a.getAttribute("href"))
                       .foreach{
            link => {
              gossiperActor.map(x => x ! LinkMessage(new URL(link), linkInfo._2 + 1))
            }
          }

        val s1:Seq[(String,String)]= info.filter(_._2.isDefined).map{
          x=>
            x._2 match {
              case Some(a) => (x._1,a)
              case None => (x._1,"")
            }
        }
        if (s1.exists { case (x, y) => y.nonEmpty })
             saverActor ! SaveJob(s1)

        page.close()
      }
    }
  }


object JuicerActor{
  def props(): Props = Props(new JuicerActor(ConfigurationManager.domainInformation))
}