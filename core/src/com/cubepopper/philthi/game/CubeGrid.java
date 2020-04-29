package com.cubepopper.philthi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.cubepopper.philthi.game.Cubes.CubeInterface;
import com.cubepopper.philthi.game.Cubes.DropperCube;
import com.cubepopper.philthi.game.Cubes.PopCube;
import com.cubepopper.philthi.game.Cubes.UnknownClusterSizeException;
import com.cubepopper.philthi.game.Level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static com.cubepopper.philthi.game.Cubes.PopCube.STATE_REMOVED;
import static com.cubepopper.philthi.game.Cubes.PopCube.STATE_REST;

public class CubeGrid {
    private Level level;
    private CubeInterface[][] cubes;

    private CubeFactory cf;
    private Batch batch;

    private int minClusterSize = 2;

    private int xOff=0, yOff=0;

    public void dispose() {
        cf.dispose();
    }

    public CubeGrid(Level level, Batch batch) {
        this();
        this.level = level;
        this.cubes = new PopCube[level.getColumns()][level.getRows()];
        this.batch = batch;
        populate();
        position();
    }

    private CubeGrid() {
        this.cf = new CubeFactory();
    }

    public void restart(){
        populate();
        position();
    }

    public void removeCubes(Collection<Position> positions) {
        for(int i=level.getRows()-1;i>=0;i--){
            for (Position pos:positions) {
                if((int)pos.y == i){
                    deleteCube((int)pos.x, (int)pos.y, false);
                }
            }
        }
    }

    private void logGrid() {
        String sentence = "    ";
        for(int col=0; col <= level.getColumns()-1; col++){
            sentence += " " + col + " ";
        }
        Gdx.app.debug("cube popper", sentence);
        sentence = "    ";
        for(int col=0; col <= level.getColumns()-1; col++){
            sentence += " v ";
        }
        Gdx.app.debug("cube popper", sentence);

        for (int row=level.getRows()-1;row>=0;row--){
            sentence = "";
            for(int col=0; col <= level.getColumns()-1; col++){
                sentence += cubes[col][row].toString();
            }
            Gdx.app.debug("cube popper", row + " > " + sentence);
        }
    }

    public void deleteCube(int delCol, int delRow, boolean noDrop){
        level.popped(cubes[delCol][delRow]);
        cubes[delCol][delRow].onDelete();
        cubes[delCol][delRow] = null;
        if(noDrop) {
            return;
        }
        //update the column, to fall down
        for(int row=delRow;row<=level.getRows()-1;row++) {
            //not top row, copy from row above
            if(row != level.getRows()-1) {
                cubes[delCol][row] = cubes[delCol][row + 1].clone();
                cubes[delCol][row].setGridPos(new Position(delCol, row));
            } else { //top row, generate new cube
                cubes[delCol][row] = cf.RandomCube(level.getSpawnCubes(), new Position(delCol, row), false);
                cubes[delCol][row].getPos().y = cubes[delCol][row-1].getPos().y + (cubes[delCol][row].scaledSize().height * 2);
            }
        }
    }

    private void populate() {
        for(int row=0;row<level.getRows();row++) {
            for (int col=0;col<level.getColumns();col++){
                cubes[col][row] = cf.RandomCube(level.getSpawnCubes(), new Position(col, row), true);
            }
        }
    }

    private void position() {
        Size cubeSize = new Size(cubes[0][0].scaledSize().height, cubes[0][0].scaledSize().width);
        int width = (int)cubeSize.width * level.getColumns();
        int height = (int)cubeSize.height * level.getRows();
        xOff = (Gdx.graphics.getBackBufferWidth() - width) / 2;
        yOff = (Gdx.graphics.getBackBufferHeight() - height) / 2;
    }

