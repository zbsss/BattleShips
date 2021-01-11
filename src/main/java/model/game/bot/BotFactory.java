package model.game.bot;

import model.game.AbstractPlayer;
import model.statuses.Difficulty;

public class BotFactory {

    public static AbstractPlayer createBot(Difficulty difficulty, int boardSize){
        switch (difficulty) {
            case EASY: return new EasyBot(boardSize);
            case HARD: return new HardBot(boardSize);
        };
        return null;
    }
}
