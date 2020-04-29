package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubepopper.philthi.game.Cubes.CubeInterface;
import com.cubepopper.philthi.game.Cubes.DropperCube;
import com.cubepopper.philthi.game.Cubes.PopCube;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CubeFactory {
    public Map<String, Texture> Textures;

    public CubeFactory(){
        Textures = new HashMap<>();
        Textures.put("ruby", new Texture("ruby.png"));
        Textures.put("crystal", new Texture("crystal.png"));
        Textures.put("sand", new Texture("sand.png"));
        Textures.put("topaz", new Texture("topaz.png"));
        Textures.put("dropper", new Texture("time_bonus.png"));
        Textures.put("super_dropper", new Texture("super_time_bonus.png"));
    }

    public CubeInterface LoadCube(CubeConfig config) {
        switch(config.type){
            case "dropper":
            case "super_dropper":
                return new DropperCube(Textures.get(config.type), config);
            default:
                return new PopCube(Textures.get(config.type), config);
        }

    }


    public CubeInterface RandomCube(CubeConfig[] cubes, Position gridPos, boolean isPopulate){
        CubeInterface cube = RandomCube(cubes, isPopulate);

        cube.setGridPos(gridPos);
        cube.setPos(new Position(gridPos.x * cube.scaledSize().width, gridPos.y * cube.scaledSize().height));
        return cube;
    }

    public CubeInterface RandomCube(CubeConfig[] cubes, boolean isPopulate) {
        int totalChances=0;
        //add up all the weights
        for(CubeConfig cube: cubes){
            if(isPopulate && !cube.inPopulate || !cube.doesSpawn){
                continue;
            }
            totalChances += cube.spawnWeight;
        }
        Random rand = new Random();
        int n = rand.nextInt(totalChances);
        for(CubeConfig cube: cubes){
            if(isPopulate && !cube.inPopulate || !cube.doesSpawn){
                continue;
            }
            n -= cube.spawnWeight;
            if(n<=0){
                return this.LoadCube(cube);
            }
        }
        return this.LoadCube(cubes[0]);

    }

    public void dispose() {
        Textures.get("ruby").dispose();
        Textures.get("crystal").dispose();
        Textures.get("sand").dispose();
        Textures.get("topaz").dispose();
        Textures.get("dropper").dispose();
        Textures.get("super_dropper").dispose();
    }
}
