package model.game.bot;

import model.game.AbstractPlayer;
import model.game.Position;

/**
 * First picks at random, than after a hit it tries in the vicinity
 */
public class MediumBot extends AbstractPlayer {

    public MediumBot(int boardSize) {
        super(boardSize);
    }

    @Override
    public Position makeTurn() {
        return null;
    }
}
