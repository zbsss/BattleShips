package model.game.bot;

import model.game.AbstractPlayer;
import model.game.Position;

/**
 * Just like the MediumBot but it has a higher chance of hitting randomly
 */
public class HardBot extends AbstractPlayer {

    public HardBot(int boardSize) {
        super(boardSize);
    }

    @Override
    public Position makeTurn() {
        return null;
    }
}
