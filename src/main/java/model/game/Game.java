package model.game;

import model.game.bot.BotFactory;
import model.players.PlayerInfo;
import model.statuses.CellStatus;
import model.statuses.Difficulty;
import model.statuses.Result;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    private final PlayerInfo playerInfo;
    private final HumanPlayer player;
    private final AbstractPlayer bot;

    private LocalDateTime beginning;
    private LocalDateTime end;
    private Result result;
    private AtomicBoolean running;

    public Game(PlayerInfo playerInfo, int boardSize, Difficulty difficulty){
        this.playerInfo = playerInfo;
        player = new HumanPlayer(boardSize);
        bot = BotFactory.createBot(difficulty, boardSize);
    }

    /**
     * plays a single turn of the active player
     * @param active player that makes a move
     * @param passive other player
     */
    private void playTurn(AbstractPlayer active, AbstractPlayer passive){
        Position position = active.makeTurn();
        CellStatus status = passive.enemyTurn(position);
        active.updateEnemyBoard(position, status);
    }

    /**
     * plays a single round of the game
     */
    private void playRound(){
        playTurn(player, bot);
        playTurn(bot, player);
    }

    /**
     * starts the game
     */
    public void start(){
        beginning = LocalDateTime.now();
        running = new AtomicBoolean(true);

        player.place();
        bot.place();

        run();
    }

    /**
     * main loop of the game, after the game if finished sets results
     */
    private void run(){
        while (running.get() &&
                player.hasNotLost() &&
                bot.hasNotLost()) {
            playRound();
        }

        setResult();
    }

    /**
     * saves the result of the game
     */
    private void setResult(){
        end = LocalDateTime.now();

        if(player.hasNotLost() && bot.hasNotLost())
            result = Result.CANCELED;
        else
            result = player.hasNotLost() ? Result.WON : Result.LOST;
    }

    /**
     * interrupts the game
     */
    public void cancel(){
        running.set(false);
    }

    /**
     * @return result of the game
     */
    public Result getResult() {
        return result;
    }
}
