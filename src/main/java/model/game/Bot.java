package game;

import statuses.CellStatus;
import statuses.Difficulty;

public class Bot extends AbstractPlayer {
    private final Difficulty difficulty;

    public Bot(int size, Difficulty difficulty) {
        super(size);
        this.difficulty = difficulty;
    }

    @Override
    public void place() {

    }

    @Override
    public Position makeTurn() {
        return null;
    }
}
