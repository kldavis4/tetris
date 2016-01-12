package com.davis.tetris;

import java.util.*;

/**
 * Tetris game object. Maintains global game board state, renders game screen and handles user input.
 *
 * Created by kellyd on 11/28/15.
 */
public class Game {
    private Piece activePiece = null;

    private int boardWidth;
    private int boardHeight;

    private Random random = new Random(System.currentTimeMillis());
    private Scanner sc;

    private int[][] board;

    /**
     * Initial game of tetris
     *
     * @param boardWidth - number of columns on the board
     * @param boardHeight - number of rows on the board
     */
    public Game(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.board = new int[boardHeight][boardWidth];
    }

    /**
     * Start a game of Tetris
     */
    public void start() {
        sc = new Scanner(System.in);

        Command c = Command.NIL;
        for (;;) {
            updateBoard(c);
            renderBoard();

            c = Command.NIL;
            while ( c == Command.NIL ) {
                try {
                    c = parseInput(sc.nextLine());
                } catch (NoSuchElementException e) {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Parse the user input into a known Command enum
     *
     * @param input raw user input
     * @return return a Command or the NIL command if unable to parse input
     */
    private Command parseInput(String input) {
        Command result = Command.NIL;
        if ( input != null ) {
            if ( "a".equalsIgnoreCase(input.trim()) ) {
                result = Command.MOVE_LEFT;
            } else if ( "d".equalsIgnoreCase(input.trim())) {
                result = Command.MOVE_RIGHT;
            } else if ( "w".equalsIgnoreCase(input.trim())) {
                result = Command.ROTATE_CCW;
            } else if ( "s".equalsIgnoreCase(input.trim())) {
                result = Command.ROTATE_CW;
            }
        }
        return result;
    }

    /**
     * Generates a new Tetris piece and places it randomly
     */
    private void generateNewPiece() {
        activePiece = Piece.randomPiece();

        int row = -activePiece.bottomRow(); //only bottom is visible
        int col = random.nextInt(boardWidth);

        if ( col + activePiece.rightColumn() >= boardWidth ) {
            col = boardWidth - activePiece.rightColumn() - 1;
        } else if ( col - activePiece.leftColumn() < 0 ) {
            col = -activePiece.leftColumn();
        }

        activePiece.setRow(row);
        activePiece.setColumn(col);

        if ( collideBottom() ) {
            System.out.println("Reached the top! Game Over");
            System.exit(0);
        }
    }

    /**
     * Update game state based on user input
     *
     * @param c - user input command
     */
    private void updateBoard(Command c) {
        if ( activePiece == null ) {
            generateNewPiece();
            writeActivePieceToBoard();
            return;
        }

        //Clear the current location of the active piece
        clearActivePieceFromBoard();

        switch(c) {
            case ROTATE_CCW:
                activePiece.rotateCCW();

                int col = activePiece.getColumn();

                //Adjust the piece to the right or the left if it hits the side wall
                if ( col + activePiece.rightColumn() >= boardWidth ) {
                    activePiece.setColumn(boardWidth - activePiece.rightColumn() - 1);
                } else if ( col - activePiece.leftColumn() < 0 ) {
                    activePiece.setColumn(-activePiece.leftColumn());
                }

                if ( collideBottom() ) {
                    //If we hit an existing piece on rotation - reset
                    activePiece.rotateCW();
                    activePiece.setColumn(col);
                }

                break;
            case ROTATE_CW:
                activePiece.rotateCW();

                col = activePiece.getColumn();

                //Adjust the piece to the right or the left if it hits the side wall
                if ( col + activePiece.rightColumn() >= boardWidth ) {
                    activePiece.setColumn(boardWidth - activePiece.rightColumn() - 1);
                } else if ( col - activePiece.leftColumn() < 0 ) {
                    activePiece.setColumn(-activePiece.leftColumn());
                }

                if ( collideBottom() ) {
                    //If we hit an existing piece on rotation - reset
                    activePiece.rotateCCW();
                    activePiece.setColumn(col);
                }
                break;
            case MOVE_LEFT:
                activePiece.moveLeft();

                if ( collidateLeft() ) {
                    activePiece.moveRight();
                }

                break;
            case MOVE_RIGHT:
                activePiece.moveRight();

                if ( collideRight()) {
                    activePiece.moveLeft();
                }

                break;
        }

        activePiece.moveDown();

        if ( collideBottom() ) {
            activePiece.moveUp();

            writeActivePieceToBoard();
            generateNewPiece();
        }

        writeActivePieceToBoard();
    }

    /**
     * Writes the current piece to the playing board
     */
    private void writeActivePieceToBoard() {
        for ( int row = activePiece.topRow(); row <= activePiece.bottomRow(); row++ ) {
            for ( int col = activePiece.leftColumn(); col <= activePiece.rightColumn(); col++ ) {
                int boardRow = row+activePiece.getRow();
                int boardCol = col+activePiece.getColumn();

                if ( boardRow >= 0 && boardRow < boardHeight &&
                     boardCol >= 0 && boardCol < boardWidth ) {
                    board[boardRow][boardCol] = activePiece.getValue(row, col);
                }
            }
        }
    }

    /**
     * Clears the current piece from the playing board
     */
    private void clearActivePieceFromBoard() {
        for ( int row = activePiece.topRow(); row <= activePiece.bottomRow(); row++ ) {
            for ( int col = activePiece.leftColumn(); col <= activePiece.rightColumn(); col++ ) {
                int boardRow = row+activePiece.getRow();
                int boardCol = col+activePiece.getColumn();

                if ( boardRow >= 0 && boardRow < boardHeight &&
                        boardCol >= 0 && boardCol < boardWidth ) {
                    board[boardRow][boardCol] = 0;
                }
            }
        }
    }

    /**
     * Check for collisions with the bottom wall and any existing pieces
     *
     * @return True if there was a collision
     */
    private boolean collideBottom() {
        if ( activePiece.getRow() + activePiece.bottomRow() >= boardHeight ) {
            return true;
        } else {
            //Start on the bottom
            for ( int row = activePiece.bottomRow(); row >= activePiece.topRow(); --row ) {
                for (int col = activePiece.leftColumn(); col <= activePiece.rightColumn(); col++) {
                    int boardRow = row+activePiece.getRow();
                    int boardCol = col+activePiece.getColumn();

                    if ( boardRow < 0 ||
                         boardCol < 0 ||
                         boardRow >= boardHeight ||
                         boardCol >= boardWidth ) {
                        continue;
                    }

                    if ( activePiece.getValue(row,col) == 1 &&
                            board[boardRow][boardCol] == 1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Check for collisions with the right wall and any existing pieces
     *
     * @return True if there was a collision
     */
    private boolean collideRight() {
        //Check wall
        if ( activePiece.getColumn() + activePiece.rightColumn() >= boardWidth ) {
            return true;
        } else {
            for ( int row = activePiece.topRow(); row <= activePiece.bottomRow(); row++ ) {
                //Start on the right side of the piece
                for (int col = activePiece.rightColumn(); col >= activePiece.leftColumn(); --col) {
                    int boardRow = row+activePiece.getRow();
                    int boardCol = col+activePiece.getColumn();

                    if ( boardRow < 0 ||
                            boardCol < 0 ||
                            boardRow >= boardHeight ||
                            boardCol >= boardWidth ) {
                        continue;
                    }

                    if ( activePiece.getValue(row,col) == 1 &&
                         board[boardRow][boardCol] == 1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Check for collisions with the left wall and any existing pieces
     *
     * @return True if there was a collision
     */
    private boolean collidateLeft() {
        //Check wall
        if ( activePiece.getColumn() + activePiece.leftColumn() < 0 ) {
            return true;
        } else {
            for ( int row = activePiece.topRow(); row <= activePiece.bottomRow(); row++ ) {
                //Start on the left side of the piece
                for (int col = activePiece.leftColumn(); col <= activePiece.rightColumn(); col++) {
                    int boardRow = row+activePiece.getRow();
                    int boardCol = col+activePiece.getColumn();

                    if ( boardRow < 0 ||
                            boardCol < 0 ||
                            boardRow >= boardHeight ||
                            boardCol >= boardWidth ) {
                        continue;
                    }

                    if ( activePiece.getValue(row,col) == 1 &&
                            board[boardRow][boardCol] == 1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Renders the board to System.out
     */
    private void renderBoard() {
        for ( int row = 0; row < board.length; row++ ) {
            System.out.print("*");
            for (int col = 0; col < board[row].length; col++ ) {
                if ( row < board.length ) {
                    if (board[row][col] == 1) {
                        System.out.print("*");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println("*");
        }

        //Render the bottom
        for ( int col = 0; col < board[0].length + 1; col++ ) {
            System.out.print("*");
        }
        System.out.println("*");
    }
}
