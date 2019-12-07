package com.cubepopper.philthi.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CubePopper extends ApplicationAdapter {
	SpriteBatch batch;
	CubeGrid grid;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		grid = new CubeGrid(8, 8, batch);
		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				grid.touch(screenX, Gdx.graphics.getBackBufferHeight() - screenY);
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
	}
	
	@Override
	public void dispose () {
		grid.dispose();
		batch.dispose();
	}
}
