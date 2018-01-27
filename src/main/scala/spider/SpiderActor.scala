package spider


import java.io.File
import java.util.Properties

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import juicer.JuicerActor
import messages.{HauntingLinkMessage, SqueezeLinkMessage}

import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities


class SpiderActor  extends Actor with ActorLogging  {
  private val juicer: ActorRef = context.actorOf(JuicerActor.props())




  override def receive: Receive = {
    case HauntingLinkMessage(linkInfo @ (link,_)) => {
      val sCaps = new DesiredCapabilities();
      sCaps.setJavascriptEnabled(true);

      import org.openqa.selenium.phantomjs.PhantomJSDriverService
      sCaps.setCapability("takesScreenshot", false)
      sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", "My User Agent - Chrome")
      sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, Array[String]("--web-security=false", "--ssl-protocol=any", "--ignore-ssl-errors=true", "--webdriver-loglevel=FATAL"))
      val driver =  new  PhantomJSDriver(sCaps)
      driver.get(link.toString)
      juicer ! SqueezeLinkMessage(linkInfo,driver)
    }
  }

}

object SpiderActor {
  def props(): Props = Props(new SpiderActor())
}