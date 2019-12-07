package com.cubepopper.philthi.game;

public class Position {
    public float x;
    public float y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0, 0);
    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
}
