package com.cubepopper.philthi.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Stack;

public class CubePopper extends Game {
	public Stack<Screen> screenStack;
	@Override
	public void create () {
		screenStack = new Stack<Screen>();
		setScreen(new NewGameMenu(this));
	}

	public void changeScreen(Screen screen) {
		screenStack.push(getScreen());
		Gdx.app.log("CUBE POPPER", "changing to " + screen.getClass().getName());
		setScreen(screen);
	}

	public void exitScreen() {
		Screen screen = screenStack.peek();
		if(screen == null) {
			Gdx.app.exit();
		}
		Gdx.app.log("CUBE POPPER", "changing to " + screen.getClass().getName());
		setScreen(screenStack.pop());
	}
}
