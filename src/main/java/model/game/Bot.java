package model.game;

import model.statuses.CellStatus;
import model.statuses.Difficulty;

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
