package com.cubepopper.philthi.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.cubepopper.philthi.game.Level.Goal;
import com.cubepopper.philthi.game.Level.Level;

import java.util.HashMap;
import java.util.Map;

public class CubePopper extends ApplicationAdapter {
	SpriteBatch batch;
	CubeGrid grid;
	BitmapFont font;
	Level level;
	String state = "new_game";
	String last_state = "";
	Map<String, Texture> textures;
	ProgressBar countdown;

	@Override
	public void create () {
		textures = new HashMap<>();
		textures.put("menu_background", new Texture("menu_background.png"));
		textures.put("button", new Texture("b_3.png"));
		textures.put("pause_button", new Texture("b_8.png"));
		textures.put("score_background", new Texture("b_5.png"));
		textures.put("logo", new Texture("cube-popper-logo.png"));
		textures.put("pb_back", new Texture("progress_bar_background.png"));
		textures.put("pb_knob", new Texture("progress_bar_knob.png"));
		font = new BitmapFont();
		font.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
		font.getData().setScale(4.0f);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		level = new Level();
		level.addScore(50);
		countdown = new ProgressBar(0, level.getTimeLimit(), 0.1f, false, new ProgressBar.ProgressBarStyle());
		countdown.getStyle().background = new TextureRegionDrawable(textures.get("pb_back"));
		countdown.setWidth(Gdx.graphics.getWidth()-100);
		countdown.getStyle().knob = new TextureRegionDrawable(textures.get("pb_knob"));
		countdown.setStepSize(0.1f);

		grid = new CubeGrid(level, batch);
		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(state.equals("playing")) {
					if(screenX >= 25 && screenX <= 175) {
						if(screenY >= 25 && screenY <= 175){
							last_state = state;
							state = "paused";
							return true;
						}
					}
					grid.touch(screenX, Gdx.graphics.getBackBufferHeight() - screenY);
				} else if(state.equals("paused")){
					if(screenX >= 125 && screenX <= 925) {
						if(screenY >= 800 && screenY <= 1000) {
							//continue
							last_state = state;
							state = "playing";
						} else if (screenY >= 1000 && screenY <= 1200) {
							// restart
							grid.restart();
							level.restart();
							last_state = state;
							state = "playing";
						} else if (screenY >= 1200 && screenY <= 1400) {
							last_state = state;
							state = "help";
						} else if (screenY >= 1400 && screenY <= 1600) {
                            last_state = state;
                            state = "credits";
                        }
					}
				} else if (state.equals("credits")) {
					if (screenY >= 1200 && screenY <= 1400) {
						//back button
						state = last_state;
						last_state = "credits";
					}
				} else if (state.equals("help")) {
					if (screenY >= 1200 && screenY <= 1400) {
						//back button
						state = last_state;
						last_state = "help";
					}
				} else if (state.equals("game_over")) {
					if (screenY >= 1000 && screenY <= 1200) {
						// restart
						grid.restart();
						level.restart();
						last_state = state;
						state = "playing";
					} else if (screenY >= 1200 && screenY <= 1400) {
                        // credits
                        last_state = state;
                        state = "help";
                    } else if (screenY >= 1400 && screenY <= 1600) {
						// credits
						last_state = state;
						state = "credits";
					}
				} else if (state.equals("new_game")) {
					if (screenY >= 1000 && screenY <= 1200) {
						last_state = state;
						state = "playing";
					} else if (screenY >= 1200 && screenY <= 1400) {
                        // credits
                        last_state = state;
                        state = "help";
                    } else if (screenY >= 1400 && screenY <= 1600) {
						// credits
						last_state = state;
						state = "credits";
					}
				}
				return true;
			}
		});
	}

	@Override
	public void render () {
		switch(state){
			case "new_game":
				Gdx.gl.glClearColor(0.8f, 0.4f, 0.0f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				renderNewGame();
				break;
			case "game_over":
				Gdx.gl.glClearColor(0.8f, 0.4f, 0.0f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				renderGameOver();
				break;
			case "paused":
				Gdx.gl.glClearColor(0.8f, 0.4f, 0.0f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				renderPause();
				break;
			case "playing":
				Gdx.gl.glClearColor(0.8f, 0.8f, 0.85f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				renderPlaying();
				break;
			case "credits":
				Gdx.gl.glClearColor(0.8f, 0.4f, 0.0f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				renderCredits();
				break;
			case "help":
				Gdx.gl.glClearColor(0.8f, 0.4f, 0.0f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				renderHelp();
				break;
		}
		return;
	}

	public void renderCredits() {
		batch.begin();
		batch.setTransformMatrix(new Matrix4().trn(0,0,0));
		batch.draw(
				this.textures.get("menu_background"),
				Gdx.graphics.getWidth()/2 - 500,
				25,
				1000,
				2000
		);

		batch.draw(
				this.textures.get("logo"),
				Gdx.graphics.getWidth()/2 -250,
				1350,
				500,
				300
		);

		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 800, 800, 200);
		font.setColor(new Color(1.0f, 0.5f, 0.0f, 1.0f));

		font.getData().setScale(2.0f);
		drawCenteredText(batch, "https://www.creativegameassets.com/", 1125, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "https://libgdx.badlogicgames.com/", 1075, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		font.getData().setScale(4.0f);
		drawCenteredText(batch, "Back", 925, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		batch.end();
		return;
	}

	public void renderHelp() {
		batch.begin();
		batch.setTransformMatrix(new Matrix4().trn(0,0,0));
		batch.draw(
				this.textures.get("menu_background"),
				Gdx.graphics.getWidth()/2 - 500,
				25,
				1000,
				2000
		);

		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 800, 800, 200);
		font.setColor(new Color(1.0f, 0.5f, 0.0f, 1.0f));

		font.getData().setScale(4.0f);
		drawCenteredText(batch, "You receive points for popping", 1700, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "cubes. You receive more points", 1600, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "for popping more cubes at once.", 1500, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "You start with 60 seconds to pop", 1400, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "as many cubes as you can.", 1300, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Earn more time by moving heart ", 1200, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "power ups to the bottom.", 1100, new Color(1.0f, 0.5f, 0.0f, 1.0f));


		font.getData().setScale(4.0f);
		drawCenteredText(batch, "Back", 925, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		batch.end();
		return;
	}

	public void renderPlaying() {
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
			last_state = state;
			state = "game_over";
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


	public void renderNewGame() {
		batch.begin();
		batch.setTransformMatrix(new Matrix4().trn(0,0,0));
		batch.draw(
				this.textures.get("menu_background"),
				Gdx.graphics.getWidth()/2 - 500,
				25,
				1000,
				2000
		);


		batch.draw(
			this.textures.get("logo"),
			Gdx.graphics.getWidth()/2 -250,
			1350,
			500,
			300
		);


		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 1000, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 800, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 600, 800, 200);

		drawCenteredText(batch, "Start", 1125, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Help", 925, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Credits", 725, new Color(1.0f, 0.5f, 0.0f, 1.0f));

		batch.end();
	}

	public void renderGameOver() {
		batch.begin();
		batch.setTransformMatrix(new Matrix4().trn(0,0,0));
		batch.draw(
				this.textures.get("menu_background"),
				Gdx.graphics.getWidth()/2 - 500,
				25,
				1000,
				2000
		);

		batch.draw(
				this.textures.get("logo"),
				Gdx.graphics.getWidth()/2 -250,
				1350,
				500,
				300
		);

		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 1000, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 800, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 600, 800, 200);

		drawCenteredText(batch, "Restart", 1125, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Help", 925, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Credits", 725, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "score: " + level.getCurrentScore(), 500, new Color(0,0,0,1));
		batch.end();
	}

	public void renderPause() {
		batch.begin();
		batch.setTransformMatrix(new Matrix4().trn(0,0,0));
		batch.draw(
				this.textures.get("menu_background"),
				Gdx.graphics.getWidth()/2 - 500,
				25,
				1000,
				2000
		);

		batch.draw(
				this.textures.get("logo"),
				Gdx.graphics.getWidth()/2 -250,
				1350,
				500,
				300
		);

		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 1200, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 1000, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 800, 800, 200);
		batch.draw(this.textures.get("button"), Gdx.graphics.getWidth()/2 - 400, 600, 800, 200);

		drawCenteredText(batch, "Continue", 1325, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Restart", 1125, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Help", 925, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		drawCenteredText(batch, "Credits", 725, new Color(1.0f, 0.5f, 0.0f, 1.0f));
		batch.end();
	}

	public void drawCenteredText(Batch batch, String text, float y, Color color) {
		font.setColor(color);
		GlyphLayout gl = new GlyphLayout();
		gl.setText(font, text);
		font.draw(batch, gl, (Gdx.graphics.getWidth() / 2) - (gl.width / 2), y);
	}

	@Override
	public void dispose () {
		grid.dispose();
		batch.dispose();
		font.dispose();
	}
}
