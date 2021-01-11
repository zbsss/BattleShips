package model.game;

import model.statuses.CellStatus;

import java.util.ArrayList;
import java.util.Collection;

public class Cell {
    private final Position position;
    private CellStatus status;
    private final Collection<CellObserver> observers = new ArrayList<>();

    public Cell(Position position){
        this.position = position;
        status = CellStatus.WATER;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * changes status of the cell to hit or miss
     */
    public CellStatus hit() {
        if(status == CellStatus.SHIP){
            setStatus(CellStatus.HIT);
        }
        else if (status == CellStatus.WATER){
            setStatus(CellStatus.MISS);
        }

        return status;
    }

    /**
     * @return status of the cell
     */
    public CellStatus getStatus() {
        return status;
    }

    /**
     * sets cell status and notifies observers
     * @param newStatus new status of the cell
     */
    public void setStatus(CellStatus newStatus){
        status = newStatus;
        notifyObservers();
    }

    /**
     * adds a new observer to the cell
     * @param observer new observer to be added
     */
    public void addObserver(CellObserver observer){
        observers.add(observer);
    }

    /**
     * notifies all observers of the cell
     */
    private void notifyObservers(){
        for (CellObserver observer: observers) {
            observer.update(this);
        }
    }
}
