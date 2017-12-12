public class Queen extends Piece {
	private final char symbol;
	private final char altsymbol = 'Q';
	private final int pointValue = 9;
	/**
	 * constructs a new piece
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color piece
	 */
	public Queen(int x, int y, String color) {
		super(x, y, color);
		if (!color.equals("white")) {
			symbol = (char)(0x2655);
		} else {
			symbol = (char)(0x265B);
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
		
		boolean isValidRookMove = true;
		boolean isValidBishopMove = true;
		
		//Checks if the move is a valid rook move
		if (fromX != toX) {
			if (fromY != toY) {
				isValidRookMove = false;
			} else if (isValidRookMove) {
				int distanceMoved = Math.abs(toX - fromX);
				int i = 1;
				if (toX > fromX) {
					while (distanceMoved > 1) {
						if (board.getBoard()[toX - i][toY].isOccupied()) {
							isValidRookMove = false;
						}
						i++;
						distanceMoved--;
					}
				} else {
					while (distanceMoved > 1) {
						if (board.getBoard()[toX + i][toY].isOccupied()) {
							isValidRookMove = false;
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
						isValidRookMove = false;
					}
					i++;
					distanceMoved--;
				}
			} else {
				while (distanceMoved > 1) {
					if (board.getBoard()[toX][toY + i].isOccupied()) {
						isValidRookMove = false;
					}
					i++;
					distanceMoved--;
				}
			}
		}
		
		//Checks if the move is a valid bishop move.
		int changeInX = Math.abs(toX - fromX);
		int changeInY = Math.abs(toY - fromY);
		
		if (changeInX != changeInY) {
			isValidBishopMove = false;
		}
		
		int distance = changeInX;
		int i = 1;
		if (isValidBishopMove) {
			if (toX > fromX && toY > fromY) {
				while (distance > 1) {
					if (board.getBoard()[toX - i][toY - i].isOccupied()) {
						isValidBishopMove = false;
					}
					i++;
					distance--;
				}
			} else if (toX > fromX && fromY > toY) {
				while (distance > 1) {
					if (board.getBoard()[toX - i][toY + i].isOccupied()) {
						isValidBishopMove = false;
					}
					i++;
					distance--;
				}
			} else if (fromX > toX && toY > fromY) {
				while (distance > 1) {
					if (board.getBoard()[toX + i][toY - i].isOccupied()) {
						isValidBishopMove = false;	
					}
					i++;
					distance--;
				}
			} else {
				while (distance > 1) {
					if (board.getBoard()[toX + i][toY + i].isOccupied()) {
						isValidBishopMove = false;
					}
					i++;
					distance--;
				}
			}
		}
		
		
		if (isValidRookMove || isValidBishopMove) {
			return true;
		} else {
			return false;
		}
	}
	
}