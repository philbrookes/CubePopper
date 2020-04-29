package com.cubepopper.philthi.game;

public class CubeConfig {
    public String type;
    public float scale;
    public int spawnWeight = 100;
    public int score = 1;
    public int popGoal = 0;
    public float timeBonus = 0;
    public boolean inPopulate = true;
    public boolean doesSpawn = true;

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
    public CubeConfig(String type, float scale, int spawnWeight, int score, boolean inPopulate, float timeBonus){
        this(type, scale, spawnWeight, score, inPopulate);
        this.timeBonus = timeBonus;
    }
    public CubeConfig(String type, float scale, int spawnWeight, int score, boolean inPopulate, float timeBonus, boolean doesSpawn){
        this(type, scale, spawnWeight, score, inPopulate, timeBonus);
        this.doesSpawn = doesSpawn;
    }
}
