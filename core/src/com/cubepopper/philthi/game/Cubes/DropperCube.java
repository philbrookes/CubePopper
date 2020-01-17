package com.cubepopper.philthi.game.Cubes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.Position;
import com.cubepopper.philthi.game.Size;

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
    public boolean touchHandled(int col, int row) {
        super.touchHandled(col, row);
        Gdx.app.debug("cube popper", "dropper touch");
        return true;
    }

    @Override
    public DropperCube clone() {
        DropperCube clone = new DropperCube(this.texture, this.config);
        clone.size = new Size(this.size.width, this.size.height);
        clone.gridPos = new Position((int)this.gridPos.x, (int)this.gridPos.y);
        clone.pos = new Position((int)this.pos.x, (int)this.pos.y);
        clone.moveSpeed = moveSpeed;
        clone.scaleSpeed = scaleSpeed;
        clone.state = state;

        return clone;
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
