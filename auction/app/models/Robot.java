package models;

import play.*;
import play.mvc.*;
import play.libs.*;

import scala.concurrent.duration.*;
import akka.actor.*;

import com.fasterxml.jackson.databind.JsonNode;

import static java.util.concurrent.TimeUnit.*;

public class Robot {

    public Robot(ActorRef auctionRoom) {

        // Create a Fake socket out for the robot that log events to the console.
        WebSocket.Out<JsonNode> robotChannel = new WebSocket.Out<JsonNode>() {

            public void write(JsonNode frame) {
                Logger.of("robot").info(Json.stringify(frame));
            }

            public void close() {}

        };

        // Join the room
        auctionRoom.tell(new AuctionRoom.Join("ロッボト", robotChannel), null);

        // Make the robot talk every 30 seconds
        Akka.system().scheduler().schedule(
            Duration.create(30, SECONDS),
            Duration.create(30, SECONDS),
            auctionRoom,
            new AuctionRoom.Bid("ロッボト", "お客様、いらっしゃいませ、ロッボトです。今　ご入札できます。"),
            Akka.system().dispatcher(),
            /** sender **/ null
        );

    }

}