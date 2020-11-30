package game;

import statuses.CellStatus;

import java.util.ArrayList;
import java.util.Collection;

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
     * places ships on the board
     */
    public abstract void place();

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
