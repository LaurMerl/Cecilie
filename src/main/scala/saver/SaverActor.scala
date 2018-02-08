package saver

import akka.actor.{Actor, ActorLogging, Props}
import org.mongodb.scala._
import saver.SaverActor.SaveJob

class SaverActor extends Actor with ActorLogging   {
  // To directly connect to the default server localhost on port 27017

  // Use a Connection String
  val mongoClient: MongoClient = MongoClient("mongodb://mongo:27017")
  val database: MongoDatabase = mongoClient.getDatabase("cecilie")
  val collection: MongoCollection[Document] = database.getCollection("jobs")


  override def receive: Receive = {

    case SaveJob(jobInfo) =>
      val e = jobInfo.toMap
      collection.insertOne(Document(e)).subscribe(new Observer[Completed] {

        override def onNext(result: Completed): Unit = println("Inserted")

        override def onError(e: Throwable): Unit = println("Failed")

        override def onComplete(): Unit = println("Completed")
      })
      println("save " + jobInfo)
  }
}

object SaverActor {
  case class SaveJob(ifos:Seq[(String,String)])
  def props(): Props = Props(new SaverActor())
}
