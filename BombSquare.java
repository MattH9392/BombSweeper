import java.util.*;

/**
 * Represents a square which might contain a bomb
 */
public class BombSquare extends GameSquare {

    private boolean thisSquareHasBomb = false;
    private boolean revealed = false;
    public static final int MINE_PROBABILITY = 10;

    /**
     * Constructor - randomly populates squares with bombs
     *
     * @param x     The x-coordinate of the square.
     * @param y     The y-coordinate of the square.
     * @param board The game board to which the square belongs.
     */
    public BombSquare(int x, int y, GameBoard board) {
        super(x, y, "images/blank.png", board);

        Random r = new Random();
        thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
    }

    /**
     * Executes if square clicked
     */
    public void clicked() {
        if (thisSquareHasBomb) { // Square contains a bomb
            setImage("images/bomb.png");
        } else {
            if (countSurroundingBombs(xLocation, yLocation) != 0) { // At least 1 surrounding square contains a bomb
                String numImage = "images/" + countSurroundingBombs(xLocation, yLocation) + ".png";
                setImage(numImage);
            } else { // No surrounding squares contain a bomb
                setImage("images/0.png");
                revealSurroundingSquares();
            }
        }
    }

    /**
     * Counts the number of bombs in the surrounding squares
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @return The number of bombs in the surrounding squares.
     */
    public int countSurroundingBombs(int x, int y) {
        int surroundingBombs = 0;
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                if (board.getSquareAt(x + i, y + j) != null) { // If square doesn't exist .getSquareAt() will return null
                    BombSquare neighbourSquare = (BombSquare) board.getSquareAt(x + i, y + j);
                    if (neighbourSquare.thisSquareHasBomb) {
                        surroundingBombs++;
                    }
                }
            }
        }
        return surroundingBombs;
    }

    /**
     * Reveals the surrounding squares of the current square
     */
    public void revealSurroundingSquares() {
        revealed = true;
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                if (i == 0 && j == 0) {
                    continue;
                }
				int nextX = xLocation + i;
				int nextY = yLocation + j;
				if (board.getSquareAt(nextX, nextY) != null) {
					BombSquare neighbourSquare = (BombSquare) board.getSquareAt(nextX, nextY);
					if (!neighbourSquare.revealed) {
						neighbourSquare.clicked(); // recursively call clicked() to process neighbouring squares
					}
				}
            }
        }
    }
}
