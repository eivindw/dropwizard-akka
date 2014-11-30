package eivindw.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import com.google.common.collect.ImmutableMap;

public class PingUntypedActor extends UntypedActor {

   private final DiagnosticLoggingAdapter log = Logging.getLogger(this);

   private final ActorRef pinger;

   public PingUntypedActor(ActorRef pinger) {
      this.pinger = pinger;
   }

   @Override
   public void onReceive(Object message) throws Exception {
      if (message instanceof Message) {
         final Message msg = (Message) message;

         log.setMDC(ImmutableMap.of("corId", msg.corId()));

         log.info("Got a message: {}", msg.messageData());

         pinger.tell(msg.messageData(), self());

         sender().tell("Howdie " + msg.messageData(), self());

         log.clearMDC();
      } else {
         log.info("Unexpected message: {}", message);
         unhandled(message);
      }
   }
}
