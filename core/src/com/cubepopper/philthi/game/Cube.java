package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Cube {
    public final int STATE_REST=0;
    public final int STATE_FALLING=1;
    public final int STATE_REMOVING=2;
    public final int STATE_REMOVED=3;
    private Texture texture;
    public Position pos;
    public Position gridPos;
    public Position desiredPos;
    public Size size;
    public float scale;
    public int state=STATE_REST;
    public int fallSpeed=200;
    public int scaleSpeed=1;

    public Cube(Texture texture) {
        this.texture = texture;
        this.desiredPos = this.pos = new Position();
        this.size = new Size(texture.getWidth(), texture.getHeight());
    }

    public Cube(Texture texture, float scale) {
        this(texture);
        this.scale = scale;
        this.size.width = texture.getWidth();
        this.size.height = texture.getHeight();

    }

    public Texture getTexture() {
        return this.texture;
    }

    public Cube clone() {
        Cube clone = new Cube(this.texture, this.scale);

        clone.size = new Size(this.size.width, this.size.height);
        clone.gridPos = new Position((int)this.gridPos.x, (int)this.gridPos.y);
        clone.pos = new Position((int)this.pos.x, (int)this.pos.y);
        clone.fallSpeed = fallSpeed;
        clone.scaleSpeed = scaleSpeed;
        clone.state = state;

        return clone;
    }

    public void Draw(Batch batch, float xOff, float yOff) {
        batch.draw(
            this.texture,
            xOff + (gridPos.x * this.scaledSize().width),
            yOff + (gridPos.y * this.scaledSize().height) + this.pos.y,
            this.scaledSize().width,
            this.scaledSize().height
        );
    }

    public Size scaledSize() {
        return new Size(size.width * scale, size.height * scale);
    }

    public void Process(float timeElapsed){
        if(state != STATE_REST) {
            Gdx.app.debug("cube popper", "processing cube at " + gridPos + ": state >" + state);
        }
        switch (state){
            case STATE_FALLING:
                if(pos.y > 0){
                    pos.y -= fallSpeed * timeElapsed;
                }
                if(pos.y <= 0) {
                    pos.y = 0;
                    state = STATE_REST;
                }
                break;
            case STATE_REMOVING:
                scale -= scaleSpeed * timeElapsed;
                Gdx.app.debug("cube popper", "scale: " + scale);
                if (scale <= 0){
                    scale = 0;
                    state = STATE_REMOVED;
                }
                break;
            case STATE_REST:
            default:
                break;
        }
    }

    public void Fall() {
        this.state = STATE_FALLING;
    }

    public void Remove() {
        this.state = STATE_REMOVING;
    }
}
