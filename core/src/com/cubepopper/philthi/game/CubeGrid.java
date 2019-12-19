package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

import java.util.Collection;
import java.util.HashMap;

import static com.cubepopper.philthi.game.Cube.STATE_FALLING;
import static com.cubepopper.philthi.game.Cube.STATE_REMOVED;
import static com.cubepopper.philthi.game.Cube.STATE_REST;

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

    public void dispose() {
        cf.dispose();
    }

    public CubeGrid(int rows, int columns, Batch batch) {
        this();
        this.rows = rows;
        this.columns = columns;
        this.cubes = new Cube[columns][rows];
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
        CubeConfig[] configs = new CubeConfig[]{crystal, ruby, sand, topaz};
        cubes[delCol][delRow] = null;
        //update the column, to fall down
        for(int row=delRow;row<=rows-1;row++) {
            //not top row, copy from row above
            if(row != rows-1) {
                cubes[delCol][row] = cubes[delCol][row + 1].clone();
                cubes[delCol][row].gridPos = new Position(delCol, row);
            } else { //top row, generate new cube
                cubes[delCol][row] = cf.RandomCube(configs, new Position(delCol, row));
                cubes[delCol][row].pos.y = cubes[delCol][row-1].pos.y + (cubes[delCol][row].scaledSize().height * 2);
            }
        }
    }

    private void populate() {
        CubeConfig[] configs = new CubeConfig[]{crystal, ruby, sand, topaz};
        for(int row=0;row<rows;row++) {
            for (int col=0;col<columns;col++){
                cubes[col][row] = cf.RandomCube(configs, new Position(col, row));
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
                cubes[col][row].Draw(batch);
            }
        }
    }

    public boolean touch(int touchX, int touchY){
        if(touchX < xOff || touchX > (rows * cf.LoadCube(new CubeConfig("ruby")).scaledSize().width) + xOff ||
            touchY < yOff || touchY > (rows * cf.LoadCube(new CubeConfig("ruby")).scaledSize().height) + yOff){
            return false;
        }

        int col = (touchX - xOff) / (int)(cf.LoadCube(new CubeConfig("ruby")).scaledSize().width);
        int row = (touchY - yOff) / (int)(cf.LoadCube(new CubeConfig("ruby")).scaledSize().height);
        HashMap<String, Position> neighbours = findNeighbours(col, row, new HashMap<String, Position>());
        if(neighbours.values().size() >= 2) {
            removeCubes(neighbours.values());
        }
        return true;
    }

    public HashMap<String, Position> findNeighbours(int col, int row, HashMap<String, Position> neighbours){
        neighbours.put(new Position(col, row).toString(), new Position(col, row));
        Cube cube = cubes[col][row];
        if(row > 0){
            if(cubes[col][row-1].state == STATE_REST && cubes[col][row-1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col, row-1).toString())){
                neighbours = findNeighbours(col, row-1, neighbours);
            }
        }
        if(row < rows-1){
            if(cubes[col][row+1].state == STATE_REST && cubes[col][row+1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col, row+1).toString())){
                neighbours = findNeighbours(col, row+1, neighbours);
            }
        }
        if(col > 0){
            if(cubes[col-1][row].state == STATE_REST && cubes[col-1][row].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col-1, row).toString())){
                neighbours = findNeighbours(col-1, row, neighbours);
            }
        }
        if(col < columns-1){
            if(cubes[col+1][row].state == STATE_REST && cubes[col+1][row].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col+1, row).toString())){
                neighbours = findNeighbours(col+1, row, neighbours);
            }
        }
        return neighbours;
    }
}
