package com.cubepopper.philthi.game.Cubes;

public class UnknownClusterSizeException extends Exception {
    @Override
    public String getMessage() {
        return "The specified cluster size is unknown";
    }
}
