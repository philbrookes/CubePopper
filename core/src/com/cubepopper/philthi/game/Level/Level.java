package com.cubepopper.philthi.game.Level;

import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.Cubes.CubeInterface;
import com.cubepopper.philthi.game.Cubes.PopCube;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Level {
    private static final float TIME_LIMIT=60.0f;
    protected int id;
    protected String name;
    protected Level next;
    protected CubeConfig[] cubes;
    protected int rows = 10;
    protected int columns = 10;
    protected float scale = 0.33f;
    protected int currentScore = 0;
    protected int maxScore = 0;
    protected Goal[] goals;
    protected float timeLimit = TIME_LIMIT;
    protected float timeExpired = 0.0f;

     public Level(){
         id = 1;
         name = "init";
         cubes = new CubeConfig[]{
             new CubeConfig("crystal", scale, 100),
             new CubeConfig("ruby", scale, 100),
             new CubeConfig("sand", scale, 100),
             new CubeConfig("topaz", scale, 100),
             new CubeConfig("dropper", scale, 15, 15, false, 5.0f),
             new CubeConfig("super_dropper", scale, 0, 100, false, 25.0f, false),
         };
         goals = new Goal[]{new Goal(Goal.SCORE, "score")};
     }

    public boolean complete(){
        for(Goal goal: goals){
            if(!goal.complete()){
                return false;
            }
        }
        return true;
    }

    public boolean outOfTime() {
         return timeLimit <= timeExpired;
    }

    public void restart(){
         this.timeExpired = 0;
         this.timeLimit = TIME_LIMIT;
         this.currentScore = 0;
        for(Goal goal: goals){
            goal.restart();
        }
    }


    public void increaseTimeLimit(float bonus) {
        this.timeLimit += bonus;
    }

    public void usedTime(float time) {
        this.timeExpired += time;
    }

    public float getTimeLimit() {
         return this.timeLimit;
    }

    public float getTimeExpired() {
         return timeExpired;
    }

    public void popped(CubeInterface cube) {
        currentScore += cube.getConfig().score;
        for(Goal goal: goals){
            if(goal.type == goal.SCORE) {
                goal.currentValue = currentScore;
            } else if (goal.type == goal.CUBE_POPPED_COUNT && goal.cubeType == cube.getConfig().type){
                goal.currentValue += 1;
            }
        }

        this.increaseTimeLimit(cube.getConfig().timeBonus);
    }

    public CubeConfig[] getSpawnCubes() {
         ArrayList<CubeConfig> retCubes = new ArrayList<CubeConfig>();
        for(CubeConfig cube: cubes){
            if(cube.doesSpawn) {
                retCubes.add(cube);
            }
        }
        CubeConfig[] simpleCubes = new CubeConfig[retCubes.size()];
        retCubes.toArray(simpleCubes);
        return simpleCubes;
     }

    public CubeConfig[] getCubes() {
        return cubes;
    }

    public CubeConfig getCubeConfig(String type) {
        for (CubeConfig cube: cubes) {
            if(cube.type == type) {
                return cube;
            }
        }

        return new CubeConfig("", 0.0f);
    }

    public float getScale() {
        return scale;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getId() {
        return id;
    }

    public Level getNext() {
        return next;
    }

    public String getName() {
        return name;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void addScore(int score) {
         currentScore += score;
    }

    public Goal[] getGoals() {
        return goals;
    }
}
