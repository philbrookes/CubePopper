package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameOverMenu extends AbstractMenu {
    public GameOverMenu(CubePopper game) {
        super(game);
    }

    @Override
    protected Stage buildMenu() {
        Stage menu = new Stage();
        menu.addActor(buildButton("New Game", 300, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.changeScreen(new GameScreen(game));
                return true;
            }
        }));
        menu.addActor(buildButton("Help", 0, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.changeScreen(new HelpMenu(game));
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
        return menu;
    }
}
