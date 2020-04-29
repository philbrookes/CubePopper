package com.cubepopper.philthi.game.Cubes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.CubeFactory;
import com.cubepopper.philthi.game.Position;
import com.cubepopper.philthi.game.Size;

import java.util.HashMap;

public interface CubeInterface {
    public CubeInterface clone();
    public void cloneInto(CubeInterface dest);
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
    public Boolean isSuperPop(int clusterSize);
    public CubeInterface SuperPop(int clusterSize, CubeFactory cf) throws UnknownClusterSizeException;
    public int minClusterPop();

    public boolean touchHandled(HashMap<String, Position> neighbours, int col, int row);

    public boolean atRest();

    public boolean onDelete();
}
