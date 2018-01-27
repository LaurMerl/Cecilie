package types

import java.net.URL

import org.openqa.selenium.{WebDriver}


object DefaultType {
  type Link = URL
  type DepthType = Int
  type Domain = String
  type DataMining = String
  type XPath = String

  type LinkSpec = (Link,DepthType)
  type Page =  WebDriver

}
