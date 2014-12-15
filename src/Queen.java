import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class Queen extends Piece {
    private String img_file;
    private BufferedImage img;
    public Queen(Color c, Position pos) {
        super(c, pos);
        if (c.equals(Color.BLACK)) {
            img_file = "images\\BLACK_QUEEN.png";
        }
        else {
            img_file = "images\\WHITE_QUEEN.png";
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
        final Position currentPos = this.getPosition();

        final int x = currentPos.getX();
        final int y = currentPos.getY();

        // Includes same logic as Bishop and Rook.
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

        finalPos = new Position(x+1, y);       
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX()+1, finalPos.getY());
        }
        if (Position.isValidPosition(finalPos) && 
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }

        finalPos = new Position(x-1, y);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX()-1, finalPos.getY());
        }
        if (Position.isValidPosition(finalPos) && 
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }

        finalPos = new Position(x, y+1);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX(), finalPos.getY()+1);
        }
        if (Position.isValidPosition(finalPos) && 
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }

        finalPos = new Position(x, y-1);
        while (Position.isValidPosition(finalPos) && b.getPiece(finalPos) == null) {
            ans.add(new Move(currentPos, finalPos));
            finalPos = new Position(finalPos.getX(), finalPos.getY()-1);
        }
        if (Position.isValidPosition(finalPos) && 
                !this.getColor().equals(b.getPiece(finalPos).getColor())) {
            ans.add(new Move(currentPos, finalPos));
        }
        
        return ans;
    }

    @Override
    public String getPieceType() {
        return "QUEEN";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, WIDTH, HEIGHT, null);
    }

}
