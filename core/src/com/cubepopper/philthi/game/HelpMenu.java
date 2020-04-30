package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class HelpMenu extends AbstractMenu {
    public HelpMenu(CubePopper game) {
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
            "Pop clusters of 2 or more cubes to earn points.Get grey hearts to the bottom " +
                "to earn points and more time. pop 5 or more grey hearts for a super heart, " +
                "which gives lots of points and time when it reaches the bottom!",

            0,
            750,
            500
        ));
        return menu;
    }
}
