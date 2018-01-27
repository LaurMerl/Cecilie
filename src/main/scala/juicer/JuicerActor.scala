package juicer

import java.net.{URI, URL}

import scala.collection.JavaConversions._
import akka.actor.{Actor, ActorLogging, Props}
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
  def getConfigTable(path:String):(Map[Domain,Seq[(DataMining,XPath)]],Set[Link]) = {
    val wb = load(path)
    val e = wb.sheets.map(x=>{
      val rowOrdered = x.rows
        .toSeq
        .sortBy(_.index)

      val headers: Map[Int, String] = rowOrdered
        .head
        .cells
        .map{case StringCell(z,d)=>z->d}.toMap

      val elements = rowOrdered.tail.map{_.cells.map{
        case StringCell(headId,d) if headId == 0 => (headers(headId),new URI(d).getHost)
        case StringCell(headId,d) => (headers(headId),d)
      }}

      elements.flatMap(x=> {
        val domain = x.find(z => z._1 == headers(0))
          .get._2
        x.filter(_._1 != headers(0))
          .map((domain,_))
      }).groupBy(_._1)
        .mapValues(_.map(_._2))
    }).head



    val req = wb.sheets.flatMap( x => {
      val rowOrdered = x.rows.toSeq.sortBy(_.index)
      rowOrdered.tail
        .map(p=>p.cells.find(_.index == 0 ).get)
        .map{case StringCell(_,s)=>new URL(s)}
    })

    (e,req)
  }

  def props(): Props = Props(new JuicerActor(getConfigTable("./config.xls")._1))
}