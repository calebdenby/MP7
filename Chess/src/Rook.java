public class Rook extends Piece {
	private final char symbol;
	private final char altsymbol = 'R';
	private final int pointValue = 5;
	/**
	 * constructs a new piece
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color piece
	 */
	public Rook(int x, int y, String color) {
		super(x, y, color);
		if (!color.equals("white")) {
			symbol = (char)(0x2656);
		} else {
			symbol = (char)(0x265C);
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
		if (fromX != toX) {
			if (fromY != toY) {
				return false;
			} else {
				int distanceMoved = Math.abs(toX - fromX);
				int i = 1;
				if (toX > fromX) {
					while (distanceMoved > 1) {
						if (board.getBoard()[toX - i][toY].isOccupied()) {
							return false;
						}
						i++;
						distanceMoved--;
					}
				} else {
					while (distanceMoved > 1) {
						if (board.getBoard()[toX + i][toY].isOccupied()) {
							return false;
						}
						i++;
						distanceMoved--;
					}
				}
			}
		} else {
			int distanceMoved = Math.abs(toY - fromY);
			int i = 1;
			if (toY > fromY) {
				while (distanceMoved > 1) {
					if (board.getBoard()[toX][toY - i].isOccupied()) {
						return false;
					}
					i++;
					distanceMoved--;
				}
			} else {
				while (distanceMoved > 1) {
					if (board.getBoard()[toX][toY + i].isOccupied()) {
						return false;
					}
					i++;
					distanceMoved--;
				}
			}
		}
		return true;
	}
}