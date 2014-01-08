package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import views.html.index;
import views.html.player;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result player() {
        int id = (int)(Math.random()*10000);
        return ok(player.render(id));
    }

}
