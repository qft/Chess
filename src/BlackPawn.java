import java.awt.Color;
import java.util.TreeSet;


public class BlackPawn extends WhitePawn {

    public BlackPawn(Color c, Position pos) {
        super(c, pos);
    }

    @Override
    public TreeSet<Move> getPossibleMoves(Board b) {
        TreeSet<Move> ans = new TreeSet<Move>();
        Position currentPos = this.getPosition();

        final int x = currentPos.getX();
        final int y = currentPos.getY();

        // The first two of these check if the pawn can capture a piece, either
        // through normal diagonal capturing or en passant.
        Position finalPos = new Position(x-1, y-1);
        if ((Position.isValidPosition(finalPos) && (((b.getPiece(finalPos) != null) &&
                !this.getColor().equals(b.getPiece(finalPos).getColor())) ||
                b.getPiece(new Position(x-1,y)) != null &&
                (b.getPiece(new Position(x-1, y))).getColor().equals(Color.WHITE) &&
                b.lastMove().getInitialPosition().equals(new Position(x-1, 1))))) {
            ans.add(new Move(currentPos, finalPos));
        }

        // Pawn capture in other x direction (might be en passant)
        finalPos = new Position(x+1, y-1);
        if ((Position.isValidPosition(finalPos) && (((b.getPiece(finalPos) != null) &&
                !this.getColor().equals(b.getPiece(finalPos).getColor())) ||
                b.getPiece(new Position(x+1,y)) != null &&
                (b.getPiece(new Position(x+1, y))).getColor().equals(Color.WHITE) &&
                b.lastMove().getInitialPosition().equals(new Position(x+1, 1))))) {
            ans.add(new Move(currentPos, finalPos));
        }

        // These check simple forward movement.
        finalPos = new Position(x, y-1);
        if (Position.isValidPosition(finalPos) && (b.getPiece(finalPos) == null)) {
            ans.add(new Move(currentPos, finalPos));
        }

        finalPos = new Position(x, y-2);
        if (y-2 == 4 && Position.isValidPosition(finalPos) &&
                (b.getPiece(finalPos) == null) && 
                b.getPiece(new Position(x, y-2)) == null) {
            ans.add(new Move(currentPos, finalPos));
        }

        return ans;
    }
}
