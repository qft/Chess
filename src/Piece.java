import java.awt.Color;
import java.awt.Graphics;
import java.util.TreeSet;

public abstract class Piece {
    // Color of the Piece
    private Color c;
    // Position on Board
    private Position pos;
    
    // Pixel coordinates in Graphics object
    public int pos_x;
    public int pos_y;

    // Pixel width and height of Piece's image
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;
    
    public Piece(Color c, Position pos) { 
        this.c = c; 
        this.pos = pos;
        setOffset(7 - (int) (62.5 * (pos.getX()) + 2),
                    7 - (int) (62.5 * (pos.getY() + 2)));
    }

    // Each piece has a different getPossibleMoves function.
    public abstract TreeSet<Move> getPossibleMoves(Board b);

    // Create new Color that .equals(this.c) to return. Prevents aliasing.
    public Color getColor() {
        return new Color(c.getRed(), c.getGreen(), c.getBlue());
    }

    // Get the Board Position of the Piece.
    public Position getPosition() {
        return this.pos;
    }
    
    // Change Board Position pos as well as pixel coordinates pos_x and pos_y.
    public void setPosition(Position pos) {
        this.pos = pos;
        
        // updates pixel position
        this.setOffset((int) (pos.getX() * 62.5) + 2,
                (int) (pos.getY() * 62.5) + 2);
    }

    // Sets pixel coordinates pos_x and pos_y
    private void setOffset(int dx, int dy) {
        this.pos_x = dx;
        this.pos_y = dy;
    }
    
    // Prints the Piece name. Used for debugging.
    public abstract String getPieceType();
    
    // Each piece should be able to draw itself.
    public abstract void draw(Graphics g);
}
