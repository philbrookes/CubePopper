package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.cubepopper.philthi.game.CubePopper;

import java.util.logging.FileHandler;

public abstract class AbstractMenu extends ScreenAdapter {
    protected CubePopper game;
    Stage menu;
    BitmapFont font;
    Texture button;
    Texture background;
    Texture logo;
    FileHandle topScore;
    SpriteBatch batch;
    protected int score;

    public AbstractMenu(CubePopper game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        font.getData().setScale(4.0f);
        button = new Texture("b_3.png");
        background = new Texture("menu_background.png");
        logo = new Texture("cube-popper-logo.png");
        topScore = Gdx.files.local("topscore.txt");
    }

    abstract protected Stage buildMenu();

    @Override
    public void show(){
        menu = this.buildMenu();
        Gdx.input.setInputProcessor(menu);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 0.7f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(logo, Gdx.graphics.getWidth() / 2 - (logo.getWidth() / 2), Gdx.graphics.getHeight() - (logo.getHeight() + 100));
        drawCenteredText(batch, "Top score: " + topScore.readString(), Gdx.graphics.getHeight() - (logo.getHeight() + 300) );
        if(score > 0) {
            drawCenteredText(batch, "Score: " + score, Gdx.graphics.getHeight() - (logo.getHeight() + 350) );
        }
        batch.end();
        menu.draw();
    }

    private void drawCenteredText(Batch batch, String text, float y) {
        GlyphLayout gl = new GlyphLayout();
        gl.setText(font, text);
        font.draw(batch, gl, (Gdx.graphics.getWidth() / 2) - (gl.width / 2), y);

    }

    protected TextArea buildTextArea(String text, int yOffset, int width, int height) {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = font.getColor();
        TextArea area = new TextArea(text, style);
        area.setWidth(width);
        area.setHeight(height);
        area.setPosition(
                Gdx.graphics.getWidth()/2.0f - area.getWidth()/2.0f,
                Gdx.graphics.getHeight()/2.0f - area.getHeight()/2.0f + yOffset
        );
        area.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return true;
            }
        });
        return area;
    }

    protected TextButton buildButton(String text, int yOffset, EventListener el) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(button);
        style.font = font;
        TextButton menuButton = new TextButton(text, style);
        menuButton.addListener(el);
        menuButton.setHeight(200);
        menuButton.setWidth(600);
        menuButton.setPosition(
                Gdx.graphics.getWidth()/2.0f - menuButton.getWidth()/2.0f,
                Gdx.graphics.getHeight()/2.0f - menuButton.getHeight()/2.0f + yOffset
        );
        return menuButton;
    }
}
