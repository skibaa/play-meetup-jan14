package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.PlayerConnect;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import views.html.index;
import views.html.player;
import views.html.remote;

import play.libs.WS;
import play.mvc.Result;

import static play.libs.F.Function;
import static play.libs.F.Promise;

public class Application extends Controller {

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
                controllers.routes.javascript.Application.search()
            )
        );
    }

    public static Promise<Result> search(String query) {
        Promise<WS.Response> responsePromise = WS.url("https://www.googleapis.com/youtube/v3/search1")
                .setQueryParameter("part", "snippet")
                .setQueryParameter("q", query)
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
}
