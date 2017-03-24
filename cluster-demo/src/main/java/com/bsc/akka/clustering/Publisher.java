package com.bsc.akka.clustering;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by EdwinBrown on 3/21/2017.
 */
public class Publisher extends UntypedActor {
    private String topicName;

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    // activate the extension
    ActorRef mediator =  DistributedPubSub.get(getContext().system()).mediator();

    /**
     * Default constructor. Queue name content
     */
    public Publisher() {
        topicName = "content";
    }

    /**
     *
     * @param topicName
     */
    public Publisher(String topicName) {
        this.topicName = topicName;
    }

    /**
     *
     * @param msg
     */
    @Override
    public void onReceive(Object msg) {
        if (msg instanceof String) {
            String in = (String) msg;
            String out = in.toUpperCase();
            mediator.tell(new DistributedPubSubMediator.Publish(topicName, out), getSelf());
        } // if (msg instanceof String) {
        else {
            unhandled(msg);
        }
    }
}