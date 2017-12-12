public class Bishop extends Piece {
	private final char symbol;
	private final char altsymbol = 'B';
	private final int pointValue = 3;
	/**
	 * constructs a new piece
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color piece
	 */
	public Bishop(int x, int y, String color) {
		super(x, y, color);
		if (!color.equals("white")) {
			symbol = (char)(0x2657);
		} else {
			symbol = (char)(0x265D);
		}
	}
	
	public int getPointValue() {
		return pointValue;
	}
	public char getAltSymbol() {
		return altsymbol;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public boolean isMoveLegal(Board board, int fromX, int fromY, int toX, int toY) {
		if (!super.isMoveLegal(board, fromX, fromY, toX, toY)) {
			return false;
		}
		
		int changeInX = Math.abs(toX - fromX);
		int changeInY = Math.abs(toY - fromY);
		
		if (changeInX != changeInY) {
			return false;
		}
		
		int distance = changeInX;
		int i = 1;
		if (toX > fromX && toY > fromY) {
			while (distance > 1) {
				if (board.getBoard()[toX - i][toY - i].isOccupied()) {
					return false;
				}
				i++;
				distance--;
			}
		} else if (toX > fromX && fromY > toY) {
			while (distance > 1) {
				if (board.getBoard()[toX - i][toY + i].isOccupied()) {
					return false;
				}
				i++;
				distance--;
			}
		} else if (fromX > toX && toY > fromY) {
			while (distance > 1) {
				if (board.getBoard()[toX + i][toY - i].isOccupied()) {
					return false;	
				}
				i++;
				distance--;
			}
		} else {
			while (distance > 1) {
				if (board.getBoard()[toX + i][toY + i].isOccupied()) {
					return false;
				}
				i++;
				distance--;
			}
		}
		return true;
	}
}