# tetris

This is a basic Tetris clone implemented in Java as part of a programming test. 

## Execution

### Maven:
  `mvn exec:java -Dexec.mainClass="com.davis.tetris.Main"`

### Without Maven:
  `find . -name "*.java" -print | xargs javac`
  
  `java -cp src/main/java com.davis.tetris.Main`
  
## Playing

The playing board is rendered as text and then input is requested (A,D,W,S) followed by the enter key. Board is re-rendered and new input is requested. Game ends when the board is filled with pieces.

Commands are:

 * A - Move left
 * D - Move Right
 * W - Rotate Left
 * S - Rotate Right

### Tetrominos

This implementation currently supports the following Tetromino shapes: I,O,Z,L,J
