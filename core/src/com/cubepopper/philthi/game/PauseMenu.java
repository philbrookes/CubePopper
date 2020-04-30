package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class PauseMenu extends AbstractMenu {
    public PauseMenu(CubePopper game) {
        super(game);
    }

    @Override
    protected Stage buildMenu() {
        Stage menu = new Stage();
        menu.addActor(buildButton("Resume", 300, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.exitScreen();
                return true;
            }
        }));
        menu.addActor(buildButton("New Game", 0, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.changeScreen(new GameScreen(game));
                return true;
            }
        }));
        menu.addActor(buildButton("Credits", -300, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.changeScreen(new CreditsMenu(game));
                return true;
            }
        }));
        menu.addActor(buildButton("Help", -600, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.changeScreen(new HelpMenu(game));
                return true;
            }
        }));
        menu.addActor(buildButton("Exit Game", -900, new EventListener() {
            @Override
            public boolean handle(Event event) {
                Gdx.app.exit();
                return true;
            }
        }));
        return menu;
    }
}
