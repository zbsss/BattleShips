package model.game;

import model.statuses.CellStatus;

public interface CellObserver {
    /**
     * calls the observer after status of the cell was changed
     * @param newStatus the updated cell status
     */
    void update(CellStatus newStatus);
}
