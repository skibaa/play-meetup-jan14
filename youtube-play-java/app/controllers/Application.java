package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.PlayerConnect;
import models.PlayerInfo;
import play.*;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import scala.util.parsing.json.JSONObject;
import views.html.*;
import views.html.index;
import views.html.player;
import views.html.remote;

import play.libs.WS;
import play.mvc.Result;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static play.libs.F.Function;
import static play.libs.F.Promise;

public class Application extends Controller {
    static ConcurrentMap<Integer, PlayerInfo> players = new ConcurrentHashMap<Integer, PlayerInfo>();

    public static Result index() {
        return ok(index.render());
    }

    public static Result player() {
        int id = (int)(Math.random()*PlayerConnect.MAX_ID + 1);
        return ok(player.render(id));
    }

    private static Form<PlayerConnect> createForm() {
        return Form.form(PlayerConnect.class);
    }

    public static Result remote() {
        return ok(remote.render(createForm()));
    }

    public static Result connectRemote() {
        Form<PlayerConnect> form = createForm().bindFromRequest();

        if(form.hasErrors())
            return badRequest(remote.render(form));

        PlayerConnect playerConnect = form.get();
        return ok(remoteSearch.render(playerConnect.playerId));
    }

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Application.search(),
                controllers.routes.javascript.Application.playVideo()
            )
        );
    }

    public static Promise<Result> search(String query) {
        Promise<WS.Response> responsePromise = WS.url("https://www.googleapis.com/youtube/v3/search")
                .setQueryParameter("part", "snippet")
                .setQueryParameter("q", query)
                .setQueryParameter("key", "AIzaSyDg-_FDKc2FDD2kAzZSE3Idtjf4O2Ynm58")
                .get();
        Promise<Result> resultPromise = responsePromise.map(new Function<WS.Response, Result>() {
            @Override
            public Result apply(WS.Response response) throws Throwable {
                if (response.getStatus() != OK)
                    return status(response.getStatus(), response.getBody());

                JsonNode searchResponse = response.asJson();
                return ok(transformJson(searchResponse));
            }
        });
        return resultPromise;
    }

    private static JsonNode transformJson(JsonNode searchResponse) {
        return searchResponse;
    }

    public static Result playVideo(Integer playerId, String videoId, String thumbnailUrl) {
        ObjectNode json = Json.newObject();
        PlayerInfo playerInfo = players.get(playerId);
        if (playerInfo == null) {
            json.put("error", "Player does not exist or was closed.");
            return ok(json);
        }
        playerInfo.outSocket.write(Json.newObject().put("videoId", videoId));
        json.put("error", false)
            .put("status", Json.newObject()
                .put("status", "playing")
                .put("thumbnail",thumbnailUrl)
            );
        return ok(json);
    }

    public static WebSocket<JsonNode> playerWebSocket(final Integer playerId) {
        return new WebSocket<JsonNode>() {
            @Override
            public void onReady(In<JsonNode> jsonNodeIn, Out<JsonNode> jsonNodeOut) {
                final PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.playerId = playerId;
                playerInfo.outSocket = jsonNodeOut;

                if (players.putIfAbsent(playerId, playerInfo) != null) {
                    //there is another player with the same ID
                    ObjectNode jsonError = Json.newObject();
                    jsonError.put("error", "Player ID is not available");
                    jsonNodeOut.write(jsonError);
                    return;
                }

                jsonNodeIn.onClose(new F.Callback0() {
                    @Override
                    public void invoke() throws Throwable {
                        players.remove(playerId, playerInfo);
                    }
                });

                jsonNodeIn.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println("Player says:"+jsonNode.toString());
                    }
                });
            }
        };
    }
}
