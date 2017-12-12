public class Pawn extends Piece {
	private final char symbol;
	private final char altsymbol = 'p';
	private final int pointValue = 1;
	/**
	 * Constructs a new pawn
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color of the pawn
	 */
	public Pawn(int x, int y, String color) {
		super(x, y, color);
		if (!color.equals("white")) {
			symbol = (char)(0x2659);
		} else {
			symbol = (char)(0x265F);
		}
		if (y != 6 && y != 1) {
			super.hasMoved = true;
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
		
		if (this.getColor() == "white") {
			if (toX == fromX && toY - fromY == 1) {
				if (!board.getBoard()[toX][toY].isOccupied())
					return true;
			} else if (toX == fromX && toY - fromY == 2) {
				if (!this.hasMoved && !board.getBoard()[toX][toY].isOccupied() && !board.getBoard()[toX][toY - 1].isOccupied()) {
					return true;
				}
			} else if (Math.abs(toX - fromX) == 1 && toY - fromY == 1) {
				if (board.getBoard()[toX][toY].isOccupied()) {
					if (board.getBoard()[toX][toY].getPiece() != null) {
						if (board.getBoard()[toX][toY].getPiece().getColor() == "black") {
							return true;
						}
					}
				} if (board.getEnPassant()){
					if (toY == Character.getNumericValue(board.getPreviousMove().charAt(1)) && 
							toX == board.getPreviousMove().charAt(0) - (int)('a')) {
						return true;
					}
				}
			} 	
		} else if (this.getColor() == "black") {
			if (toX == fromX && toY - fromY == -1) {
				if (!board.getBoard()[toX][toY].isOccupied())
					return true;
			} else if (toX == fromX && toY - fromY == -2) {
				if (!this.hasMoved && !board.getBoard()[toX][toY].isOccupied() && !board.getBoard()[toX][toY + 1].isOccupied()) {
					return true;
				}
			} else if (Math.abs(toX - fromX) == 1 && toY - fromY == -1) {
				if (board.getBoard()[toX][toY].isOccupied()) {
					if (board.getBoard()[toX][toY].getPiece() != null) {
						if (board.getBoard()[toX][toY].getPiece().getColor() == "white") {
							return true;
						}
					}
				} if (board.getEnPassant()){
					if (toY == Character.getNumericValue(board.getPreviousMove().charAt(1)) && 
							toX == board.getPreviousMove().charAt(0) - (int)('a')) {
						return true;
					}
				}
			} 
		}
		return false;
	}
}