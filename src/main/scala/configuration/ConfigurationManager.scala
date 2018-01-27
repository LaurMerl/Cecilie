package configuration

import java.net.{URI, URL}

import info.folone.scala.poi.StringCell
import info.folone.scala.poi.impure.load
import types.DefaultType.{DataMining, Domain, Link, XPath}

object ConfigurationManager {
  val path = "./config.xls"

  private def getConfigTable(path:String):(Map[Domain,Seq[(DataMining,XPath)]],Set[Link]) = {
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

  private lazy val info = getConfigTable(path)

  lazy val domainInformation:Map[Domain,Seq[(DataMining,XPath)]] =info._1
  lazy val links:Set[Link] =info._2

}
