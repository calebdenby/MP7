public class King extends Piece {
	private final char symbol;
	private final char altsymbol = 'K';
	private final int pointValue = 0;
	/**
	 * constructs a new piece
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color piece
	 */
	public King(int x, int y, String color) {
		super(x, y, color);
		if (!color.equals("white")) {
			symbol = (char)(0x2654);
		} else {
			symbol = (char)(0x265A);
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
		
		if (changeInX > 1 || changeInY > 1) {
			if (!this.hasMoved) {
				if (fromX == 4 && fromY == 0 && toX == 6 && toY == 0) {
					if (!(board.getBoard()[7][0].getPiece() instanceof Rook)) {
						return false;
					}
					if (!board.getBoard()[7][0].getPiece().hasMoved && !board.getBoard()[6][0].isOccupied() && 
							!board.getBoard()[5][0].isOccupied()) {
						if (!board.isAttacked(4, 0, false) && !board.isAttacked(5, 0, false)) {
							return true;
						}	
					}
				} else if (fromX == 4 && fromY == 7 && toX == 6 && toY == 7) {
					if (!(board.getBoard()[7][7].getPiece() instanceof Rook)) {
						return false;
					}
					if (!board.getBoard()[7][7].getPiece().hasMoved && !board.getBoard()[6][7].isOccupied() && 
							!board.getBoard()[5][7].isOccupied()) {
						if (!board.isAttacked(4, 7, true) && !board.isAttacked(5, 7, true)) {
							return true;
						}
					}
				} else if (fromX == 4 && fromY == 0 && toX == 2 && toY == 0) {
					if (!(board.getBoard()[0][0].getPiece() instanceof Rook)) {
						return false;
					}
					if (!board.getBoard()[0][0].getPiece().hasMoved && !board.getBoard()[1][0].isOccupied() && 
							!board.getBoard()[2][0].isOccupied() && !board.getBoard()[3][0].isOccupied()) {
						if (!board.isAttacked(3, 0, false) && !board.isAttacked(4, 0, false) ) {
							return true;
						}
						
					}
				} else if (fromX == 4 && fromY == 7 && toX == 2 && toY == 7) {
					if (!(board.getBoard()[0][7].getPiece() instanceof Rook)) {
						return false;
					}
					if (!board.getBoard()[0][7].getPiece().hasMoved && !board.getBoard()[1][7].isOccupied() && 
							!board.getBoard()[2][7].isOccupied() && !board.getBoard()[3][7].isOccupied()) {
						if (!board.isAttacked(3, 7, true) && !board.isAttacked(4, 7, true) ) {
							return true;
						}
						
					}
				}
			}
			return false;
		}
		return true;
	}
	
}