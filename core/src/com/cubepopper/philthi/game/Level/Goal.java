package com.cubepopper.philthi.game.Level;


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
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public boolean complete() {
        return (this.currentValue >= this.value);
    }

    public String toString() {
        if(!complete()) {
            return name + ": " + currentValue + "/" + value;
        } else {
            return name + ": completed";
        }
    }
}
