package spider


import java.io.File
import java.util.Properties
import java.util.logging.Level

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import configuration.ConfigurationManager
import juicer.JuicerActor
import messages.{HauntingLinkMessage, SqueezeLinkMessage}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.phantomjs.PhantomJSDriverService


class SpiderActor  extends Actor with ActorLogging  {
  private val juicer: ActorRef = context.actorOf(JuicerActor.props())

  val sCaps = new DesiredCapabilities()
  sCaps.setJavascriptEnabled(true)
  sCaps.setCapability("takesScreenshot", false)
  sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", "My User Agent - Chrome")
  sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
            Array[String]("--web-security=false",
                          "--ssl-protocol=any",
                          "--ignore-ssl-errors=true",
                          "--webdriver-loglevel=FATAL",
                          "--proxy-type=socks5"
//                          s"--proxy=localhost:${ConfigurationManager.proxyPort}"
                         ))

  override def receive: Receive = {
    case HauntingLinkMessage(linkInfo @ (link,_)) => {
      val driver:PhantomJSDriver =  new  PhantomJSDriver(sCaps)
      driver.setLogLevel(Level.OFF)
      driver.get(link.toString)
      juicer ! SqueezeLinkMessage(linkInfo,driver)
    }
  }

}

object SpiderActor {
  def props(): Props = Props(new SpiderActor())
}