    private void checkValidMoves(){
        for (int col = 0; col < level.getColumns(); col++) {
            for (int row = 0; row < level.getRows(); row++) {
                CubeInterface cube = cubes[col][row];
                //cubes are still moving, abort check
                if(cube.getState() != STATE_REST) {
                    return;
                }

                //cannot be touched, so ignore testing for moves
                if(cube.getConfig().type == "super_dropper"){
                    continue;
                }

                //check for a valid move and return if found
                HashMap<String, Position> neighbours = findNeighbours(col, row, new HashMap<String, Position>());
                if(cube.getConfig().type != "dropper" && neighbours.size() >= minClusterSize) {
                    return;
                }
                if(cube.getConfig().type == "dropper" && neighbours.size() >= 5) {
                    return;
                }
            }
        }

        //if we got here, there are no valid moves left
        shuffleBoard();
        logGrid();
    }

    private void shuffleBoard(){
        ArrayList<CubeInterface> cubeLine = new ArrayList<CubeInterface>();

        for (int col = 0; col < level.getColumns(); col++) {
            for (int row = 0; row < level.getRows(); row++) {
                cubeLine.add(cubes[col][row]);
            }
        }

        Collections.shuffle(cubeLine);

        int i=0;
        for (int col = 0; col < level.getColumns(); col++) {
            for (int row = 0; row < level.getRows(); row++) {
                cubeLine.get(i).setGridPos(new Position(col, row));
                cubes[col][row] = cubeLine.get(i);
                i++;
            }
        }

    }

    public void draw() {
        checkValidMoves();
        Matrix4 offset = new Matrix4().trn(xOff, yOff, 0);
        batch.setTransformMatrix(offset);
        for (int col = 0; col < level.getColumns(); col++) {
            for (int row = 0; row < level.getRows(); row++) {
                cubes[col][row].Process(Gdx.graphics.getDeltaTime());
                if(cubes[col][row].getState() == STATE_REMOVED){
                    deleteCube(col, row, false);
                }
                cubes[col][row].Draw(batch);
            }
        }
    }

    public boolean touch(int touchX, int touchY){
        int col = (touchX - xOff) / (int)(cf.LoadCube(new CubeConfig("ruby", level.getScale())).scaledSize().width);
        int row = (touchY - yOff) / (int)(cf.LoadCube(new CubeConfig("ruby", level.getScale())).scaledSize().height);
        if(col < 0 || col > level.getColumns()-1 || row < 0 || row > level.getRows()-1){
            return false;
        }
        CubeInterface touchedCube = cubes[col][row];
        HashMap<String, Position> neighbours = findNeighbours(col, row, new HashMap<String, Position>());
        int clusterSize = neighbours.values().size();
        if(touchedCube.isSuperPop(clusterSize)){
            Gdx.app.log("CUBE POPPER", "got a super pop size: " + clusterSize);
            try {
                CubeInterface newCube = touchedCube.SuperPop(clusterSize, cf);
                touchedCube.cloneInto(newCube);
                int rowId=row;
                while(rowId > 0 && neighbours.containsKey(new Position(col, rowId-1).toString())){
                    rowId--;
                }
                newCube.setGridPos(new Position(col, rowId));
                cubes[col][rowId] = newCube;
//                //don't delete this cube now, as we have replaced it with a super cube
                neighbours.remove(new Position(col, rowId).toString());
                Gdx.app.log("CUBE POPPER", "neighbours size: " + neighbours.values().size());
            } catch (UnknownClusterSizeException e) {
                Gdx.app.error("CUBE POPPER", "bad sync up between isSuperPop and SuperPop in cube type: " + touchedCube.getConfig().type);
                return true;
            }
        }
        if(!touchedCube.touchHandled(neighbours, col, row)) {
            if(clusterSize >= touchedCube.minClusterPop()) {
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
        if(row < level.getRows()-1){
            if(cubes[col][row+1].getState() == STATE_REST && cubes[col][row+1].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col, row+1).toString())){
                neighbours = findNeighbours(col, row+1, neighbours);
            }
        }
        if(col > 0){
            if(cubes[col-1][row].getState() == STATE_REST && cubes[col-1][row].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col-1, row).toString())){
                neighbours = findNeighbours(col-1, row, neighbours);
            }
        }
        if(col < level.getColumns()-1){
            if(cubes[col+1][row].getState() == STATE_REST && cubes[col+1][row].getTexture() == cube.getTexture() && !neighbours.containsKey(new Position(col+1, row).toString())){
                neighbours = findNeighbours(col+1, row, neighbours);
            }
        }
        return neighbours;
    }
}
