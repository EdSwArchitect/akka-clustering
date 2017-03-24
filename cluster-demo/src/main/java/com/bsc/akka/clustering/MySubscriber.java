package com.bsc.akka.clustering;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by EdwinBrown on 3/23/2017.
 */
public class MySubscriber {
    public static void main(String[] args) {
        try {
            Config config = ConfigFactory.parseString(
                    "akka.remote.netty.tcp.port=" + args[0]).withFallback(
                    ConfigFactory.parseString("akka.cluster.roles = [ingest]"))
                    .withFallback(ConfigFactory.load());

            // Create an Akka system
            ActorSystem system = ActorSystem.create("ClusterSystem", config);

            // Create an actor that handles cluster domain events
            ActorRef sub = system.actorOf(Props.create(Subscriber.class),
                    "Subscriber");

        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }
}
