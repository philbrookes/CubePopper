package com.cubepopper.philthi.game.Cubes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.Position;
import com.cubepopper.philthi.game.Size;

public interface CubeInterface {
    public PopCube clone();
    public void Draw(Batch batch);
    public Size scaledSize();
    public void Process(float timeElapsed);
    public String toString();
    public Position getGridPos();
    public void setGridPos(Position newGridPos);
    public Position getPos();
    public void setPos(Position newPos);
    public int getState();
    public Texture getTexture();
    public CubeConfig getConfig();

    public boolean touchHandled(int col, int row);

    public boolean atRest();
}
