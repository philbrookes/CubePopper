package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;

public class Position {
    public float x;
    public float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0, 0);
    }

    public float distance(Position other){
        return (float)Math.sqrt(((x - other.x)*(x - other.x)) + ((y - other.y)*(y - other.y)));
    }


    public Position getDirection(Position other){
        return new Position(other.x - x, other.y - y);

    }

    public Position getDirectionNormal(Position other){
        Position dir = getDirection(other);
        float dist = distance(other);
        return new Position(dir.x / dist, dir.y / dist);
    }

    public boolean approach(Position other, float movement) {
        Position vector = getDirectionNormal(other);
        if(distance(other) > movement) {
            x += vector.x * movement;
            y += vector.y * movement;
            return false;
        }
        x = other.x;
        y = other.y;
        return true;
    }

    @Override
    public String toString() {
        return "(x: " + this.x + ", y: " + this.y + ")";
    }
}
