import java.util.Scanner;
public class Game {
	
	public static void main(String[] args) {
		Board board = new Board();
		Scanner reader = new Scanner(System.in);
		//Checks how many players are in the game.
		String gameMode = "";
		while (!gameMode.equals("two") && !gameMode.equals("computer")) {
			System.out.println("Would you like to play with two players or against the computer? "
					+ "\n(Enter \"two\" for two players or \"computer\" to play against the computer)");
			gameMode = reader.nextLine();
			if (gameMode == null) {
				continue;
			} else {
				gameMode = gameMode.toLowerCase();
			}
		}
		boolean twoPlayer = false;
		boolean humanWhite = true;
		String whiteorblack = "";
		//Sets the booleans depending on the user's decisions
		if (gameMode.equals("two")) {
			twoPlayer = true;
		} else {
			//If only one person is playing, checks to determine what color the user wants.
			while (!whiteorblack.equals("white") && !whiteorblack.equals("black")) {
				System.out.println("Would you like white or black?");
				whiteorblack = reader.nextLine();
				if (whiteorblack == null) {
					continue;
				}
			}
			if (whiteorblack.equals("black")) {
				humanWhite = false;
			}
		}
		
		//Loop repeats for each move until the game ends or user types 'stop'
		boolean finished = false;
		while (!finished) {
			board.printBoard();
			System.out.println("Previous move: " + board.getPreviousMove());
			String[] moves = board.allLegalMoves(board.whiteToMove());
			// If there are no legal moves, then the player must be in stalemate.
			if (moves == null) {
				System.out.println("Stalemate!");
				finished = true;
				break;
			}
			String move = "";
			int fromX;
			int fromY;
			int toX;
			int toY;
			//If it is a user's turn, their move is typed in and formatted to match the format:
			//(fromX)(fromY) (toX)(toY)
			if (twoPlayer || board.whiteToMove() == humanWhite) {
				if (board.whiteToMove()) {
					System.out.print("White's turn, ");
				} else {
					System.out.print("Black's turn, ");
				}
				System.out.println("enter your move: ");
				move = reader.nextLine();
				//Ensures the input isn't 'stop' and the input is the correct length, then formats it.
				if (move.equals("stop")) {
					finished = true;
					reader.close();
					break;
				} else if (move.length() != 5) {
					System.out.println("Invalid Input");
					continue;
				} else {
					fromX = (int)(move.charAt(0)) - (int)('a');
					fromY = Character.getNumericValue(move.charAt(1)) - 1;
					toX = (int)(move.charAt(3)) - (int)('a');
					toY = Character.getNumericValue(move.charAt(4)) - 1;
					move = fromX + "" + fromY + " " + toX + "" + toY;
				}
			} else {
				//If it is the computers move, the input section is skipped.
				move = engine(board, board.whiteToMove()).substring(0, 5);
				fromX = Character.getNumericValue(move.charAt(0));
				fromY = Character.getNumericValue(move.charAt(1));
				toX =  Character.getNumericValue(move.charAt(3));
				toY = Character.getNumericValue(move.charAt(4));
			}

			//move() returns false if the move is illegal
			if (!move(board, fromX, fromY, toX, toY)) {
				System.out.println("Illegal move");
			} else if (board.promotionTest()){
				//Checks if a pawn moved to the 8th rank, then prompts the user to enter the piece they wish to promote to.
				String promotionPiece = "";
				boolean successful = false;
				while (!successful) {
					System.out.println("Which piece do you want to promote to?");
					promotionPiece = reader.nextLine();
					successful = promotion(board, fromX, fromY, toX, toY, promotionPiece);
				}
			}
			
			//Checks for checkmate, which ends the game, and check, which triggers the system to print 'Check!'.
			if (board.isInCheck() != 0) {
				if (board.isCheckmate(board.whiteToMove())) {
					board.printBoard();
					System.out.println("Checkmate!");
					finished = true;
					break;
				} else {
					System.out.println("Check!");
				}
			}
			//If the game runs too long, it is stopped.
			if (board.moveNumber > 300) {
				break;
			}
		}
		reader.close();
		System.out.println(board.getNotationSheet());
		System.out.println("Thanks for playing!");
		
	}

/**
 * Checks the inputed move for legality, and if legal, commits it to the board.
 * @param board game  board
 * @param fromX starting X coordinate
 * @param fromY starting Y coordinate
 * @param toX ending X coordinate
 * @param toY ending Y coordinate
 * @return true, if the move is legal
 */
	public static boolean move(Board board, int fromX, int fromY, int toX, int toY) {
		//Makes sure the move is in bounds.
		if (fromX < 0 || fromX > 7 || fromY < 0 || fromY > 7 
				|| toX < 0 || toX > 7 || toY < 0 || toY > 7) {
			return false;
		}
		Piece piece = board.getBoard()[fromX][fromY].getPiece();
		//Checks the legality of the input
		if (piece == null) {
			System.out.println("There is no piece there.");
			return false;
		}
		if (!piece.isMoveLegal(board, fromX, fromY, toX, toY)) {
			return false;
		}
		if (board.whiteToMove()) {
    		if (board.getBoard()[fromX][fromY].getPiece().getColor() == "black") {
    			System.out.println("It's not your turn.");
    			return false;
    		}
    	} else {
    		if (board.getBoard()[fromX][fromY].getPiece().getColor() == "white") {
    			System.out.println("It's not your turn.");
    			return false;
    		}
    	}
		
		boolean ep = isEnPassant(board, fromX, fromY, toX, toY);
		//Changes the board state depending on the move.
		recordMove(board, fromX, fromY, toX, toY);
		board.setPreviousMove(board.coordsToAlgebraic(fromX, fromY, toX, toY));
		updateBoardState(board, fromX, fromY, toX, toY, ep);
		
		//If the board state leaves the player in check, the move is illegal, and therefore undone
		if (board.whiteToMove()) {
			if (board.isInCheck() > 1) {
				undoMove(board, fromX, fromY, toX, toY, ep);
				System.out.println("You're still in check");
				return false;
			}
		} else {
			if (board.isInCheck() == 1 || board.isInCheck() == 3) {
				undoMove(board, fromX, fromY, toX, toY, ep);
				System.out.println("You're still in check");
				return false;
			}
		}
		
		//Records the move and other information about the board state.
		piece.hasMoved();
		board.setEnPassant(board.epLegal(toX, fromY, toY));
		if (!board.isCheckmate(board.whiteToMove())) {
			board.nextTurn();
		}
		return true;
	}
	
