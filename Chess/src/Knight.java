public class Knight extends Piece {
	private final char symbol;
	private final char altsymbol = 'N';
	private final int pointValue = 3;
	/**
	 * constructs a new piece
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color piece
	 */
	public Knight(int x, int y, String color) {
		super(x, y, color);
		if (!color.equals("white")) {
			symbol = (char)(0x2658);
		} else {
			symbol = (char)(0x265E);
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
		
		if (changeInX == 2) {
			if (changeInY == 1) {
				return true;
			} else {
				return false;
			}
		} else if (changeInX == 1) {
			if (changeInY == 2) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
		
	}
}