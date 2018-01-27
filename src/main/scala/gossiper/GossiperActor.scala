package gossiper

import akka.actor.{Actor, ActorLogging, Props}
import com.typesafe.config.ConfigFactory
import messages.{HauntingLinkMessage, LinkMessage}
import spider.SpiderActor
import types.DefaultType.{DepthType, Link}

import scala.concurrent.ExecutionContext.Implicits.global


class GossiperActor(domains:List[Link],maxDepth:DepthType) extends Actor with ActorLogging {
  val spiders = context.actorOf(SpiderActor.props())
  println(context.self.path)
  //  context.actorSelection("/user/myActorName/").resolveOne()
  override def receive: Receive = {
    case LinkMessage(link,depth) if messageValidator(link) && maxDepth < depth  => GoForIt(link,depth)
    case LinkMessage(link,depth) if messageValidator(link) && maxDepth >= depth => println(s"Max depth reach")
    case LinkMessage(link,_)     if !messageValidator(link)                     => println(s"Url $link not in the domain")
  }

  //TODO: Give a body to this function, he has to return true if the current link is
  // in the domain. Note that is possible that either the domains list or the link contain protocol and  www
  //TODO: Check if the link its already visited
  //
  def messageValidator(link:Link):Boolean = {
    domains.map(_.getHost).contains(link.getHost)
  }

  def GoForIt(link: Link,depth:DepthType): Unit ={
    println(s"crawlero $link")
    spiders ! HauntingLinkMessage((link,depth))
  }
}


object GossiperActor {
  def props(domains:List[Link],maxDepth:DepthType): Props = Props(new GossiperActor(domains,maxDepth))
}