	/**
	 * Records the inputed move onto the running notation sheet.
	 * @param board game board
	 * @param fromX starting X coordinate
	 * @param fromY starting Y coordinate
	 * @param toX ending X coordinate
	 * @param toY ending Y coordinate
	 */
	public static void recordMove(Board board, int fromX, int fromY, int toX, int toY) {
		String notation = board.coordsToAlgebraic(fromX, fromY, toX, toY);
		if (board.whiteToMove()) {
			board.addToNotationSheet(" " + board.moveNumber + ". " + notation);
		} else {
			board.addToNotationSheet(" " + notation);
		}
	}
	/**
	 * Returns the "point value" of each piece the character represents
	 * @param c the symbol representing a piece
	 * @return the point value of the piece
	 */
	public static int getSymbolValue(char c) {
		if (c == 'K') {
			return 0;
		} else if (c == 'Q') {
			return 9;
		} else if (c == 'B') {
			return 3;
		} else if (c == 'N') {
			return 3;
		} else if (c == 'R') {
			return 5;
		} else if (c == 'p') {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Replaces a pawn on the 8th rank with the selected piece
	 * @param scr
	 * @param board
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 */
	public static boolean promotion(Board board, int fromX, int fromY, int toX, int toY, String promotionPiece) {
		boolean finished = false;
		while (!finished) {
			String color = board.getBoard()[toX][toY].getPiece().getColor();
			if (promotionPiece.toLowerCase().equals("queen") || promotionPiece.toLowerCase().equals("q")) {
				board.getBoard()[toX][toY] = new Square(toX, toY, new Queen(toX, toY, color));
				finished = true;
			} else if (promotionPiece.toLowerCase().equals("rook") || promotionPiece.toLowerCase().equals("r")) {
				board.getBoard()[toX][toY] = new Square(toX, toY, new Rook(toX, toY, color));
				finished = true;
			} else if (promotionPiece.toLowerCase().equals("bishop") || promotionPiece.toLowerCase().equals("b")) {
				board.getBoard()[toX][toY] = new Square(toX, toY, new Bishop(toX, toY, color));
				finished = true;
			} else if (promotionPiece.toLowerCase().equals("knight") || promotionPiece.toLowerCase().equals("n")) {
				board.getBoard()[toX][toY] = new Square(toX, toY, new Knight(toX, toY, color));
				finished = true;
			} else {
				System.out.println("Invalid input.");
			}
		}
		return finished;
	}
	
	/**
	 * Undoes whatever the previous move changed about the board state
	 * @param board game board
	 * @param fromX starting X coordinate
	 * @param fromY starting Y coordinate 
	 * @param toX ending X coordinate
	 * @param toY ending Y coordinate
	 * @param enPassant whether or not the previous move took a pawn en passant.
	 */
	public static void undoMove(Board board, int fromX, int fromY, int toX, int toY, boolean enPassant) {
		Piece piece = board.getBoard()[toX][toY].getPiece();
		//Checks if the previous move was castling, then replaces the rook as well as the king
		if (piece instanceof King) {
			if (fromX == 4 && fromY == 0 && toX == 6) {
				board.getBoard()[7][0].placePiece(board.getBoard()[5][0].getPiece());
				board.getBoard()[5][0].removePiece();
				board.setLastCaptured(null);
			} else if (fromX == 4 && fromY == 0 && toX == 2) {
				board.getBoard()[0][0].placePiece(board.getBoard()[3][0].getPiece());
				board.getBoard()[3][0].removePiece();
				board.setLastCaptured(null);
			} else if (fromX == 4 && fromY == 7 && toX == 6) {
				board.getBoard()[7][7].placePiece(board.getBoard()[5][7].getPiece());
				board.getBoard()[5][7].removePiece();
				board.setLastCaptured(null);
			} else if (fromX == 4 && fromY == 7 && toX == 2) {
				board.getBoard()[0][7].placePiece(board.getBoard()[3][7].getPiece());
				board.getBoard()[3][7].removePiece();
				board.setLastCaptured(null);
			}
		} else if (enPassant) {
			if (piece.getColor() == "white") {
				board.getBoard()[toX][toY - 1].placePiece(board.getLastCaptured());
			} else {
				board.getBoard()[toX][toY + 1].placePiece(board.getLastCaptured());
			}
			board.getBoard()[fromX][fromY].placePiece(piece);
			board.getBoard()[toX][toY].placePiece(null);
			return;
		}
		board.getBoard()[toX][toY].placePiece(board.getLastCaptured());
		board.getBoard()[fromX][fromY].placePiece(piece);
	}
	
	/**
	 * Determines if the inputed move is castling
	 * @param board game board
	 * @param fromX starting X coordinate
	 * @param fromY starting Y coordinate 
	 * @param toX ending X coordinate
	 * @param toY ending Y coordinate
	 * @return true, if inputed move is castling
	 */
	public static boolean isCastling(Board board, int fromX, int fromY, int toX, int toY) {
		Piece piece = board.getBoard()[fromX][fromY].getPiece();
		if (piece instanceof King) {
			if (piece.getColor() == "white") {
				if (fromX == 4 && fromY == 0 && (toX == 6 || toX == 2)) {
					return true;
				}
			} else {
				if (fromX == 4 && fromY == 7 && (toX == 6 || toX == 2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Determines if the inputed move is en passant.
	 * @param board game board
	 * @param fromX starting X coordinate
	 * @param fromY starting Y coordinate 
	 * @param toX ending X coordinate
	 * @param toY ending Y coordinate
	 * @return true, if inputed move captures a pawn en passant.
	 */
	public static boolean isEnPassant(Board board, int fromX, int fromY, int toX, int toY) {
		if (toY != 2 && toY != 5) {
			return false;
		}
		if (board.getBoard()[fromX][fromY].getPiece() instanceof Pawn) {
			if (board.getBoard()[toX][toY].getPiece() == null) {
				if (board.getBoard()[toX][toY - 1].getPiece() instanceof Pawn) {
					if (fromX != toX) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Updates the game board by moving the pieces to the selected squares.
	 * @param board game board
	 * @param fromX starting X coordinate
	 * @param fromY starting Y coordinate 
	 * @param toX ending X coordinate
	 * @param toY ending Y coordinate
	 * @param ep true, if inputed move is en passant
	 */
	public static void updateBoardState(Board board, int fromX, int fromY, int toX, int toY, boolean ep) {
		Piece piece = board.getBoard()[fromX][fromY].getPiece();
		if (ep) {
			//En passant required special instructions to remove the pawn.
			board.setLastCaptured(board.getBoard()[toX][toY - 1].getPiece());
			board.getBoard()[toX][toY - 1].removePiece();
			board.getBoard()[toX][toY].placePiece(piece);
			board.getBoard()[fromX][fromY].removePiece();
		} else if (isCastling(board, fromX, fromY, toX, toY)) {
			//Castling requires special instructions to move the rook.
			board.getBoard()[fromX][fromY].removePiece();
			board.getBoard()[toX][toY].placePiece(piece);
			if (toX == 2 && toY == 7) {
				board.getBoard()[3][7].placePiece(board.getBoard()[0][7].getPiece());
				board.getBoard()[0][7].removePiece();
			} else if (toX == 6 && toY == 7) {
				board.getBoard()[5][7].placePiece(board.getBoard()[7][7].getPiece());
				board.getBoard()[7][7].removePiece();
			} else if (toX == 2 && toY == 0) {
				board.getBoard()[3][0].placePiece(board.getBoard()[0][0].getPiece());
				board.getBoard()[0][0].removePiece();
			} else if (toX == 6 && toY == 0) {
				board.getBoard()[5][0].placePiece(board.getBoard()[7][0].getPiece());
				board.getBoard()[7][0].removePiece();
			}
		} else {
			//The standard way to update the board for all other pieces.
			board.setLastCaptured(board.getBoard()[toX][toY].getPiece());
			board.getBoard()[fromX][fromY].removePiece();
			board.getBoard()[toX][toY].placePiece(piece);
		}
	}
	
	/**
	 * Returns the move that the algorithm determines to be best for any game board.
	 * @param board game board
	 * @param white true, if it's white's turn. false if it's black's turn.
	 * @return The move that the algorithm determines is best.
	 */
	public static String engine(Board board, boolean white) {
		//Different categories of moves to sort the list of legal moves into.
		String[] legalMoves = board.allLegalMoves(white);
		int legalMovesCount = 0;
		String[] checkmates = new String[150];
		int chkmtCount = 0;
		String[] isNotStalemate = new String[150];
		int isNotStaleCount = 0;
		//Iterates through each legal move, and then determines which category to place it into.
		for (int i = 0; i < legalMoves.length; i++) {
			if (legalMoves[i] == null) {
				continue;
			}
			legalMovesCount++;
			char currentPiece = legalMoves[i].charAt(5);
			int fromX = Character.getNumericValue(legalMoves[i].charAt(0));
			int fromY = Character.getNumericValue(legalMoves[i].charAt(1));
			int toX = Character.getNumericValue(legalMoves[i].charAt(3));
			int toY = Character.getNumericValue(legalMoves[i].charAt(4));
			boolean ep = isEnPassant(board, fromX, fromY, toX, toY);
			board.setLastCaptured(board.getBoard()[toX][toY].getPiece());
			//Updates the board state so the algorithm can determine key features about the move.
			updateBoardState(board, fromX, fromY, toX, toY, ep);
			if (board.isCheckmate(!white)) {
				//If the move placed the opponent into checkmate, the move is added to the checkmates category.
				checkmates[chkmtCount] = legalMoves[i];
				chkmtCount++;
			} else if (board.allLegalMoves(!white) == null) {
				//If the move results in stalemate, it is not added to any category.
				continue;
			} else {
				//If the move wasn't stalemate or checkmate, more tests are run.
				String[] locations = board.pieceLocations(white);
				int pointDifference = 0;
				int worstLossValue = 0;
				if (board.getLastCaptured() != null) {
					pointDifference = board.getLastCaptured().getPointValue();
				}
				//Checks which pieces the opponent can capture in the updated board state.
				for (int j = 0; j < locations.length; j++) {
					if (locations[j] == null) {
						continue;
					}
					int xLocation = Character.getNumericValue(locations[j].charAt(1));
					int yLocation = Character.getNumericValue(locations[j].charAt(2));
					if (board.isAttacked(xLocation, yLocation, !white)) {
						if (getSymbolValue(locations[j].charAt(0)) > worstLossValue) {
							//The most points the opponent can capture is determined
							worstLossValue = getSymbolValue(locations[j].charAt(0));
						}
					}
				}
				//The true point difference is determined by subtracting the point value of the captured piece by
				//the point value of the best piece the opponent can capture in response.
				pointDifference -= worstLossValue;
				//The move along with the point difference is recorded.
				isNotStalemate[isNotStaleCount] = legalMoves[i] + pointDifference + currentPiece;
				isNotStaleCount++;
			}
			//The board is reset and ready to test the next move.
			undoMove(board, fromX, fromY, toX, toY, ep);
		}
		
		String move = "";
		//If checkmate is legal, it is the move.
		if (checkmates[0] != null) {
			move = checkmates[0];
		} 
		//If checkmate is illegal, a move is selected from the category that is not stalemate
		//The move that is selected has the best point difference determined earlier.
		if (isNotStalemate[0] != null && move.equals("")) {
			int bestPointDifference = -10;
			String[] equivalentMoves = new String[150];
			int equalMovesCount = 0;
			//The first for loop determines the best point difference in the array of moves
			for (int i = 0; i < isNotStalemate.length; i++) {
				if (isNotStalemate[i] == null) {
					continue;
				}
				if (Character.isDigit(isNotStalemate[i].charAt(6))) {
					if (Character.getNumericValue(isNotStalemate[i].charAt(6)) > bestPointDifference) {
						bestPointDifference = Character.getNumericValue(isNotStalemate[i].charAt(6));
					}
				} else {
					if (-Character.getNumericValue(isNotStalemate[i].charAt(7)) > bestPointDifference) {
						bestPointDifference = -Character.getNumericValue(isNotStalemate[i].charAt(7));
					}
				}
			}
			//The second for loop searches for moves that equal the best point difference and adds them to the equal moves array
			for (int i = 0; i < isNotStalemate.length; i++) {
				if (isNotStalemate[i] == null) {
					continue;
				}
				if (Character.isDigit(isNotStalemate[i].charAt(6))) {
					if (Character.getNumericValue(isNotStalemate[i].charAt(6)) == bestPointDifference) {
						equivalentMoves[equalMovesCount] = isNotStalemate[i];
						equalMovesCount++;
					}
				} else {
					if (-Character.getNumericValue(isNotStalemate[i].charAt(7)) == bestPointDifference) {
						equivalentMoves[equalMovesCount] = isNotStalemate[i];
						equalMovesCount++;
					}
				}
				
 			}
			//A move is selected at random from the equivalent best moves.
			//It is then evaluated, if it passes the test, the move is returned.
			do {
				move = equivalentMoves[(int)(Math.random() * equalMovesCount)];
			} while (!moveEvaluator(move, board, white));
			
		} else if (move.equals("")){
			//If there are no legal moves that are not stalemate, any legal move is returned as they are all equivalent.
			return legalMoves[(int)(Math.random() * legalMovesCount)];
		}
		return move;
	}
	
	/**
	 * Decreases the odds against moves that don't meet certain criteria are played.
	 * @param move The move to evaluate
	 * @param board game board
	 * @param white color to move
	 * @return true, if the move passes the test
	 */
	public static boolean moveEvaluator(String move, Board board, boolean white) {
		//Determines the opponent's color
		String opponentColor;
		if (white) {
			opponentColor = "black";
		} else {
			opponentColor = "white";
		}
		//Determines the opponent's king's Y location
		int oppKingY = 0;
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (board.getBoard()[i][j].getPiece() == null) {
					continue;
				}
				if (board.getBoard()[i][j].getPiece() instanceof King && 
						board.getBoard()[i][j].getPiece().getColor() == opponentColor) {
					oppKingY = board.getBoard()[i][j].getPiece().getY();
				}
			}
		}
		int fromY = Character.getNumericValue(move.charAt(1));
		int toY = Character.getNumericValue(move.charAt(4));
		Piece[] oppPieces = board.getPieces(!white);
		int oppPieceCount = 0;
		for (int i = 0; i < oppPieces.length; i++) {
			if (oppPieces[i] == null) {
				continue;
			}
			oppPieceCount++;
		}
		//Makes it less likely that a king move will be played
		if (oppPieceCount > 7 && move.charAt(7) == 'K') {
			if (Math.random() > 0.002) {
				return false;
			}
		}
		//Makes it less likely that a move that doesn't advance the piece gets played
		if (Math.abs(oppKingY - fromY) <= Math.abs(oppKingY - toY)) {
			if (Math.random() > 0.005) {
				return false;
			}
		}
		return true;
	}

}
