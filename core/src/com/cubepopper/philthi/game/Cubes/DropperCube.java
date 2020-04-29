package com.cubepopper.philthi.game.Cubes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.Position;
import com.cubepopper.philthi.game.Size;

import java.util.HashMap;

public class DropperCube extends PopCube {
    public DropperCube(Texture texture, CubeConfig config) {
        super(texture, config);
        this.config = config;
        this.texture = texture;
        this.pos = new Position();
        this.size = new Size(texture.getWidth(), texture.getHeight());
        this.scale = config.scale;
    }

    @Override
    public boolean touchHandled(HashMap<String, Position> neighbours, int col, int row) {
        super.touchHandled(neighbours, col, row);
        Gdx.app.debug("cube popper", "dropper touch");
        return true;
    }

    @Override
    public DropperCube clone() {
        DropperCube clone = new DropperCube(this.texture, this.config);
        cloneInto(clone);
        return clone;
    }

    public void cloneInto(CubeInterface dest) {
        DropperCube d = (DropperCube)dest;
        d.size = new Size(this.size.width, this.size.height);
        d.gridPos = new Position((int)this.gridPos.x, (int)this.gridPos.y);
        d.pos = new Position((int)this.pos.x, (int)this.pos.y);
        d.moveSpeed = moveSpeed;
        d.scaleSpeed = scaleSpeed;
        d.state = state;
        return;
    }


    @Override
    public boolean atRest() {
        if(gridPos.y == 0){
            this.state = STATE_REMOVED;
        }
        return true;
    }

    @Override
    public boolean onDelete() {
        return false;
    }
}
