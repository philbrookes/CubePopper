package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.cubepopper.philthi.game.Cubes.CubeInterface;
import com.cubepopper.philthi.game.Cubes.PopCube;

import java.util.Collection;
import java.util.HashMap;

import static com.cubepopper.philthi.game.Cubes.PopCube.STATE_REMOVED;
import static com.cubepopper.philthi.game.Cubes.PopCube.STATE_REST;

public class CubeGrid {
    private int rows;
    private int columns;
    private CubeInterface[][] cubes;

    private CubeFactory cf;
    private CubeConfig crystal, ruby, sand, topaz, dropper;
    private Batch batch;

    private int xOff=0, yOff=0;

    private int score = 0;

    private CubeConfig[] spawnCubes;

    public void dispose() {
        cf.dispose();
    }

    public CubeGrid(int rows, int columns, Batch batch, CubeConfig[] spawnCubes) {
        this(spawnCubes);
        this.rows = rows;
        this.columns = columns;
        this.cubes = new PopCube[columns][rows];
        this.batch = batch;
        populate();
        position();
    }

    private CubeGrid(CubeConfig[] spawnCubes) {
        this.spawnCubes = spawnCubes;
        this.cf = new CubeFactory();
    }

    public int getScore(){
        return this.score;
    }

    public void removeCubes(Collection<Position> positions) {
        for(int i=rows-1;i>=0;i--){
            for (Position pos:positions) {
                if((int)pos.y == i){
                    deleteCube((int)pos.x, (int)pos.y);
                }
            }
        }
    }

    private void logGrid() {
        String sentence = "    ";
        for(int col=0; col <= columns-1; col++){
            sentence += " " + col + " ";
        }
        Gdx.app.debug("cube popper", sentence);
        sentence = "    ";
        for(int col=0; col <= columns-1; col++){
            sentence += " v ";
        }
        Gdx.app.debug("cube popper", sentence);

        for (int row=rows-1;row>=0;row--){
            sentence = "";
            for(int col=0; col <= columns-1; col++){
                sentence += cubes[col][row].toString();
            }
            Gdx.app.debug("cube popper", row + " > " + sentence);
        }
    }

    public void deleteCube(int delCol, int delRow){
        score += cubes[delCol][delRow].getConfig().score;
        cubes[delCol][delRow].onDelete();
        cubes[delCol][delRow] = null;
        //update the column, to fall down
        for(int row=delRow;row<=rows-1;row++) {
            //not top row, copy from row above
            if(row != rows-1) {
                cubes[delCol][row] = cubes[delCol][row + 1].clone();
                cubes[delCol][row].setGridPos(new Position(delCol, row));
            } else { //top row, generate new cube
                cubes[delCol][row] = cf.RandomCube(spawnCubes, new Position(delCol, row), false);
                cubes[delCol][row].getPos().y = cubes[delCol][row-1].getPos().y + (cubes[delCol][row].scaledSize().height * 2);
            }
        }
    }

    private void populate() {
        for(int row=0;row<rows;row++) {
            for (int col=0;col<columns;col++){
                cubes[col][row] = cf.RandomCube(spawnCubes, new Position(col, row), true);
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
        Matrix4 offset = new Matrix4().trn(xOff, yOff, 0);
        batch.setTransformMatrix(offset);
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                cubes[col][row].Process(Gdx.graphics.getDeltaTime());
                if(cubes[col][row].getState() == STATE_REMOVED){
                    deleteCube(col, row);
                }
                cubes[col][row].Draw(batch);
            }
        }
    }

    public boolean touch(int touchX, int touchY, float scale){
        int col = (touchX - xOff) / (int)(cf.LoadCube(new CubeConfig("ruby", scale)).scaledSize().width);
        int row = (touchY - yOff) / (int)(cf.LoadCube(new CubeConfig("ruby", scale)).scaledSize().height);
        if(col < 0 || col > columns-1 || row < 0 || row > rows-1){
            return false;
        }
        Gdx.app.debug("cube popper", "touched a " + cubes[col][row].getConfig().type + ", touch handled? " + cubes[col][row].touchHandled(col, row));
        if(!cubes[col][row].touchHandled(col, row)) {
            HashMap<String, Position> neighbours = findNeighbours(col, row, new HashMap<String, Position>());
            if (neighbours.values().size() >= 2) {
                removeCubes(neighbours.values());
            }
        }
        return true;
    }

    public HashMap<String, Position> findNeighbours(int col, int row, HashMap<String, Position> neighbours){
        neighbours.put(new Position(col, row).toString(), new Position(col, row));
        CubeInterface cube = cubes[col][row];
        if(row > 0){
            if(cubes[col][row-1].getState() == STATE_REST && cubes[col][row-1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col, row-1).toString())){
                neighbours = findNeighbours(col, row-1, neighbours);
            }
        }
        if(row < rows-1){
            if(cubes[col][row+1].getState() == STATE_REST && cubes[col][row+1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col, row+1).toString())){
                neighbours = findNeighbours(col, row+1, neighbours);
            }
        }
        if(col > 0){
            if(cubes[col-1][row].getState() == STATE_REST && cubes[col-1][row].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col-1, row).toString())){
                neighbours = findNeighbours(col-1, row, neighbours);
            }
        }
        if(col < columns-1){
            if(cubes[col+1][row].getState() == STATE_REST && cubes[col+1][row].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col+1, row).toString())){
                neighbours = findNeighbours(col+1, row, neighbours);
            }
        }
        return neighbours;
    }
}
