package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cubepopper.philthi.game.CubeGrid;
import com.cubepopper.philthi.game.CubePopper;
import com.cubepopper.philthi.game.Level.Level;

import java.util.HashMap;

import javax.xml.soap.Text;

public class GameScreen extends ScreenAdapter {
    CubePopper game;
    CubeGrid grid;
    Level level;
    SpriteBatch batch;
    ProgressBar countdown;
    HashMap<String, Texture> textures;
    BitmapFont font;

    public GameScreen(CubePopper game){
        this.game = game;
        level = new Level();
        batch = new SpriteBatch();
        textures = loadTextures();
        grid = new CubeGrid(level, batch);
        countdown = buildCountdown();
        font = new BitmapFont();
        font.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        font.getData().setScale(4.0f);
    }

    private HashMap<String, Texture> loadTextures(){
        HashMap textures = new HashMap<String, Texture>();
        textures.put("pause_button", new Texture("b_8.png"));
        textures.put("pb_back", new Texture("progress_bar_background.png"));
        textures.put("pb_knob", new Texture("progress_bar_knob.png"));
        textures.put("score_background", new Texture("b_5.png"));
        return textures;
    }

    private ProgressBar buildCountdown() {
        countdown = new ProgressBar(0, level.getTimeLimit(), 0.1f, false, new ProgressBar.ProgressBarStyle());
        countdown.getStyle().background = new TextureRegionDrawable(textures.get("pb_back"));
        countdown.setWidth(Gdx.graphics.getWidth()-100);
        countdown.getStyle().knob = new TextureRegionDrawable(textures.get("pb_knob"));
        countdown.setStepSize(0.1f);
        return countdown;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(screenX >= 25 && screenX <= 175) {
                    if(screenY >= 25 && screenY <= 175){
                        // pause button
                        PauseMenu pause = new PauseMenu(game);
                        game.changeScreen(pause);
                        return true;
                    }
                }
                grid.touch(screenX, Gdx.graphics.getBackBufferHeight() - screenY);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setTransformMatrix(new Matrix4().trn(0,0,0));
        batch.draw(textures.get("pause_button"), 25, Gdx.graphics.getHeight() - 175, 150, 150);
        batch.end();

        batch.begin();
        grid.draw();
        batch.end();

        batch.begin();
        batch.setTransformMatrix(new Matrix4().trn(50,450,0));
        countdown.setRange(0, level.getTimeLimit());
        countdown.setValue(level.getTimeExpired());
        countdown.draw(batch, 1.0f);
        countdown.act(Gdx.graphics.getDeltaTime());
        level.usedTime(Gdx.graphics.getDeltaTime());
        if(level.outOfTime()) {
            // game over
        }
        batch.end();

        batch.begin();
        batch.setTransformMatrix(new Matrix4().trn((Gdx.graphics.getWidth()/2) - 200,200,0));
        batch.draw(
                this.textures.get("score_background"),
                0,
                0,
                400,
                150
        );
        font.draw(batch, level.getGoals()[0].toString(), 50, 107);

        batch.end();
    }

    @Override
    public void hide() {

    }
}
