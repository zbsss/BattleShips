package game;

import statuses.CellStatus;
import statuses.ShipStatus;

import java.util.Collection;

public class Ship implements CellObserver{
    private final int length;
    private Collection<Cell> cells;
    private ShipStatus status;

    public Ship(int length){
        this.length = length;
    }

    /**
     * for each cell sets it's status as SHIP and adds the ship as an observer
     * @param cells collection of cells that represent the ship
     */
    public void setCells(Collection<Cell> cells) {
        if(cells.size() != length)
            throw new IllegalArgumentException("Number of cells must be equal to length of ship.");

        this.cells = cells;
        for (Cell cell : cells) {
            cell.setStatus(CellStatus.SHIP);
            cell.addObserver(this);
        }
    }

    /**
     * checks if the ship has sunk
     * if all the cells are hit, sink the ship and set all cells as destroyed
     */
    private void checkIfSunk() {
        for (Cell cell : cells)
            if(cell.getStatus() != CellStatus.HIT)
                return;

        status = ShipStatus.DESTROYED;
        for (Cell cell : cells)
            cell.setStatus(CellStatus.DESTROYED);
    }

    public boolean isDestroyed(){
        return status == ShipStatus.DESTROYED;
    }

    /**
     * if the cell was hit, check if the ship has sunk
     * @param newStatus the updated cell status
     */
    @Override
    public void update(CellStatus newStatus) {
        if(newStatus == CellStatus.HIT)
            checkIfSunk();
    }

    /**
     * @return length of the ship
     */
    public int getLength() {
        return length;
    }
}
