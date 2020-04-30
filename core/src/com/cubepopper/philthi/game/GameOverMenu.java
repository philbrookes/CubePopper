package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cubepopper.philthi.game.Level.Level;

public class GameOverMenu extends AbstractMenu {
    Level level;
    public GameOverMenu(CubePopper game, Level level) {
        super(game);
        this.level = level;
        Gdx.app.log("CUBE POPPER", "game over, score: " + level.getCurrentScore());
    }

    @Override
    protected Stage buildMenu() {
        Stage menu = new Stage();
        try {
            String recStr = topScore.readString();
            if (!recStr.equals("")) {
                int record = Integer.parseInt(recStr);
                if (level.getCurrentScore() > record) {
                    topScore.writeString(String.valueOf(level.getCurrentScore()), false);
                }
            }
        }catch(Exception e){
            topScore.writeString(String.valueOf(level.getCurrentScore()), false);
        }

        score = level.getCurrentScore();

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
                Gdx.app.exit();
                return true;
            }
        }));
        return menu;
    }
}
