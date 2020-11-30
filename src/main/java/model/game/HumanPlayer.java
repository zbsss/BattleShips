package game;

import statuses.CellStatus;

import java.util.Collection;

public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(int size) {
        super(size);
    }

    private void placeShip(Collection<Position> positions, Ship ship){
        getMyBoard().place(positions, ship);
    }

    @Override
    public void place() {
        // For each ship, get positions from GUI and call placeShip()
    }

    @Override
    public Position makeTurn() {
        return null;
    }
}
