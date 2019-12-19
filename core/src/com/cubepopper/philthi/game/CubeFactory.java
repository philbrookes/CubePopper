package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.Random;

public class CubeFactory {
    protected Texture ruby = new Texture("ruby.png");
    protected Texture crystal = new Texture("crystal.png");
    protected Texture sand = new Texture("sand.png");
    protected Texture topaz = new Texture("topaz.png");

    public Cube LoadCube(CubeConfig config) {
        return new Cube(this.getTexture(config.type), config.type,0.25f);
    }

    private Texture getTexture(String name) {
        switch(name){
            case "ruby":
                return ruby;
            case "sand":
                return sand;
            case "topaz":
                return topaz;
            case "crystal":
            default:
                return crystal;
        }
    }

    public Cube RandomCube(CubeConfig[] cubes, Position gridPos){
        Cube cube = RandomCube(cubes);

        cube.gridPos = gridPos;
        cube.pos = new Position(gridPos.x * cube.scaledSize().width, gridPos.y * cube.scaledSize().height);
        return cube;
    }

    public Cube RandomCube(CubeConfig[] cubes) {
        Random rand = new Random();
        int n = rand.nextInt(cubes.length);
        return this.LoadCube(cubes[n]);
    }

    public void dispose() {
        ruby.dispose();
        crystal.dispose();
        sand.dispose();
        topaz.dispose();
    }
}
