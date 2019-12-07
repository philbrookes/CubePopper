package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Collection;
import java.util.HashMap;

import sun.rmi.runtime.Log;

public class CubeGrid {
    public final int STATE_ACTIVE=0;
    public final int STATE_INACTIVE=1;

    private int rows;
    private int columns;
    private Cube[][] cubes;

    private CubeFactory cf;
    private CubeConfig crystal, ruby, sand, topaz;
    private Batch batch;

    private int xOff=0, yOff=0;

    private int state = STATE_ACTIVE;

    public void dispose() {
        cf.dispose();
    }

    public CubeGrid(int rows, int columns, Batch batch) {
        this();
        this.rows = rows;
        this.columns = columns;
        this.cubes = new Cube[rows][columns];
        this.batch = batch;
        populate();
        position();
    }

    private CubeGrid() {
        this.cf = new CubeFactory();
        crystal = new CubeConfig("crystal");
        ruby = new CubeConfig("ruby");
        sand = new CubeConfig("sand");
        topaz = new CubeConfig("topaz");
    }

    public void removeCubes(Collection<Position> positions) {
        for(int i=rows-1;i>=0;i--){
            for (Position pos:positions) {
                if((int)pos.x == i){
                    cubes[(int)pos.x][(int)pos.y].Remove();
                }
            }
        }
    }

    public void deleteCube(int delRow, int delCol){
        CubeConfig[] configs = new CubeConfig[]{crystal, ruby, sand, topaz};
        cubes[delRow][delCol] = null;
        //update the column, to fall down
        for(int row=delRow;row<=rows-1;row++) {
            //not top row, copy from row above
            if(row != rows-1) {
                cubes[row][delCol] = cubes[row + 1][delCol].clone();
                cubes[row][delCol].pos.y += cubes[row][delCol].scaledSize().height;
            } else { //top row, generate new cube
                cubes[row][delCol] = cf.RandomCube(configs);
                cubes[row][delCol].gridPos = new Position(row, delCol);
                cubes[row][delCol].pos.y += cubes[row][delCol].scaledSize().height;
            }
            cubes[row][delCol].state = cubes[row][delCol].STATE_FALLING;
        }
    }

    private void populate() {
        CubeConfig[] configs = new CubeConfig[]{crystal, ruby, sand, topaz};
        for(int row=0;row<rows;row++) {
            for (int col=0;col<columns;col++){
                cubes[row][col] = cf.RandomCube(configs);
                cubes[row][col].gridPos = new Position(row, col);
            }
        }
    }

    private void position() {
        Size cubeSize = new Size(cubes[0][0].scaledSize().height, cubes[0][0].scaledSize().width);
        int width = (int)cubeSize.width * columns;
        int height = (int)cubeSize.height * rows;
        xOff = (Gdx.graphics.getBackBufferWidth() - width) / 2;
        yOff = (Gdx.graphics.getBackBufferHeight() - height) / 2;
    }

    public void draw() {
        if(state==STATE_ACTIVE) {
            for (int col = 0; col < columns; col++) {
                for (int row = 0; row < rows; row++) {
                    if (cubes[row][col] == null) {
                        continue;
                    }
                    Cube cube = cubes[row][col];
                    cube.Process(Gdx.graphics.getDeltaTime());
                    if(cube.state == cube.STATE_REMOVED){
                        deleteCube(row, col);
                    } else {
                        cube.Draw(batch, xOff, yOff);
                    }
                }
            }
        } else if(state==STATE_INACTIVE){
                state = STATE_ACTIVE;
        }
    }

    public boolean touch(int touchX, int touchY){
        if(state == STATE_INACTIVE){
            return false;
        }
        for (int col=0;col<columns;col++){
            for(int row=0;row<rows;row++) {
                if(cubes[row][col] == null) {
                    continue;
                }
                Cube cube = cubes[row][col];
                if(touchX >= xOff + (col * cube.scaledSize().width) && touchX <= xOff + (col * cube.scaledSize().width) + cube.scaledSize().width &&
                    touchY >= yOff + (row * cube.scaledSize().height) && touchY <= yOff + (row * cube.scaledSize().height) + cube.scaledSize().height){
                    Gdx.app.debug("cube popper", "touched cube at: " + cube.gridPos);
                    HashMap<String, Position> neighbours = findNeighbours(row, col, new HashMap<String, Position>());
                    if(neighbours.values().size() >= 2) {
                        this.state = STATE_INACTIVE;
                        Gdx.app.debug("cube popper", "removing a cube");
                        removeCubes(neighbours.values());
                    }
                }
            }
        }
        return false;
    }

    public HashMap<String, Position> findNeighbours(int row, int col, HashMap<String, Position> neighbours){
        neighbours.put(new Position(row, col).toString(), new Position(row, col));
        Cube cube = cubes[row][col];
        if(row > 0){
            if(cubes[row-1][col].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(row-1, col).toString())){
                neighbours = findNeighbours(row-1, col, neighbours);
            }
        }
        if(row < rows-1){
            if(cubes[row+1][col].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(row+1, col).toString())){
                neighbours = findNeighbours(row+1, col, neighbours);
            }
        }
        if(col > 0){
            if(cubes[row][col-1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(row, col-1).toString())){
                neighbours = findNeighbours(row, col-1, neighbours);
            }
        }
        if(col < columns-1){
            if(cubes[row][col+1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(row, col+1).toString())){
                neighbours = findNeighbours(row, col+1, neighbours);
            }
        }
        return neighbours;
    }
}
