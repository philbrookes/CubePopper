package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class NewGameMenu extends AbstractMenu {
    public NewGameMenu(CubePopper game) {
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
        menu.addActor(buildButton("Exit Game", -600, new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.exitScreen();
                return true;
            }
        }));

        return menu;
    }
}
