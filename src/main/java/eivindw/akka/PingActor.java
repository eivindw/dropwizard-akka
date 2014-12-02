package eivindw.akka;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class PingActor extends AbstractActor {

   private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

   @Override
   public void aroundReceive(PartialFunction<Object, BoxedUnit> receive, Object msg) {
      log.info("Before receive? {}", msg);
      super.aroundReceive(receive, msg);
      log.info("After receive? {}", msg);
   }

   public PingActor() {
      receive(
         ReceiveBuilder
            .match(String.class, msg -> {
               log.info("Replying to message: {}", msg);
               sender().tell("Hi " + msg, self());
            })
            .build()
      );
   }
}
