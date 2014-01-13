package controllers;

import models.PlayerConnect;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import views.html.index;
import views.html.player;
import views.html.remote;

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
}
