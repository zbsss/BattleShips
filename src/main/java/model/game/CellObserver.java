package model.game;

import model.statuses.CellStatus;

public interface CellObserver {
    /**
     * calls the observer after status of the cell was changed
     * @param newCell the updated cell status
     */
    void update(Cell newCell);
}
