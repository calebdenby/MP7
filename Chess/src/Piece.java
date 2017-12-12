public class Piece {
	/**
	 * X coordinate
	 */
	private int x;
	/**
	 * y coordinate
	 */
	private int y;
	/**
	 * color piece
	 */
	private String color;
	/**
	 * true if the piece has moved
	 */
	public boolean hasMoved = false;
	/**
	 * Letter symbol for the piece
	 */
	private char altsymbol = ' ';
	/**
	 * point value for the piece
	 */
	private int pointValue = 0;
	/**
	 * gets the piece's point value
	 * @return the point value
	 */
	public int getPointValue() {
		return pointValue;
	}
	/**
	 * gets the piece's alternate symbol
	 * @return the piece's alternate symbol
	 */
	public char getAltSymbol() {
		return altsymbol;
	}
	/**
	 * constructs a new piece
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @param color color piece
	 */
	public Piece(int x, int y, String color) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
	}
	/**
	 * gets the piece's symbol
	 * @return the piece's symbol
	 */
	public char getSymbol() {
		return ' ';
	}
	/**
	 * Gets the piece's x coordinate
	 * @return the piece's x coordinate
	 */
	public int getX() {
        return x;
    }
	/**
	 * sets the piece's x coordinate
	 * @param x new x coordinate
	 */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * gets the piece's y coordinate
     * @return the piece's y coordinate
     */
    public int getY() {
        return y;
    }
    /**
     * sets the piece's y coordinate
     * @param y the new y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * gets the piece's color
     * @return the piece's color
     */
    public String getColor() {
    	return color;
    }
    /**
     * sets the piece's color
     * @param color the new color
     */
    public void setColor(String color) {
    	this.color = color;
    }
    
    /**
     * determines if the move is legal
     * @param board game board
     * @param fromX starting x coordinate
     * @param fromY starting y coordinate
     * @param toX ending x coordinate
     * @param toY ending y coordinate
     * @return true, if the move is legal
     */
    public boolean isMoveLegal(Board board, int fromX, int fromY, int toX, int toY) {
    	
    	if (board.getBoard()[fromX][fromY] == null) {
    		System.out.println("There's no piece there.");
    		return false;
    	}
    	
    	if (toX == fromX && toY == fromY) {
    		return false;
    	} 
    	if (toX < 0 || toX > 7 || toY < 0 || toY > 7 || fromX < 0 || fromX > 7 || fromY < 0 || fromY > 7) {
    		System.out.println("You're out of bounds.");
    		return false;
    	}
    	if (board.getBoard()[toX][toY].getPiece() != null) {
    		if (board.getBoard()[toX][toY].getPiece().getColor() == this.getColor()) {
        		return false;
        	}
    	}
    	
    	return true;
    }
	/**
	 * sets the hasmoved variable to true.
	 */
    public void hasMoved() {
    	hasMoved = true;
    }
	
}
