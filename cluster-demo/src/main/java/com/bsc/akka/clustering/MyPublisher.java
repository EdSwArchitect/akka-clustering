package com.bsc.akka.clustering;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

            for (int i = 0; i < 60; i++) {
                pub.tell("Message at time: " + new Date() + " is the time", null);
                TimeUnit.SECONDS.sleep(30);
            }

        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }
}
