public class Board {
	//Used to keep important information about the board state.
	private Square[][] board = new Square[8][8];
	private boolean whiteToMove = true;
	public int moveNumber = 1;
	private Piece lastCapturedPiece;
	private boolean enPassant = false;
	//Used to give the player information.
	private String notationSheet = "";
	private String previousMove = "";

	/**
	 * Creates a new board with the starting position in chess.
	 */
	public Board() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new Square(i, j);
			}
		}
		
		board[0][0].placePiece(new Rook(0, 0, "white"));
		board[1][0].placePiece(new Knight(1, 0, "white"));
		board[2][0].placePiece(new Bishop(2, 0, "white"));
		board[3][0].placePiece(new Queen(3, 0, "white"));
		board[4][0].placePiece(new King(4, 0, "white"));
		board[5][0].placePiece(new Bishop(5, 0, "white"));
		board[6][0].placePiece(new Knight(6, 0, "white"));
		board[7][0].placePiece(new Rook(7, 0, "white"));
		for (int i = 0; i < 8; i++) {
			board[i][1].placePiece(new Pawn(i, 1, "white"));
		}
		board[0][7].placePiece(new Rook(0, 7, "black"));
		board[1][7].placePiece(new Knight(1, 7, "black"));
		board[2][7].placePiece(new Bishop(2, 7, "black"));
		board[3][7].placePiece(new Queen(3, 7, "black"));
		board[4][7].placePiece(new King(4, 7, "black"));
		board[5][7].placePiece(new Bishop(5, 7, "black"));
		board[6][7].placePiece(new Knight(6, 7, "black"));
		board[7][7].placePiece(new Rook(7, 7, "black"));
		for (int i = 0; i < 8; i++) {
			board[i][6].placePiece(new Pawn(i, 6, "black"));
		}
		
	}
	/**
	 * Creates a copy of a board
	 * @param board the board to create a copy of
	 */
	public Board(Square[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				this.board[i][j] = board[i][j];
			}
		}
	}
	
	/**
	 * 
	 * @return the current move number
	 */
	public int getMoveNumber() {
		return moveNumber;
	}
	/**
	 * 
	 * @return the last captured piece
	 */
	public Piece getLastCaptured() {
		return lastCapturedPiece;
	}
	/**
	 * sets the lastcapturedpiece variable
	 * @param p the new last captured piece
	 */
	public void setLastCaptured(Piece p) {
		lastCapturedPiece = p;
	}
	/**
	 * 
	 * @return the notation sheet
	 */
	public String getNotationSheet() {
		return notationSheet;
	}
	/**
	 * appends the input to the notation sheet
	 * @param move the move to append
	 */
	public void addToNotationSheet(String move) {
		notationSheet += move;
	}
	/**
	 * 
	 * @return the current boardstate 
	 */
	public Square[][] getBoard() {
		return board;
	}
	/**
	 * 
	 * @return the whiteToMove variable
	 */
	public boolean whiteToMove() {
		return whiteToMove;
	}
	/**
	 * 
	 * @return the en passant variable
	 */
	public boolean getEnPassant() {
		return enPassant;
	}
	/**
	 * Sets the enPassant variable
	 * @param ep sets the enPassant variable
	 */
	public void setEnPassant(boolean ep) {
		enPassant = ep;
	}
	/**
	 * 
	 * @return the previous move
	 */
	public String getPreviousMove() {
		return previousMove;
	}
	/**
	 * Sets the previous move to a new string
	 * @param s the new previous move
	 */
	public void setPreviousMove(String s) {
		previousMove = s;
	}
	/**
	 * Flips the whiteToMove variable and advances the move number.
	 */
	public void nextTurn() {
		if (whiteToMove) {
			whiteToMove = false;
		} else {
			moveNumber++;
			whiteToMove = true;
		}
	}
	/**
	 * Flips the whiteToMove variable
	 */
	public void changeTurn() {
		if (whiteToMove) {
			whiteToMove = false;
		} else {
			whiteToMove = true;
		}
	}
	

	/**
	 * Translates coordinate notation to algebraic notation
	 * @param fromX X coordinate from where the piece starts
	 * @param fromY Y coordinate from where the piece starts
	 * @param toX coordinate where the piece is going
	 * @param toY coordinate where the piece is going
	 * @return Algebraic notation of the move
	 */
	public String coordsToAlgebraic(int fromX, int fromY, int toX, int toY) {
		String algebraic = "";
		Piece currentPiece = board[fromX][fromY].getPiece();
		//Checks if the move is castling 
		if (Game.isCastling(new Board(board), fromX, fromY, toX, toY)) {
			if (toX == 6) {
				return "0-0";
			} else {
				return "0-0-0";
			}
		}
		//Adds the symbol for the piece
		if (!(currentPiece instanceof Pawn) && currentPiece != null) {
			algebraic += currentPiece.getAltSymbol();
		}
		//Adds the file or number from where the piece started if necessary	
		String[] legalMoves = allLegalMoves(whiteToMove());
		int legalMovesLength = 0;
		if (legalMoves != null) {
			legalMovesLength = legalMoves.length;
		}
		for (int i = 0; i < legalMovesLength; i++) {
			
			if (legalMoves[i] == null || currentPiece instanceof Pawn) {
				continue;
			}
			if (legalMoves[i].charAt(5) == currentPiece.getAltSymbol()) {
				if (Character.getNumericValue(legalMoves[i].charAt(3)) == toX && 
						Character.getNumericValue(legalMoves[i].charAt(4)) == toY) {
					if (Character.getNumericValue(legalMoves[i].charAt(0)) != fromX)  {
						algebraic += (char)(fromX + (int)('a')); 
					} else if (Character.getNumericValue(legalMoves[i].charAt(1)) != fromY) {
						algebraic += fromY + 1;
					}
				}
			}
		}
		//Adds an "x" to the notation if a piece was captured
		if (board[toX][toY].getPiece()!= null) {
			if (currentPiece instanceof Pawn) {
				algebraic += (char)(fromX + (int)('a'));
			}
			algebraic += 'x';
		}
		//Adds the destination square
		char file = (char)(toX + (int)('a'));
		int rank = toY + 1;
		algebraic += file;
		algebraic += rank;
		//Checks if the move was a promotion
		if (currentPiece instanceof Pawn) {
			if (toY == 0 || toY == 7) {
				algebraic += "";
			}
		}
		return algebraic;
	}
	/**
	 * Prints the current position in a readable format.
	 */
	public void printBoard() {
		for (int i = 7; i >= -1; i--) {
			for (int j = -1; j < board[0].length; j++) {
				//Prints out the Letters and Numbers for the coordinate system
				if (j == -1) {
					if (i != -1) {
						System.out.print(i + 1);
					}
					continue;
				} else if (i == -1) {
					if (j == 0) {
						System.out.print("   ");
					} 
					System.out.print((char)(0x3000) + "" + (char)(j + 'a'));
					continue;
				}
				//Prints out the actual board
				Piece currentPiece = board[j][i].getPiece(); 
				if (currentPiece != null) {
					System.out.print(" " + currentPiece.getSymbol() + " ");
				} else {
					System.out.print(" " + (char)(0xFE58) + " ");
				}
			}
			System.out.println();
		}
	}
	/**
	 * Determines if en passant is legal in the position
	 * @return true, if en passant is legal in the position
	 */
	public boolean epLegal(int toX, int fromY, int toY) {
		//Checks if the piece previously moved is a pawn
		if (board[toX][toY].getPiece() instanceof Pawn) {
			String color = "";
			//Checks if the pawn moved down two squares, up two squares, or something else. 
			if (toY - fromY == -2) {
				//Pawn moved down two squares, meaning it was a black pawn moving from the 7th rank to the 5th rank.
				//So we need to check for white pawns on either side
				color = "white";
			} else if (toY - fromY == 2) { 
				//Pawn moved up two squares, meaning it was a white pawn moving from the 2nd rank to the 4th rank. 
				//So we need to check for black pawns on either side.
				color = "black";
			} else {
				//Pawn didn't move 2 squares so en passant is illegal
				return false;
			}
			//Checks if an enemy pawn is located one square to the left of the destination square
			if (toX > 0) {
				if (board[toX - 1][toY].getPiece() instanceof Pawn) {
					if (board[toX - 1][toY].getPiece().getColor().equals(color)) {
						return true;
					}
				}
			}
			//Checks if an enemy pawn is located one square to the right of the destination square
			if (toX < 7) {
				if (board[toX + 1][toY].getPiece() instanceof Pawn) {
					if (board[toX + 1][toY].getPiece().getColor().equals(color)) {
						return true;
					}
				}
			}
		}
		//Since no enemy pawn was located to the left or right of the pawn, en passant is illegal.
		return false;
	}
	/**
	 * Determines if a square is being attacked.
	 * @param x x value of the square being checked
	 * @param y y value of the square being checked
	 * @param white color of the player possibly attacking the square
	 * @return true, if the specified color player can legally move a piece to the square
	 */
	public boolean isAttacked(int x, int y, boolean white) {
		Piece[] pieces = new Piece[64];
		pieces = getPieces(white);
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] == null) {
				continue;
			}
			int xValue = pieces[i].getX();
			int yValue = pieces[i].getY();
			//If any of the pieces can legally move to the square, it is attacked.
			if (pieces[i].isMoveLegal(new Board(this.board), xValue, yValue, x, y)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Tests if the previous moved placed a pawn onto the 8th rank or 1st rank
	 * @return true, if the previous moved placed a pawn onto the 8th rank or 1st rank
	 */
	public boolean promotionTest() {
		for (int i = 0; i < 8; i++) {
			if (board[i][0].getPiece() instanceof Pawn || board[i][7].getPiece() instanceof Pawn) {
				return true;
			} 
		}
		return false;
	}
	/**
	 * Determines if either player is in check.
	 * @return 0 if neither player is in check
	 * 1 if black is in check
	 * 2 if white is in check
	 * 3 if both players are in check
	 */
	public int isInCheck() {
		int checkStatus = 0;
		int blackKingX = 0;
		int blackKingY = 0;
		int whiteKingX = 0;
		int whiteKingY = 0;
		//Iterates through each square on the board checking if the piece located on the square is either king
		//Records the location of each king into the variables.
		for (int j = 0; j < board.length; j++) {
			for (int i = 0; i < board.length; i++) {
				if (board[i][j].getPiece() == null) {
					continue;
				}
				if (board[i][j].getPiece() instanceof King) {
					if (board[i][j].getPiece().getColor() == "white") {
						whiteKingX = board[i][j].getPiece().getX();
						whiteKingY = board[i][j].getPiece().getY();
					} else {
						blackKingX = board[i][j].getPiece().getX();
						blackKingY = board[i][j].getPiece().getY();
					}
				}
			}
		}
		//If the king is attacked, checkStatus is updated
		if (isAttacked(blackKingX, blackKingY, true)) {
			checkStatus++;
		}
		if (isAttacked(whiteKingX, whiteKingY, false)) {
			checkStatus += 2;
		}
		return checkStatus;
	}
	/**
	 * Determines if the specified player is in checkmate.
	 * @param white Determines the color of the player 
	 * @return true, if the specified color player is in checkmate.
	 */
	public boolean isCheckmate(boolean white) {
		//Determines if the player is in check
		if (white) {
			if (isInCheck() <= 1) {
				return false;
			} 
		} else {
			if (isInCheck() != 1 && isInCheck() != 3) {
				return false;
			}
		}
		//Determines if the player has any legal moves to escape the check.
		if (allLegalMoves(white) == null) {
			return true;
		}
		return false;
	}
	/**
	 * Determines all of the legal moves in the position.
	 * @param white Determines the color of the pieces
	 * @return A string array of all legal moves for the current board state for the color player specified.
	 * Moves are returned in the format "(fromX)(fromY) (toX)(toY)(Piece Symbol)"
	 */
	public String[] allLegalMoves(boolean white) {
		Board board = new Board(this.board);
		String[] allLegalMoves = new String[300];
		Piece[] pieces = new Piece[64];
		int legalMoveCount = 0;
		int pieceCount = 0;
		String color;
		if (white) {
			color = "white";
		} else {
			color = "black";
		}
		//Gets the location of every piece on the current board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board.getBoard()[i][j].getPiece();
				if (piece != null) {
					if (piece.getColor() == color) {
						pieces[pieceCount] = piece;
						pieceCount++;
					}
				}
			}
		}
		//Determines the legal moves for each piece and adds them to the allLegalMoves array
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] == null) {
				continue;
			}
			//Iterates through all possible destination squares for each piece
			for (int j = 0; j < 8; j++) {
				for (int k = 0; k < 8; k++) {
					int fromX = pieces[i].getX();
					int fromY = pieces[i].getY();
					//Checks if all aspects of the move is legal besides rules involving check
					if (pieces[i].isMoveLegal(board, fromX, fromY, k, j)) {
						//Is needed to update the board state properly. Board state must be updated to
						//determine if the move leaves the player in check
						boolean ep = Game.isEnPassant(board, fromX, fromY, k, j);
						
						Game.updateBoardState(board, fromX, fromY, k, j, ep);
						//Checks if the move leaves the player in check
						if (white) {
							if (isInCheck() <= 1) {
								allLegalMoves[legalMoveCount] = fromX + "" + (fromY) + " " + k + "" + (j) + pieces[i].getAltSymbol();
								legalMoveCount++;
							}
						} else {
							if (isInCheck() != 1 && isInCheck() != 3) {
								allLegalMoves[legalMoveCount] = (fromX) + "" + (fromY) + " " + (k) + "" + (j) + pieces[i].getAltSymbol();
								legalMoveCount++;
							}
						}
						//Undoes the changes to the board state
						Game.undoMove(board, fromX, fromY, k, j, ep);
					}
				}
			}
		}
		if (legalMoveCount == 0) {
			return null;
		} else {
			return allLegalMoves;
		}
	}
	/**
	 * Determines the location of all of a players pieces.
	 * @param white Determines the color of the pieces
	 * @return A String array with information detailing the location of each piece in the format "(Piece symbol)xy"
	 */
	public String[] pieceLocations(boolean white) {
		String[] locations = new String[64];
		Piece[] pieces = getPieces(white);
		int pieceCount = 0;
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] == null) {
				continue;
			}
			locations[pieceCount] = pieces[i].getAltSymbol() + "" + pieces[i].getX() + "" + pieces[i].getY();
			pieceCount++;
		}

		return locations;
	}
	/**
	 * Creates a list of all of a players pieces.
	 * @param white Determines the color of pieces returned
	 * @return An array of all of the pieces for the specified color
	 */
	public Piece[] getPieces(boolean white) {
		Piece[] pieces = new Piece[64];
		int pieceCount = 0;
		String color;
		if (white) {
			color = "white";
		} else {
			color = "black";
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j].getPiece() != null) {
					if (board[i][j].getPiece().getColor() == color) {
						pieces[pieceCount] = board[i][j].getPiece();
						pieceCount++;
					}
				}
			}
		}
		return pieces;
	}



}
