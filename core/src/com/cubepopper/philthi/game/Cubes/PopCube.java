package com.cubepopper.philthi.game.Cubes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.CubeFactory;
import com.cubepopper.philthi.game.Position;
import com.cubepopper.philthi.game.Size;

import java.util.HashMap;

public class PopCube implements CubeInterface{
    public static final int STATE_REST=0;
    public static final int STATE_FALLING=1;
    public static final int STATE_REMOVED=2;
    protected Texture texture;
    public Position pos;
    public Position gridPos;
    public Size size;
    public float scale;
    public int state=STATE_REST;
    public int moveSpeed =1000;
    public int scaleSpeed=1;
    public CubeConfig config;

    public PopCube(Texture texture, CubeConfig config) {
        this.config = config;
        this.texture = texture;
        this.pos = new Position();
        this.size = new Size(texture.getWidth(), texture.getHeight());
        this.scale = config.scale;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public PopCube clone() {
        PopCube clone = new PopCube(this.texture, this.config);
        clone.size = new Size(this.size.width, this.size.height);
        clone.gridPos = new Position((int)this.gridPos.x, (int)this.gridPos.y);
        clone.pos = new Position((int)this.pos.x, (int)this.pos.y);
        clone.moveSpeed = moveSpeed;
        clone.scaleSpeed = scaleSpeed;
        clone.state = state;

        return clone;
    }

    public void cloneInto(CubeInterface dest) {
        PopCube d = (PopCube)dest;
        d.size = new Size(this.size.width, this.size.height);
        d.gridPos = new Position((int)this.gridPos.x, (int)this.gridPos.y);
        d.pos = new Position((int)this.pos.x, (int)this.pos.y);
        d.moveSpeed = moveSpeed;
        d.scaleSpeed = scaleSpeed;
        d.state = state;
        return;
    }

    public void Draw(Batch batch) {
        batch.draw(
            this.texture,
            pos.x,
            pos.y,
            this.scaledSize().width,
            this.scaledSize().height
        );
    }

    public Size scaledSize() {
        return new Size(size.width * scale, size.height * scale);
    }

    public CubeInterface SuperPop(int clusterSize, CubeFactory cf) throws UnknownClusterSizeException {
        if(clusterSize >= 25) {
            return cf.LoadDropper();
        }
        throw new UnknownClusterSizeException();
    }

    @Override
    public int minClusterPop() {
        return 2;
    }

    public Boolean isSuperPop(int clusterSize) {
        return clusterSize >= 25;
    }

    public void Process(float timeElapsed){
        Position dest = new Position(gridPos.x * scaledSize().width, gridPos.y * scaledSize().height);
        if(pos.approach(dest, (this.moveSpeed * timeElapsed))){
            state = STATE_REST;
            moveSpeed = 1000;
            atRest();
        } else {
            state = STATE_FALLING;
            moveSpeed += 50;
        }
    }

    public String toString(){
        return "[" + config.type.substring(0, 1).toUpperCase() + "]";
    }

    @Override
    public Position getGridPos() {
        return gridPos;
    }

    @Override
    public void setGridPos(Position newGridPos) {
        gridPos = newGridPos;
    }

    @Override
    public Position getPos() {
        return pos;
    }

    @Override
    public void setPos(Position newPos) {
        pos = newPos;
    }

    @Override
    public int getState() {
        Position dest = new Position(gridPos.x * scaledSize().width, gridPos.y * scaledSize().height);
        if(pos.distance(dest) > 0){
            state = STATE_FALLING;
        }
        return state;
    }

    public boolean touchHandled(HashMap<String, Position> neighbours, int col, int row) {
        return false;
    }

    @Override
    public boolean atRest() {
        return false;
    }

    @Override
    public boolean onDelete() {
        return false;
    }

    @Override
    public CubeConfig getConfig() {
        return config;
    }
}
