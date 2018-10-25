package util

import akka.actor.ActorSystem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuxiliaryExecutor @Inject()(actorSystem: ActorSystem)
  extends WebServerExecutor(actorSystem, "dispatcher.auxiliary") {
}
