package util

import akka.actor.ActorSystem
import scala.concurrent.ExecutionContextExecutor

/**
  * An executor base class for Play web servers.
  * It uses the built in actor system to discover its configured dispatcher.
  * @param actorSystem
  */
abstract class WebServerExecutor(actorSystem: ActorSystem, dispatcherConfigName: String) extends ExecutionContextExecutor {

  private val dispatcher = actorSystem.dispatchers.lookup(dispatcherConfigName)

  override def execute(command: Runnable): Unit = {
    dispatcher.execute(command)
  }

  override def reportFailure(cause: Throwable): Unit = {
    dispatcher.reportFailure(cause)
  }
}