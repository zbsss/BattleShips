package model.game;

import model.statuses.CellStatus;

import java.util.*;

public abstract class AbstractPlayer {
    private final Board myBoard;
    private final Board enemyBoard;
    private final Collection<Ship> ships;

    public AbstractPlayer(int boardSize){
        myBoard = new Board(boardSize);
        enemyBoard = new Board(boardSize);

        ships = new ArrayList<>();
        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(3));
        ships.add(new Ship(3));
        ships.add(new Ship(4));
    }

    public boolean hasNotLost(){
        for(Ship ship : ships){
            if(!ship.isDestroyed())
                return true;
        }
        return false;
    }

    /**
     * places all ships in random positions
     */
    public void place() {
        Random rnd = new Random();
        int boardSize = getMyBoard().getBoardSize();

        Position shipBeginning;
        boolean vertical;

        for(Ship ship : getShips()){
            do{
                vertical = rnd.nextBoolean();
                int i = rnd.nextInt(boardSize);
                int j = rnd.nextInt(boardSize);
                shipBeginning = getShipBeginning(ship, i, j, vertical);
            } while (shipBeginning == null);

            // Construct a list of all Positions of the ship
            List<Position> positions = new LinkedList<>();
            int i = shipBeginning.getX();
            int j = shipBeginning.getY();

            if(vertical){
                for(int ni = i; ni < i + ship.getLength(); ni++){
                    positions.add(new Position(ni, j));
                }
            }
            else{
                for(int nj = j; nj < j + ship.getLength(); nj++){
                    positions.add(new Position(i, nj));
                }
            }

            // Place the ship on given positions
            getMyBoard().place(positions, ship);
        }
    }


    /**
     * given some starting position and orientation
     * returns a starting position for the ship
     * (highest point if it's vertical or left-most if not vertical (horizontal))
     * or null if some position along the way was taken
     */
    private Position getShipBeginning(Ship ship, int i, int j, boolean vertical){
        int boardSize = myBoard.getBoardSize();
        int shipLength = ship.getLength();

        /*
        Set i, j at the beginning of the ship
        vertical means that (i, j) is the highest point of the ship
        horizontal means that (i,j) is the left-most point of the ship
        */
        if(vertical){
            if(i + shipLength > boardSize){
                i -= shipLength;
            }
        }
        else if(j + shipLength > boardSize){
            j -= shipLength;
        }

        // Check if all positions and all positions around them are free
        // If some positions were taken return null
        boolean isFree;
        if(vertical) {
             isFree = spaceNotFree(i, j, boardSize, ship.getLength(), 1);
        }
        else{
            isFree = spaceNotFree(i, j, boardSize, 1, ship.getLength());
        }

        return isFree ? new Position(i, j) : null;
    }

    /**
     * Checks if all cells around the ship are free, so that the ship can be freely placed
     * @param Di delta i, length of the ship if ship is vertical or 1 if it's horizontal
     * @param Dj delta j, length of the ship if ship is horizontal or 1 if it's vertical
     * @return true if all positions are free, false if at least one is taken
     */
    private boolean spaceNotFree(int i, int j, int boardSize, int Di, int Dj) {
        for (int ni = Math.max(i - 1, 0); ni <= Math.min(i + Di, boardSize - 1); ni++) {
            for (int nj = Math.max(j - 1, 0); nj <= Math.min(j + Dj, boardSize - 1); nj++) {
                if (!myBoard.cellFree(new Position(ni, nj)))
                    return true;
            }
        }
        return false;
    }


    /**
     * plays a turn
     * @return position on board selected by player
     */
    public abstract Position makeTurn();

    /**
     * @param position position that the enemy player selected in his move
     * @return new status of the cell, at the selected position, after the cell was hit
     */
    public CellStatus enemyTurn(Position position){
        return myBoard.hit(position);
    }

    /**
     * updates the newStatus of a cell on the enemy board
     * @param position position on the board to be updated
     * @param newStatus new newStatus of the cell at position
     */
    public void updateEnemyBoard(Position position, CellStatus newStatus){
        enemyBoard.getCell(position).setStatus(newStatus);
    }

    /**
     * @return players board
     */
    public Board getMyBoard() {
        return myBoard;
    }

    public Collection<Ship> getShips() {
        return ships;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public int getBoardSize(){
        return myBoard.getBoardSize();
    }
}
