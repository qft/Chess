import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class King extends Piece {
    private String img_file;
    private BufferedImage img;
    public King(Color c, Position pos) {
        super(c, pos);
        if (c.equals(Color.BLACK)) {
            img_file = "images\\BLACK_KING.png";
        }
        else {
            img_file = "images\\WHITE_KING.png";
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

        // The first 8 of these check for movement one square away from the King.
        Position finalPos = new Position(x+1, y+1);
        Piece target;
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x, y+1);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x-1, y+1);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x+1, y);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x-1, y);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x+1, y-1);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x, y-1);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }

        finalPos = new Position(x-1, y-1);
        if (Position.isValidPosition(finalPos)) {
            target = b.getPiece(finalPos);
            if ((target == null) || 
                    !target.getColor().equals(this.getColor())) {
                ans.add(new Move(currentPos, finalPos));
            }
        }


        // This checks if castling can occur.
        boolean leftRookMoved = false, rightRookMoved = false, kingMoved = false;

        // Check that the King and Rooks have not been moved. 
        for (Move m : b.history) {
            Position initPos = m.getInitialPosition();
            if (this.getColor().equals(Color.BLACK)) {
                if (initPos.equals(new Position(7,0))) {
                    leftRookMoved = true;
                }
                if (initPos.equals(new Position(7,7))) {
                    rightRookMoved = true;
                }
                // Break early if king has moved.
                if (initPos.equals(new Position(7,4))) {
                    kingMoved = true;
                    break;
                }
            }
            else {
                if (initPos.equals(new Position(0,0))) {
                    leftRookMoved = true;
                }
                if (initPos.equals(new Position(7,0))) {
                    rightRookMoved = true;
                }
                // Break early if king has moved.
                if (initPos.equals(new Position(0,4))) {
                    kingMoved = true;
                    break;
                }
            }
        }

        Position leftPos = new Position(x-1,y);
        Position rightPos = new Position(x+1,y);
        boolean canCastleLeft = false, canCastleRight = false;


        // Check there are no pieces between the King and each Rook.
        while (leftPos.getX() > 0 && b.getPiece(leftPos) == null) {
            leftPos = new Position(leftPos.getX() - 1, y);
        }
        while (rightPos.getX() < 7 && b.getPiece(rightPos) == null) {
            rightPos = new Position(rightPos.getX() + 1 ,y);
        }

        if (leftPos.getX() > -1 &&
                b.getPiece(new Position(leftPos.getX(), y)) instanceof Rook) {
            canCastleLeft = true;
        }

        if (rightPos.getX() < 8 &&  
                b.getPiece(new Position(rightPos.getX(), y)) instanceof Rook) {
            canCastleRight = true;
        }

        canCastleLeft = canCastleLeft && !leftRookMoved;
        canCastleRight = canCastleRight && !rightRookMoved;

        if (!kingMoved) {
            if (canCastleLeft) {
                ans.add(new Move(currentPos, new Position(x-2,y)));
            }
            if (canCastleRight) {
                ans.add(new Move(currentPos, new Position(x+2,y)));
            }
        }

        return ans;
    }

    @Override
    public String getPieceType() {
        return "KING";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, WIDTH, HEIGHT, null);        
    }

}
