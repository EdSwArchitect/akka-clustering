package com.bsc.akka.clustering;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.bsc.akka.clustering.Command.CMD.*;

/**
 * Created by EdwinBrown on 3/23/2017.
 */
public class MyPublisher {
    public static void main(String[] args) {
        try {
            Config config = ConfigFactory.parseString(
                    "akka.remote.netty.tcp.port=" + args[0]).withFallback(
                    ConfigFactory.parseString("akka.cluster.roles = [ingest]"))
                    .withFallback(ConfigFactory.load());

            // Create an Akka system
            ActorSystem system = ActorSystem.create("ClusterSystem", config);

            // Create an actor that handles cluster domain events
            ActorRef pub = system.actorOf(Props.create(Publisher.class),
                    "Publisher");

            LoggingAdapter log = system.log();
            Command.CMD cmd = START;

            for (int i = 0; i < 60; i++) {
                TimeUnit.SECONDS.sleep(30);
                log.info("Sending message at: {}", new Date());

                switch(cmd) {
                    case PAUSE:
                        cmd = STOP;
                        break;
                    case STOP:
                        cmd = METRICS;
                        break;
                    case METRICS:
                        cmd = START;
                        break;
                    case START:
                        cmd = PAUSE;
                        break;
                    default:
                        cmd = START;
                }

                pub.tell(cmd, null);
            }

        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }
}
