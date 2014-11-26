package eivindw;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import eivindw.akka.PingActor;
import eivindw.service.YoService;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.Executors;

public class ExampleApp extends Application<Configuration> {

    private final Logger log = LoggerFactory.getLogger(ExampleApp.class);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[]{"server", "src/main/config/local.yml"};
        }
        new ExampleApp().run(args);
    }

    @Override
    public void run(Configuration conf, Environment env) throws Exception {
        MDC.put("sourceThread", Thread.currentThread().getName());
        log.info("Starting application");

        final ActorSystem actorSystem = ActorSystem.create("ExampleSystem");

        final ActorRef pinger = actorSystem.actorOf(Props.create(PingActor.class), "Pinger");

        final Future<Object> pong = Patterns.ask(pinger, "ping", 100);

        final java.util.concurrent.Future<String> javaFuture =
           Executors.newFixedThreadPool(2).submit(() -> new YoService().sayYo("Boy"));

        Thread.sleep(50);

        final Object result = Await.result(pong, Duration.create("1 second"));

        log.info("Gots result: {}", result);

        log.info("Yoz: {}", javaFuture.get());
    }
}
