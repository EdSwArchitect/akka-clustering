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
public class Subscriber extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private String topicName;

    /**
     *
     */
    public Subscriber() {
        this("content");
    }

    public Subscriber(String topicName) {
        this.topicName = topicName;
        ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();

        // subscribe to the topic named "content"
        mediator.tell(new DistributedPubSubMediator.Subscribe(topicName, getSelf()), getSelf());
    }

    /**
     *
     * @param msg
     * @throws Throwable
     */
    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            log.info("Got: {}", msg);
        } // if (msg instanceof String) {
        else if (msg instanceof DistributedPubSubMediator.SubscribeAck) {
            log.info("subscribing");
        } // else if (msg instanceof DistributedPubSubMediator.SubscribeAck) {
        else {
            unhandled(msg);
        }
    }
}
