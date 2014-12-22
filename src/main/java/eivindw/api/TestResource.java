package eivindw.api;

import akka.actor.ActorRef;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.google.common.collect.ImmutableMap;
import eivindw.akka.Message;
import eivindw.service.YoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContextExecutorService;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.Map;
import java.util.concurrent.Executors;

@Path("test")
@Consumes("application/json")
@Produces("application/json")
public class TestResource {

   private final Logger log = LoggerFactory.getLogger(TestResource.class);

   private final ActorRef pinger;
   private final ExecutionContextExecutorService execCtx;

   public TestResource(ActorRef pinger) {
      this.pinger = pinger;
      execCtx = ExecutionContexts.fromExecutorService(Executors.newFixedThreadPool(2));
   }

   @GET
   @Path("{user}")
   public Map test(@PathParam("user") String user) throws Exception {
      log.info("Got request from: {}", user);

      final Future<Object> pong = Patterns.ask(pinger, new Message<>(user, "yoyo from " + user), 100);
      final Object result = Await.result(pong, Duration.create("1 second"));

      log.info("Gotz yo: {}", result);

      final java.util.concurrent.Future<String> javaFuture =
         Executors.newFixedThreadPool(2).submit(() -> new YoService().sayYo(user));

      log.info("YoService: " + javaFuture.get());

      return ImmutableMap.of(
         "name", "Bob",
         "age", 42
      );
   }

   @GET
   @Path("other")
   public Map test() throws Exception {
      log.info("Got other request");

      return ImmutableMap.of(
         "name", "Other",
         "age", 27
      );
   }

   @GET
   @Path("async/{user}")
   public void async(@Suspended AsyncResponse response, @PathParam("user") String user) {
      log.info("Async request start");

      final Future<Object> pong =
         Patterns.ask(pinger, new Message<>(user, "yoyo from " + user), 100);

      pong.onSuccess(
         new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
               log.info("Async returning {}", result);
               response.resume(ImmutableMap.of(
                  "result", result
               ));
            }
         },
         execCtx
      );
   }
}
