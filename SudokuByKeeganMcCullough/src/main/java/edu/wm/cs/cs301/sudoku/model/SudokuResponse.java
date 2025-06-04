package edu.wm.cs.cs301.sudoku.model;

import java.awt.Color;

public class SudokuResponse {

    private final char c;

    private final ColorResponse colorResponse;

    public SudokuResponse(char c, Color backgroundColor, Color foregroundColor) {
        this.c = c;
        this.colorResponse = new ColorResponse(backgroundColor, foregroundColor);
    }

    public char getChar() {
        return c;
    }

    public Color getBackgroundColor() {
        return colorResponse.getBackgroundColor();
    }

    public Color getForegroundColor() {
        return colorResponse.getForegroundColor();
    }

}

