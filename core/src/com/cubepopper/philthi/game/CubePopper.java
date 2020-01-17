package com.cubepopper.philthi.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class CubePopper extends ApplicationAdapter {
	SpriteBatch batch;
	CubeGrid grid;
	BitmapFont font;
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
		font.getData().setScale(4.0f);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		final float scale = 0.33f;
		CubeConfig[] spawnables = new CubeConfig[]{
				new CubeConfig("crystal", scale),
				new CubeConfig("ruby", scale),
				new CubeConfig("sand", scale),
				new CubeConfig("topaz", scale),
				new CubeConfig("dropper", scale, 15, 15, false),
		};
		grid = new CubeGrid(18, 8, batch, spawnables);
		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				grid.touch(screenX, Gdx.graphics.getBackBufferHeight() - screenY, scale);
				return true;
			}
		});
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.85f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		grid.draw();
		batch.end();

		batch.begin();
		font.draw(batch, "Score: " + grid.getScore(), 0, -10);

		batch.end();
	}
	
	@Override
	public void dispose () {
		grid.dispose();
		batch.dispose();
		font.dispose();
	}
}
