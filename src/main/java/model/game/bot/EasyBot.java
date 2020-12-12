package model.game.bot;


import model.game.AbstractPlayer;
import model.game.Position;

/**
 * Makes every move randomly not remembering the previous one
 */
public class EasyBot extends AbstractPlayer {

    public EasyBot(int boardSize) {
        super(boardSize);
    }

    /**
     * picks the next move at random
     */
    @Override
    public Position makeTurn() {
        return null;
    }

}
