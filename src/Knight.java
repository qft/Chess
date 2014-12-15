import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class Knight extends Piece {
    private String img_file;
    private BufferedImage img;
    public Knight(Color c, Position pos) {
        super(c, pos);
        if (c.equals(Color.BLACK)) {
            img_file = "images\\BLACK_KNIGHT.png";
        }
        else {
            img_file = "images\\WHITE_KNIGHT.png";
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

        // Checks the eight possible Positions the Knight can move to.
        Position finalPos = new Position(x+2, y-1);
        Piece current;
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos); 
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x+2, y+1);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }            
        }

        finalPos = new Position(x-2, y-1);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x-2, y+1);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x-1, y+2);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x+1, y+2);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x-1, y-2);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x+1, y-2);
        if (Position.isValidPosition(finalPos)) {
            current = b.getPiece(finalPos);
            if ((current == null) ||
                    !current.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        return ans;
    }

    @Override
    public String getPieceType() {
        return "KNIGHT";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, WIDTH, HEIGHT, null);
    }

}
