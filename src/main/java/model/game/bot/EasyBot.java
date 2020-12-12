package model.game.bot;


import model.game.AbstractPlayer;
import model.game.Position;

import java.util.Random;

/**
 * Makes every move randomly not remembering the previous one
 */
public class EasyBot extends AbstractPlayer {
    private Random random = new Random();

    public EasyBot(int boardSize) {
        super(boardSize);
    }

    /**
     * picks the next move at random
     */
    @Override
    public Position makeTurn() {
        Position position;

        do{
            position = new Position(random.nextInt(getBoardSize()),
                                    random.nextInt(getBoardSize()));
        }while(!getEnemyBoard().cellCanBeHit(position));

        return position;
    }

}
