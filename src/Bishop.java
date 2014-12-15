import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class Bishop extends Piece {
    public String img_file;
    private BufferedImage img;

    public Bishop(Color c, Position pos) {
        super(c, pos);
        if (c.equals(Color.BLACK)) {
            img_file = "images\\BLACK_BISHOP.png";
        }
        else {
            img_file = "images\\WHITE_BISHOP.png";
        }
        try {
            img = ImageIO.read(new File(img_file));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public TreeSet<Move> getPossibleMoves(Board b) {
        TreeSet<Move> ans = new TreeSet<Move>();
        Position currentPos = this.getPosition();

        int x = currentPos.getX();
        int y = currentPos.getY();
        

        // Check each diagonal for empty spaces, and then for enemy Pieces for
        // capture.
        Position finalPos = new Position(x+1, y+1);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos)); 
            finalPos = new Position(finalPos.getX()+1, finalPos.getY()+1);
        }
        if (Position.isValidPosition(finalPos) &&
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
            }

        finalPos = new Position(x-1, y-1);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX()-1, finalPos.getY()-1);
        }
        if (Position.isValidPosition(finalPos) &&
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }


        finalPos = new Position(x-1, y+1);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX()-1, finalPos.getY()+1);
        }
        if (Position.isValidPosition(finalPos) && 
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }

        finalPos = new Position(x+1, y-1);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX()+1, finalPos.getY()-1);
        }
        if (Position.isValidPosition(finalPos) && 
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }
        
        return ans;
    }

    @Override
    public String getPieceType() {
        return "BISHOP";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, WIDTH, HEIGHT, null);
    }

}
