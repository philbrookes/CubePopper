package com.cubepopper.philthi.game.Level;


import com.badlogic.gdx.graphics.g2d.Batch;

public class Goal {
    public static final int SCORE = 1;
    public static final int CUBE_POPPED_COUNT = 2;
    public int type;
    public String name;
    public int value;
    public int currentValue;
    public String cubeType;

    public Goal(int type, String name, int value, String cubeType){
        this(type, name, value);
        this.cubeType = cubeType;
    }

    public Goal(int type, String name, int value){
        this(type, name);
        this.value = value;
    }

    public Goal(int type, String name) {
        this.type = type;
        this.name = name;
        this.value=0;
    }

    public void restart() {
        this.currentValue = 0;
    }

    public boolean complete() {
        if(this.value == 0) { return false; }
        return (this.currentValue >= this.value);
    }

    public void draw(Batch batch){

    }

    public String toString() {
        if(!complete()) {
            if(value == 0){
                return name + ": " + currentValue;
            }
            return name + ": " + currentValue + "/" + value;
        }
        return name + ": completed";
    }
}
