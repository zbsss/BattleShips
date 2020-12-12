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

        // Todo add all ships
        ships = new ArrayList<>();
        ships.add(new Ship(2));
        ships.add(new Ship(3));
        ships.add(new Ship(4));
        ships.add(new Ship(6));
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
     * or null if some position along the way was taken
     */
    private Position getShipBeginning(Ship ship, int i, int j, boolean vertical){
        int boardSize = myBoard.getBoardSize();
        int shipLength = ship.getLength();

        // Set i, j at the beginning of the ship
        if(vertical){
            if(i + shipLength > boardSize){
                i -= shipLength;
            }
        }
        else if(j + shipLength > boardSize){
            j -= shipLength;
        }


        // Check if all positions are free
        boolean isFree = true;

        if(vertical){
            for(int ni = i; ni < i + shipLength; ni++){
                if(!myBoard.cellFree(new Position(ni, j))){
                    isFree = false;
                    break;
                }
            }
        }
        else{
            for(int nj = j; nj < j + shipLength; nj++){
                if(!myBoard.cellFree(new Position(i, nj))){
                    isFree = false;
                    break;
                }
            }
        }

        // If some positions were taken return null
        return isFree ? new Position(i, j) : null;
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
}
