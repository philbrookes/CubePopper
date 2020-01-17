package com.cubepopper.philthi.game.Level;

import com.cubepopper.philthi.game.CubeConfig;
import com.cubepopper.philthi.game.Cubes.CubeInterface;
import com.cubepopper.philthi.game.Cubes.PopCube;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Level {
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
     public Level(){
         id = 1;
         name = "init";
         cubes = new CubeConfig[]{
             new CubeConfig("crystal", scale, 100),
             new CubeConfig("ruby", scale, 100),
             new CubeConfig("sand", scale, 100),
             new CubeConfig("topaz", scale, 100),
             new CubeConfig("dropper", scale, 15, 15, false),
         };
         goals = new Goal[]{
                 new Goal(Goal.SCORE, "score", 200),
                 new Goal(Goal.CUBE_POPPED_COUNT, "ships", 5, "dropper"),
         };
     }

     public boolean complete(){
         for(Goal goal: goals){
             if(!goal.complete()){
                 return false;
             }
         }
         return true;
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
    }

    public CubeConfig[] getCubes() {
        return cubes;
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
