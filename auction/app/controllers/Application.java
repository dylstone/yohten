package controllers;

import play.mvc.*;

import com.fasterxml.jackson.databind.JsonNode; 
import views.html.*;

import models.*;

public class Application extends Controller {

    /**
     * Display the home page.
     */
    public static Result index() {
        return ok(index.render());
    }

    /**
     * Display the auction room.
     */
    public static Result auctionRoom(String username) {
        if(username == null || username.trim().equals("")) {
            flash("error", "有効なユーザー名を選択してください。");
            return redirect(routes.Application.index());
        }
        return ok(auctionRoom.render(username));
    }

    public static Result auctionRoomJs(String username) {
        return ok(views.js.auctionRoom.render(username));
    }

    /**
     * Handle the auction websocket.
     */
    public static WebSocket<JsonNode> auction(final String username) {
        return new WebSocket<JsonNode>() {

            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){

                // Join the auction room.
                try { 
                    AuctionRoom.join(username, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

}