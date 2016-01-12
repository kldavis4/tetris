package com.davis.tetris;

import java.util.Random;

/**
 * Represents a Tetris game piece
 *
 * The state of a piece consists of a Tetromino, an orientation and row / col coordinates
 *
 * Created by kellyd on 11/28/15.
 */
public class Piece {
    private int orientation;
    private Tetromino tetromino;
    private int row;
    private int column;

    private static Random random = new Random(System.currentTimeMillis());

    /**
     * Create a new game piece with the specified Tetromino and orientation
     *
     * @param tetromino
     * @param orientation
     */
    public Piece(Tetromino tetromino, int orientation) {
        this.tetromino = tetromino;
        this.orientation = orientation;
    }

    /**
     * Generate a random Tetris game piece
     *
     * @return Piece random piece
     */
    public static Piece randomPiece() {
        Tetromino[] selections = Tetromino.values();
        Tetromino selected = selections[random.nextInt(selections.length)];

        return new Piece(selected, random.nextInt(selected.orientations()));
    }

    /**
     * @return upper most row for the Tetromino
     */
    public int topRow() {
        return tetromino.topRow(orientation);
    }

    /**
     * @return bottom most row for the Tetromino
     */
    public int bottomRow() {
        return tetromino.bottomRow(orientation);
    }

    /**
     * @return left most column for the Tetromino
     */
    public int leftColumn() {
        return tetromino.leftColumn(orientation);
    }

    /**
     * @return right most column for the Tetromino
     */
    public int rightColumn() {
        return tetromino.rightColumn(orientation);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void moveRight() {
        this.column++;
    }

    public void moveLeft() {
        --this.column;
    }

    public void moveDown() {
        this.row++;
    }

    public void moveUp() {
        --this.row;
    }

    public void rotateCW() {
        this.orientation++;
        if ( this.orientation >= tetromino.orientations() ) {
            this.orientation = 0;
        }
    }

    public void rotateCCW() {
        --this.orientation;
        if ( this.orientation < 0 ) {
            this.orientation = tetromino.orientations() - 1;
        }
    }

    /**
     * Value of a cell in the Tetromino 2d array
     *
     * @param row the row
     * @param col the column
     * @return 1 if the tetromino occupies the cell, 0 otherwise
     */
    public int getValue(int row, int col) {
        return tetromino.coords(orientation)[row][col];
    }

    public String toString() {
        return tetromino.name() + " orientation:" + orientation + " row:" + row + " col:" + column;
    }
}
