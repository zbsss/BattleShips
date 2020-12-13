package model.game.bot;

import model.game.AbstractPlayer;
import model.statuses.Difficulty;

public class BotFactory {

    public static AbstractPlayer createBot(Difficulty difficulty, int boardSize){
        return switch (difficulty) {
            case EASY -> new EasyBot(boardSize);
            case HARD -> new HardBot(boardSize);
        };
    }
}
