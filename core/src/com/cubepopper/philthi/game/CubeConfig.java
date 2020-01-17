package com.cubepopper.philthi.game;

public class CubeConfig {
    public String type;
    public float scale;
    public int spawnWeight = 100;
    public int score = 1;
    public int popGoal = 0;
    public boolean inPopulate = true;

    public CubeConfig(String type, float scale){
        this.type = type;
        this.scale = scale;
    }
    public CubeConfig(String type, float scale, int spawnWeight){
        this(type, scale);
        this.spawnWeight = spawnWeight;
    }
    public CubeConfig(String type, float scale, int spawnWeight, int score){
        this(type, scale, spawnWeight);
        this.score = score;
    }
    public CubeConfig(String type, float scale, int spawnWeight, int score, boolean inPopulate){
        this(type, scale, spawnWeight, score);
        this.inPopulate = inPopulate;
    }
}
