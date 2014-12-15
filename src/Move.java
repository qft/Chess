
public class Move implements Comparable<Move> {
    
    // Stores starting and ending Positions 
    private final Position initPos, finalPos;

    public Move(Position initPos, Position finalPos) {
        this.initPos = initPos;
        this.finalPos = finalPos;
    }

    // Getters
    public Position getInitialPosition() { return this.initPos; }
    public Position getFinalPosition()   { return this.finalPos;}

    // To be used in compareTo
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        Position otherInitPos = ((Move) o).getInitialPosition();
        Position otherFinalPos = ((Move) o).getFinalPosition();
        return (this.initPos.equals(otherInitPos) &&
                this.finalPos.equals(otherFinalPos));
    }

    // Naive compareTo for Move to be used in TreeSet<Move>
    // Ended up not working. Needs modification.
    @Override
    public int compareTo(Move o) {
        if (o == null) {
            return -1;
        }
        
        if (this.equals(o)) {
            return 0;
        }
        
        return 1;
    }
    
    // Debugging purposes.
    @Override
    public String toString() {
        return this.initPos + " " + this.finalPos;
    }

}
