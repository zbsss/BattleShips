package model.game;

import model.statuses.ShipStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HumanPlayer extends AbstractPlayer {
    private final Lock lock = new ReentrantLock();
    private final Condition shipPlaced = lock.newCondition();
    private final Condition hit = lock.newCondition();
    Collection<Position> placePositions = null;
    Position hitPosition = null;
    AtomicBoolean running = new AtomicBoolean(true);

    public HumanPlayer(int size) {
        super(size);
    }

    private void placeShip(Collection<Position> positions, Ship ship){
        getMyBoard().place(positions, ship);
        System.out.println("SHIP HAS BEEN PLACED: " + positions);
    }

    @Override
    public void place() {
        while (!allShipsPlaced() && running.get()) {
            placeSingleShip();
        }
    }

    private void placeSingleShip() {
        try {
            Collection<Position> shipPositions = getPositionsFromView();
            if (shipPositions != null)
                getShips().stream().filter(s -> s.getLength() == shipPositions.size() && s.getStatus() == ShipStatus.CREATED).findAny().ifPresent(s -> placeShip(shipPositions, s));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Collection<Position> getPositionsFromView() throws InterruptedException {
        lock.lock();
        if (placePositions == null)
            shipPlaced.await();

        Collection<Position> result = placePositions;
        placePositions = null;
        lock.unlock();

        return result;
    }

    private boolean allShipsPlaced() {
        return getShips().stream().allMatch(s -> s.getStatus() == ShipStatus.SAILING);
    }

    public void setPositions(Collection<Position> positions) {
        try {
            lock.lock();
            this.placePositions = positions;
            shipPlaced.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Position makeTurn() throws InterruptedException{
        try {
            lock.lock();
            if(running.get() && hitPosition == null)
                hit.await();
            while(running.get() && !getEnemyBoard().cellCanBeHit(hitPosition))
                hit.await();
            if (running.get()) {
                Position res = hitPosition;
                hitPosition = null;
                return res;
            } else {
                throw new InterruptedException("The game has been cancelled");
            }
        } finally {
            lock.unlock();
        }
    }


    public void tryHit(Position position) {
        try {
            lock.lock();
            this.hitPosition = position;
            hit.signal();
        } finally {
            lock.unlock();
        }
    }

    public void cancel() {
        try {
            lock.lock();
            running.set(false);
            if (allShipsPlaced()) {
                this.hitPosition = new Position(0, 0);
                hit.signal();
            }
            else {
                this.placePositions = Collections.singletonList(new Position(0, 0));
                shipPlaced.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void placeRandomly() {
        try {
            lock.lock();
            super.place();
            shipPlaced.signal();
        } finally {
            lock.unlock();
        }
    }
}
