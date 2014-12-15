import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class Board {
    // The 2D array in which all Pieces are stored
    private Piece[][] board;
    // Contains a history of Moves for logic such as castling and en passant
    ArrayList<Move> history;
    // Contains all removed pieces
    private ArrayList<Piece> removedPieces;
    // Field for the image of the board
    private BufferedImage img;

    public Board() {
        // Initialize all fields
        board = new Piece[8][8];
        history = new ArrayList<Move>();
        removedPieces = new ArrayList<Piece>();
        try {
            img = ImageIO.read(new File("images\\chessboard.jpg"));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // Place pieces on board in starting positions. Hardcoded in.
        Position pos;

        for (int i = 0; i < 8; i++) {
            pos = new Position(i, 1);
            this.addPiece(new WhitePawn(Color.WHITE, pos), pos);
        }

        for (int i = 0; i < 8; i++) {
            pos = new Position(i, 6);
            this.addPiece(new BlackPawn(Color.BLACK, pos), pos);
        }

        pos = new Position(0,0);
        this.addPiece(new Rook(Color.WHITE, pos), pos);
        pos = new Position(7,7);
        this.addPiece(new Rook(Color.BLACK, pos), pos);
        pos = new Position(7,0);
        this.addPiece(new Rook(Color.WHITE, pos), pos);
        pos = new Position(0,7);
        this.addPiece(new Rook(Color.BLACK, pos), pos);

        pos = new Position(1,0);
        this.addPiece(new Knight(Color.WHITE, pos), pos);
        pos = new Position(6,7);
        this.addPiece(new Knight(Color.BLACK, pos), pos);
        pos = new Position(6,0);
        this.addPiece(new Knight(Color.WHITE, pos), pos);
        pos = new Position(1,7);
        this.addPiece(new Knight(Color.BLACK, pos), pos);

        pos = new Position(2,0);
        this.addPiece(new Bishop(Color.WHITE, pos), pos);
        pos = new Position(5,7);
        this.addPiece(new Bishop(Color.BLACK, pos), pos);
        pos = new Position(5,0);
        this.addPiece(new Bishop(Color.WHITE, pos), pos);
        pos = new Position(2,7);
        this.addPiece(new Bishop(Color.BLACK, pos), pos);

        pos = new Position(3,0);
        this.addPiece(new Queen(Color.WHITE, pos), pos);
        pos = new Position(3,7);
        this.addPiece(new Queen(Color.BLACK, pos), pos);

        pos = new Position(4,7);
        this.addPiece(new King(Color.BLACK, pos), pos);        
        pos = new Position(4,0);
        this.addPiece(new King(Color.WHITE, pos), pos);

    }


    /**
     * Used only when initializing the Board's state
     * @param piece the Piece to be added
     * @param pos the Position of the Piece to be added
     */
    public void addPiece(Piece piece, Position pos) {
        board[pos.getX()][pos.getY()] = piece;
    }

    /**
     * Check if the current piece can be moved.
     * @param piece Piece currently selected
     * @param move the possible Move which user selects
     * @return
     */
    public boolean canMove(Piece piece, Move move) {
        TreeSet<Move> moves = piece.getPossibleMoves(this);

        // Check if the Move selected in contained in Piece's possible moves.
        boolean canMove = false;
        for (Move m: moves) {

            // Check if the final Positions match. moves.contains(move) was not
            // working correctly, so this is a simple bypass.
            Position potential = m.getFinalPosition();
            Position given = move.getFinalPosition();

            Integer xp, yp, xg, yg;
            xp = potential.getX();
            yp = potential.getY();
            xg = given.getX();
            yg = given.getY();
            if (xp.equals(xg) && yp.equals(yg)) {
                canMove = true;
                break;
            }
        }

        if (!canMove) {
            return false;
        }

        // Check if moving the Piece would result in check for currentPlayer.
        Piece replace = movePiece(piece, move);
        if (inCheck(piece.getColor())) {
            undoMove(move, replace);
            System.out.println("Would be in check");
            throw new IllegalArgumentException("Would be in check!");
        }

        undoMove(move, replace);
        return true;

    }

    public Piece movePiece(Piece piece, Move move) {

        Position initPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Remove the Piece at the target Position. May be null.
        Piece toReturn = removePiece(finalPosition);
        // Move the Piece to target Position.
        this.addPiece(piece, finalPosition);
        piece.setPosition(finalPosition);

        // En passant removal of pawn
        if (toReturn == null) {
            if (piece instanceof BlackPawn && finalPosition.getY() == 2 &&
                    finalPosition.getX() != initPosition.getX()) {
                this.addPiece(null, new Position(finalPosition.getX(), 3));
            }
            else if (piece instanceof WhitePawn && finalPosition.getY() == 5 &&
                    finalPosition.getX() != initPosition.getX()) {
                this.addPiece(null, new Position(finalPosition.getX(), 4));
            }
        }

        // Remove Piece from initial Position.
        this.addPiece(null, initPosition);
        history.add(move);

        // Castling
        if (piece instanceof King && 
                Math.abs(initPosition.getX() - finalPosition.getX()) == 2) {
            int y = initPosition.getY();
            if (initPosition.getX() > finalPosition.getX()) {
                Position rookPos = new Position(0,y);
                board[3][y] = new Rook(board[0][y].getColor(), rookPos);
                board[0][y] = null;
            }
            else {
                Position rookPos = new Position(7,y);
                board[5][y] = new Rook(board[7][y].getColor(), rookPos);
                board[7][y] = null;
            }
        }

        return toReturn;

    }

    private Piece removePiece(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        // Save removed Piece before removing.
        Piece temp = board[x][y];
        board[x][y] = null;
        // Save removed Piece to removePieces to keep track in case of undoMove.
        if (temp != null) {
            removedPieces.add(temp);
        }
        // Return Piece which was returned.
        return temp;
    }

    // Get the Piece at Position pos.
    public Piece getPiece(Position pos) {
        int x, y;
        x = pos.getX();
        y = pos.getY();
        return board[x][y];
    }

    public void draw(Graphics g) {
        // Draws background first.
        g.drawImage(img, 0, 0, 500, 500, null);

        // Draws each piece in the board.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece current = board[i][j];
                if (current != null) {
                    current.setPosition(new Position(i, j));
                    current.draw(g);
                }
            }
        }
    }


    boolean inCheck(Color c) {
        TreeSet<Move> allMoves = new TreeSet<Move>();
        Position kingPosition = null;

        for (Piece[] u : board) {
            for (Piece v : u) {
                if (v != null) {
                    Color pieceColor = v.getColor();
                    // Find the location of the currentPlayer's King.
                    if (v instanceof King && pieceColor.equals(c)) {
                        kingPosition = v.getPosition();
                    }
                    // Simultaneously find possible moves from the other player.
                    else if (!pieceColor.equals(c)) {
                        for (Move m : v.getPossibleMoves(this)) {
                            allMoves.add(m);
                        }
                    }
                }
            }
        }

        // Should never trigger. Used for debugging. If triggered, logic was 
        // incorrect and allowed the game to continue past checkmate.
        if (kingPosition == null) {
            System.out.println("King not found!");
            return true;
        }

        // Check if any of the other player's Pieces can capture the King.
        for (Move m : allMoves) {
            if (m.getFinalPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;

    }

    public boolean isCheckmate(Color c) {

        if (history.size() < 1 || !this.inCheck(c)) {
            return false;
        }

        // Get all possible moves by currentPlayer.
        TreeSet<Move> playerMoves = new TreeSet<Move>();
        for (Piece[] u : board) {
            for (Piece v : u) {
                if (v != null && c.equals(v.getColor())) {
                    for (Move m : v.getPossibleMoves(this)) {
                        playerMoves.add(m);
                    }
                }
            }
        }

        // Check if there is at least one move currentPlayer can make in order 
        // not to be in check.
        for (Move m : playerMoves) {
            Piece replace = movePiece(this.getPiece(m.getInitialPosition()), m);
            if (!inCheck(c)) {
                undoMove(m, replace);
                return false;
            }
            undoMove(m, replace);

        }
        return true;
    }

    /**
     * Undoes the last move made.
     * @param m the move just made
     * @param replace the piece was removed (may be null)
     */
    public void undoMove(Move m, Piece replace) {
        this.board[m.getInitialPosition().getX()][m.getInitialPosition().getY()]
                = this.removePiece(m.getFinalPosition());
        this.board[m.getFinalPosition().getX()][m.getFinalPosition().getY()]
                = replace;
        this.history.remove(history.size()-1);
    }

    public Move lastMove() {
        return history.get(history.size()-1);
    }

}
