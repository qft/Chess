import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class WhitePawn extends Piece {
    private String img_file;
    private BufferedImage img;
    public WhitePawn(Color c, Position pos) {
        super(c, pos);
        if (c.equals(Color.BLACK)) {
            img_file = "images\\BLACK_PAWN.png";
        }
        else {
            img_file = "images\\WHITE_PAWN.png";
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

        // The first two of these check if the pawn can capture a piece, either
        // through normal diagonal capturing or en passant.
        Position finalPos = new Position(x-1, y+1);
        if ((Position.isValidPosition(finalPos) && (((b.getPiece(finalPos) != null) &&
                !this.getColor().equals(b.getPiece(finalPos).getColor())) ||
                (b.getPiece(new Position(x-1, y))) instanceof BlackPawn &&
                b.lastMove().getInitialPosition().equals(new Position(x-1, 6))))) {
            ans.add(new Move(currentPos, finalPos));
        }
        
        finalPos = new Position(x+1, y+1);
        if ((Position.isValidPosition(finalPos) && (((b.getPiece(finalPos) != null) &&
                !this.getColor().equals(b.getPiece(finalPos).getColor())) ||
                (b.getPiece(new Position(x+1, y))) instanceof BlackPawn &&
                b.lastMove().getInitialPosition().equals(new Position(x+1, 6))))) {
            ans.add(new Move(currentPos, finalPos));
        }

        // These check simple forward movement.
        finalPos = new Position(x, y+1);
        if (Position.isValidPosition(finalPos) && (b.getPiece(finalPos) == null)) {
            ans.add(new Move(currentPos, finalPos));
        }
          
        finalPos = new Position(x, y+2);
        if (y+2 == 3 && Position.isValidPosition(finalPos) &&
                (b.getPiece(finalPos) == null) && 
                b.getPiece(new Position(x, y+1)) == null) {
            ans.add(new Move(currentPos, finalPos));
        }


        return ans;
    }

    @Override
    public String getPieceType() {
        return "PAWN";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, WIDTH, HEIGHT, null);
    }

}
