package debugger

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.{Actor, ActorLogging}
import debugger.Debugger.WriteLog
import types.DefaultType.TLog

class Debugger(fileName:String) extends Actor with ActorLogging  {
  val file1 = new File(fileName)
  val bw1 = new BufferedWriter(new FileWriter(file1, true))

  override def receive: Receive = {
    case WriteLog(message) => {
      bw1.write(s" $message \n")
    }
  }
}


object Debugger{
  case class WriteLog(log:TLog)
}