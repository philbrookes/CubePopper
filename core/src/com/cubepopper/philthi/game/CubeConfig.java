package com.cubepopper.philthi.game;

public class CubeConfig {
    public String type;
    public float scale;
    public int spawnWeight = 100;
    public int score = 1;

    public CubeConfig(String type, float scale){
        this.type = type;
        this.scale = scale;
    }
    public CubeConfig(String type, float scale, int spawnWeight){
        this(type, scale);
        this.spawnWeight = spawnWeight;
    }
}
