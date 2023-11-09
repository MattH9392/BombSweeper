import java.util.*;

public class BombSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 10;

	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}

	public void clicked()
	{
		if(thisSquareHasBomb) // Square contains a bomb
		{
			System.out.println("bomb");
			setImage("images/bomb.png");
		} else {
			if(countBombs(xLocation, yLocation) != 0) /* At least 1 bomb in adjacent squares */ { 
				System.out.println("bomb adjacent");
				// Check for bombs in surrounding squares
				String numImage = "images/" + countBombs(xLocation, yLocation) + ".png";
				board.getSquareAt(xLocation, yLocation).setImage(numImage);
			} else /* Empty */{
				System.out.println("empty");
				board.getSquareAt(xLocation, yLocation).setImage("images/0.png");
				revealSurroundingSquares();
			}
		}
	}


	public int countBombs(int x, int y) {
		int surroundingBombs = 0;
		for(int j = -1; j <= 1; j++) {
			for(int i = -1; i <= 1; i++) {
				try{
					BombSquare neighbourSquare = (BombSquare)board.getSquareAt(x + i, y + j);
					if(neighbourSquare.thisSquareHasBomb)
						surroundingBombs++;
				} catch(Exception e) {}
			}
		}
		return surroundingBombs;
	}

	public void revealSurroundingSquares() {
		boolean[][] surrounding = new boolean[3][3]; // true if surrounding bombs == 0
		for(int j = -1; j <= 1; j++) {
			for(int i = -1; i <= 1; i++) {
				if(i == 0 && j == 0)
					continue;
				try {
					int newX = xLocation + i;
					int newY = yLocation + j;
					int bombCount = countBombs(newX, newY);
					String numImage = "images/" + bombCount + ".png";
					board.getSquareAt(newX, newY).setImage(numImage);
					if(bombCount == 0)
						surrounding[i+1][j+1] = true;
				} catch(Exception e) {};
			}
		}


		for(int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				if (surrounding[i][j]) {
					int newX = xLocation - 1 + i;
					int newY = yLocation - 1 + j;

					try {
						BombSquare tempBoard = (BombSquare) board.getSquareAt(newX, newY);
						tempBoard.revealSurroundingSquares();
					} catch(Exception e) {}
				}
			}
		}
	}
	
}
