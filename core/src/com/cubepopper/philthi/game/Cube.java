package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Cube {
    public static final int STATE_REST=0;
    public static final int STATE_FALLING=1;
    public static final int STATE_REMOVED=3;
    private Texture texture;
    public Position pos;
    public Position gridPos;
    public Size size;
    public float scale;
    public int state=STATE_REST;
    public int moveSpeed =1000;
    public int scaleSpeed=1;
    public String type="";

    public Cube(Texture texture, String type) {
        this.type = type;
        this.texture = texture;
        this.pos = new Position();
        this.size = new Size(texture.getWidth(), texture.getHeight());
    }

    public Cube(Texture texture, String type, float scale) {
        this(texture, type);
        this.scale = scale;
        this.size.width = texture.getWidth();
        this.size.height = texture.getHeight();

    }

    public Texture getTexture() {
        return this.texture;
    }

    public Cube clone() {
        Cube clone = new Cube(this.texture, this.type, this.scale);

        clone.size = new Size(this.size.width, this.size.height);
        clone.gridPos = new Position((int)this.gridPos.x, (int)this.gridPos.y);
        clone.pos = new Position((int)this.pos.x, (int)this.pos.y);
        clone.moveSpeed = moveSpeed;
        clone.scaleSpeed = scaleSpeed;
        clone.state = state;

        return clone;
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

    public void Process(float timeElapsed){
        Position dest = new Position(gridPos.x * scaledSize().width, gridPos.y * scaledSize().height);
        if(pos.approach(dest, (this.moveSpeed * timeElapsed))){
            state = STATE_REST;
            moveSpeed = 1000;
        } else {
            state = STATE_FALLING;
            moveSpeed += 20;
        }
    }

    public String toString(){
        return "[" + type.substring(0, 1).toUpperCase() + "]";
    }
}
