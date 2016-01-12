package com.davis.tetris;

import java.util.HashMap;
import java.util.Map;

/**
 * Tetromino definitions
 *
 * Created by kellyd on 11/28/15.
 */
public enum Tetromino {
    I(new int[][][] { { { 0,0,0,0 },
                        { 0,0,0,0 },
                        { 1,1,1,1 },
                        { 0,0,0,0 } },
                      { { 0,1,0,0 },
                        { 0,1,0,0 },
                        { 0,1,0,0 },
                        { 0,1,0,0 } },
                      { { 0,0,0,0 },
                        { 1,1,1,1 },
                        { 0,0,0,0 },
                        { 0,0,0,0 } },
                      { { 0,0,1,0 },
                        { 0,0,1,0 },
                        { 0,0,1,0 },
                        { 0,0,1,0 } }}),

    O(new int[][][] {{{1,1},
                      {1,1}}}),

    Z(new int[][][] {  {{0,0,0},
                        {1,1,0},
                        {0,1,1}},

                       {{0,1,0},
                        {1,1,0},
                        {1,0,0}},

                       {{1,1,0},
                        {0,1,1},
                        {0,0,0}},

                       {{0,0,1},
                        {0,1,1},
                        {0,1,0}}}),

    L(new int[][][] {  {{0,0,0},
                        {1,1,1},
                        {1,0,0}},

                       {{1,1,0},
                        {0,1,0},
                        {0,1,0}},

                       {{0,0,1},
                        {1,1,1},
                        {0,0,0}},

                       {{0,1,0},
                        {0,1,0},
                        {0,1,1}}}),

    J(new int[][][] {  {{0,0,0},
                        {1,1,1},
                        {0,0,1}},

                       {{0,1,0},
                        {0,1,0},
                        {1,1,0}},

                       {{1,0,0},
                        {1,1,1},
                        {0,0,0}},

                       {{0,1,1},
                        {0,1,0},
                        {0,1,0}}});

    private int[][][] coords;
    private int orientations;

    // Pre-computed values for the location of the Tetromino boundaries
    // are stored in hash maps. Boundaries are used to more quickly check collisions
    // and render the pieces
    private Map<Integer,Integer> leftColumnMap = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> rightColumnMap = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> topRowMap = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> bottomRowMap = new HashMap<Integer, Integer>();

    private Tetromino(int[][][] coords) {
        this.coords = coords;
        this.orientations = coords.length;

        for ( int orientation = 0; orientation < orientations; orientation++ ) {
            topRowMap.put(orientation, computeTopRow(orientation));
            bottomRowMap.put(orientation, computeBottomRow(orientation));
            leftColumnMap.put(orientation, computeLeftColumn(orientation));
            rightColumnMap.put(orientation, computeRightColumn(orientation));
        }
    }

    int orientations() {
        return orientations;
    }

    int[][] coords(int orientation) {
        return this.coords[orientation];
    }

    /**
     * Top boundary of the Tetromino  for the orientation
     *
     * @param orientation
     * @return the row for the top boundary
     */
    int topRow(int orientation) {
        return topRowMap.get(orientation);
    }

    private int computeTopRow(int orientation) {
        int[][] coords = this.coords[orientation];

        for ( int row = 0; row < coords.length; row++ ) {
            for ( int col = 0; col < coords[row].length; col++ ) {
                if ( coords[row][col] != 0 ) {
                    return row;
                }
            }
        }

        return coords.length - 1;
    }

    /**
     * Bottom boundary of the Tetromino for the orientation
     *
     * @param orientation
     * @return the row for the bottom boundary
     */
    int bottomRow(int orientation) {
        return bottomRowMap.get(orientation);
    }

    private int computeBottomRow(int orientation) {
        int[][] coords = this.coords[orientation];

        for ( int row = coords.length - 1; row >= 0; --row ) {
            for ( int col = 0; col < coords[row].length; col++ ) {
                if ( coords[row][col] != 0 ) {
                    return row;
                }
            }
        }

        return 0;
    }

    /**
     * Right boundary of the Tetromino for the orientation
     *
     * @param orientation
     * @return the row for the right boundary
     */
    int rightColumn(int orientation) {
        return rightColumnMap.get(orientation);
    }

    private int computeRightColumn(int orientation) {
        int[][] coords = this.coords[orientation];

        int right = -1;

        for ( int row = 0; row < coords.length; row++ ) {
            for ( int col = coords[row].length - 1; col >= 0; --col ) {
                if ( coords[row][col] != 0 ) {
                    if ( right == -1 || col > right ) {
                        right = col;
                    }
                }
            }
        }

        return right;
    }

    /**
     * Left boundary of the Tetromino for the orientation
     *
     * @param orientation
     * @return the row for the left boundary
     */
    int leftColumn(int orientation) {
        return leftColumnMap.get(orientation);
    }

    private int computeLeftColumn(int orientation) {
        int[][] coords = this.coords[orientation];

        int left = -1;

        for ( int row = 0; row < coords.length; row++ ) {
            for ( int col = 0; col < coords[row].length; col++ ) {
                if ( coords[row][col] != 0 ) {
                    if ( left == -1 || col < left ) {
                        left = col;
                    }
                }
            }
        }

        return left;
    }
}
