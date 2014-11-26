package eivindw.akka;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class PingActor extends AbstractActor {

   private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

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
