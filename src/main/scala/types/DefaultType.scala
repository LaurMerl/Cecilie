package types

import java.net.URL

import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.RemoteWebDriver


object DefaultType {
  type Link = URL
  type DepthType = Int
  type Domain = String
  type DataMining = String
  type XPath = String
  type TLog = String

  type LinkSpec = (Link,DepthType)
  type Page =  RemoteWebDriver

}
