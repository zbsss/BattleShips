package model.game;

import model.statuses.CellStatus;
import model.statuses.ShipStatus;

import java.util.Collection;

public class Ship implements CellObserver{
    private final int length;
    private Collection<Cell> cells;
    private ShipStatus status;

    public Ship(int length){
        this.length = length;
        this.status = ShipStatus.CREATED;
    }

    public ShipStatus getStatus() {
        return status;
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
        this.status = ShipStatus.SAILING;
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
        for (Cell cell : cells) {
            cell.setStatus(CellStatus.DESTROYED);

        }
    }

    public boolean isDestroyed(){
        return status == ShipStatus.DESTROYED;
    }

    /**
     * if the cell was hit, check if the ship has sunk
     * @param newCell the updated cell
     */
    @Override
    public void update(Cell newCell) {
        if(newCell.getStatus() == CellStatus.HIT)
            checkIfSunk();
    }

    /**
     * @return length of the ship
     */
    public int getLength() {
        return length;
    }
}
