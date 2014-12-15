public class Position {
    // Two fields to store current location of piece. -1 < x,y < 8
    private final int x, y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Getters
    public int getX() { return this.x; }
    public int getY() { return this.y; }

    
    @Override
    /**
     * Returns true if x and y fields match.
     * @o object to be compared
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        Position other = (Position) o;
        
        return (this.x == other.x && this.y == other.y);
    }
    
    // Determines if x and y are within bounds of chess board
    public static boolean isValidPosition(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return (x > -1 && x < 8 && y > -1 && y < 8);
    }
    
    // Formatting for debugging purposes
    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
    
}
