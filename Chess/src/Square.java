public class Square {
	/**
	 * x coordinate
	 */
	int x;
	/**
	 * y coordinate
	 */
	int y;
	/**
	 * piece occupying the square
	 */
	Piece piece;
	boolean isOccupied;
	
	/**
	 * Constructs a new square
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		this.piece = null;
		this.isOccupied = false;
	}
	/**
	 * constructs a new square
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param p piece occupying the square
	 */
	public Square(int x, int y, Piece p) {
		this.x = x;
		this.y = y;
		this.piece = p;
		this.isOccupied = true;
	}
	/**
	 * sets this.piece to the input
	 * @param p piece to place on the square
	 */
	public void placePiece(Piece p) {
		this.piece = p;		
		if (p != null)  {
			p.setX(this.x);
			p.setY(this.y);
			this.isOccupied = true;
		} else {
			this.isOccupied = false;
		}

	}
	/**
	 * sets this.piece to null
	 */
	public void removePiece() {
		this.isOccupied = false;
		this.piece = null;
	}
	/**
	 * gets the piece occupying the square
	 * @return the piece occupying this square
	 */
	public Piece getPiece() {
		return piece;
	}
	/**
	 * determines if the square is occupied
	 * @return isOccupied variable
	 */
	public boolean isOccupied() {
		return isOccupied;
	}
	
}
