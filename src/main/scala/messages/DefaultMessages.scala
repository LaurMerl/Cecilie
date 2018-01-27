package messages

import types.DefaultType._

trait CrawlerMessage{}

case class LinkMessage(url:Link,depth:DepthType) extends CrawlerMessage

case class HauntingLinkMessage(link:LinkSpec)

case class SqueezeLinkMessage(link: LinkSpec,page:Page)