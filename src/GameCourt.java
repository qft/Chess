/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Board board;
    private Piece currentPiece;
    private Color currentPlayer = Color.WHITE;
    private boolean clicked = false;
    private Position initPos;
    private Position currentPos;
    private TreeSet<Move> possibleMoves;

    public boolean playing = false; // whether the game is running
    private JLabel status; // Current status text (i.e. Running...)


    // Game constants
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 500;
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JOptionPane.showMessageDialog(new JComponent() {}, "Just a simple two-"
                + "player chess game. \nDrag and drop to move pieces. Preview "
                + "of possible moves \n(not including moves which would put the "
                + "King in check) are shown.\n\n\n"
                + "If you need a refresher in how to play chess: \n"
                + "http://en.wikipedia.org/wiki/Chess\n\n\n"
                + "I have implemented en passant capture, castling, and pawn\n"
                + "promotion to any piece except a Pawn or a King. \n"
                + "Castling must be initiated at the player's King.");
        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long
        // as an arrow key is pressed, by changing the square's
        // velocity accordingly. (The tick method below actually
        // moves the square.)

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                // Convert mouse click location to a board Position.
                int x = (int) Math.round(e.getX() * 8.0 / COURT_WIDTH - 0.5);
                int y = (int) Math.round(e.getY() * 8.0 / COURT_HEIGHT - 0.5);

                // Get Piece which was clicked.
                Piece temp = board.getPiece(new Position(x, y));

                // Check if this Piece can be moved by currentPlayer.
                if (!clicked && temp != null &&
                        temp.getColor().equals(currentPlayer)) {
                    currentPiece = temp;
                    
                    // For the preview
                    possibleMoves = currentPiece.getPossibleMoves(board);
                    
                    // Set initPos.
                    initPos = new Position(x, y);
                    clicked = true;
                }
            }


            @Override
            public void mouseReleased (MouseEvent e) {

                // Convert mouse click location to a board Position.
                int x = (int) Math.round(e.getX() * 8.0 / COURT_WIDTH - 0.5);
                int y = (int) Math.round(e.getY() * 8.0 / COURT_HEIGHT - 0.5);

                // Check if piece is currently selected (mouseClicked was successful)
                if (clicked) {

                    currentPos = new Position(x, y);
                    Move moveTo = new Move(initPos, currentPos);

                    try {
                    if (board.canMove(currentPiece, moveTo)) {
                        board.movePiece(currentPiece, moveTo);
                        // Check if currentPiece is a pawn to be promoted.
                        if (currentPiece instanceof WhitePawn && (y == 7 || y == 0)) {
                            promotePawn();
                        }

                        // Flip the currentPlayer color.
                        if (Color.BLACK.equals(currentPlayer)) {
                            currentPlayer = Color.WHITE;
                        }
                        else {
                            currentPlayer = Color.BLACK;
                        }
                        // Check endgame conditions.
                        checkLogic();
                    }
                    } catch (IllegalArgumentException i) {
                        setStatus("Would be in check!");
                    } catch (NullPointerException npe) {
                        if (Color.BLACK.equals(currentPlayer)) {
                            currentPlayer = Color.WHITE;
                        }
                        else {
                            currentPlayer = Color.BLACK;
                        }
                        // Check endgame conditions.
                        checkLogic();
                    }
                }

                // Reset everything, no matter if a Piece was moved or not.
                currentPos = null;
                clicked = false;
                currentPiece = null;
                initPos = null;
                possibleMoves = null;
            }
        });

        this.status = status;
    }

    // Homework submission gets angry if I try to change status from within the
    // anonymous inner class.
    private void setStatus(String s) {
        status.setText(s);
    }
    
    /**
     * Get user input to promote the Pawn to a piece of choice.
     */
    private void promotePawn() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JComponent popup = new JComponent(){};

        // Default choice is a Queen.
        String piece = JOptionPane.showInputDialog(popup, 
                "What piece would you like to promote this pawn to?\n" + 
                        "(Queen, Rook, Knight, Bishop)", "Queen");
        switch (piece) {
        case "Queen":
            board.addPiece(
                    new Queen(currentPiece.getColor(), currentPos), 
                    currentPos);
            break;
        case "Bishop":
            board.addPiece(
                    new Bishop(currentPiece.getColor(), currentPos), 
                    currentPos);
            break;
        case "Rook":
            board.addPiece(
                    new Rook(currentPiece.getColor(), currentPos), 
                    currentPos);
            break;
        case "Knight":
            board.addPiece(
                    new Knight(currentPiece.getColor(), currentPos), 
                    currentPos);
            break;
        // If user mistypes, promotePawn() is run again.
        default:
            JOptionPane.showMessageDialog(popup, "Sorry what was that?");
            promotePawn();
            break;
        }

    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        board = new Board();
        playing = true;
        status.setText("Running...");
        clicked = false;
        currentPiece = null;
        currentPlayer = Color.WHITE;
        initPos = null;
        currentPos = null;
        possibleMoves = null;

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        // update the display
        repaint();
    }

    private void checkLogic() {
        if (playing) {
            // Check for game ending conditions. Displays current check status.
            if (currentPlayer.equals(Color.BLACK) && board.inCheck(Color.BLACK)) {
                status.setText("Black is in check!");
                if (board.isCheckmate(Color.BLACK)) {
                    playing = false;
                    status.setText("White wins!");
                }
            } else if (currentPlayer.equals(Color.WHITE) && board.inCheck(Color.WHITE)) {
                status.setText("White is in check!");
                if (board.isCheckmate(Color.WHITE)) {
                    playing = false;
                    status.setText("Black wins!");
                }
            }
            else {
                status.setText("Running...");
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.GRAY);
        super.paintComponent(g);
        board.draw(g);
        this.drawPossibleMoves(g, possibleMoves);
    }
    
    private void drawPossibleMoves(Graphics g, TreeSet<Move> moves) {
        if (moves == null) {
            return;
        }

        for (Move m : moves) {
            // Self-explanatory.
            Position pos = m.getFinalPosition();

            // Get pixel location from board Position and center the shape.
            int x = (int) (62.5 * pos.getX() + 20);
            int y = (int) (62.5 * pos.getY() + 20);
            g.fillOval(x, y, 20, 20);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
