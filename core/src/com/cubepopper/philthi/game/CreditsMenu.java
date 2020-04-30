package com.cubepopper.philthi.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CreditsMenu extends AbstractMenu {
    public CreditsMenu(CubePopper game) {
        super(game);
    }

    @Override
    protected Stage buildMenu() {
        Stage menu = new Stage();
        menu.addActor(buildButton("close", -600, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.exitScreen();
                return true;
            }
        }));
        menu.addActor(buildTextArea(
                "https://www.creativegameassets.com/ \n" +
                        "https://libgdx.badlogicgames.com/",
                0,
                850,
                500
        ));
        return menu;
    }
}
