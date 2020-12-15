package model.game;

import model.game.bot.BotFactory;
import model.players.PlayerInfo;
import model.statuses.CellStatus;
import model.statuses.Difficulty;
import model.statuses.Result;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game implements Runnable{
    private final PlayerInfo playerInfo;
    private final HumanPlayer player;
    private final AbstractPlayer bot;

    private LocalDateTime beginning;
    private LocalDateTime end;
    private Result result;
    private AtomicBoolean running;
    private Difficulty difficulty;

    public Game(PlayerInfo playerInfo, int boardSize, Difficulty difficulty){
        this.playerInfo = playerInfo;
        player = new HumanPlayer(boardSize);
        bot = BotFactory.createBot(difficulty, boardSize);
        this.difficulty = difficulty;
    }

    public HumanPlayer getPlayer() {
        return player;
    }

    public AbstractPlayer getBot() {
        return bot;
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
     * main loop of the game, after the game if finished sets results
     */
    @Override
    public void run(){
        beginning = LocalDateTime.now();
        running = new AtomicBoolean(true);

        player.place();
        bot.place();

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

        if (result != Result.CANCELED)
            result = player.hasNotLost() ? Result.WON : Result.LOST;

        System.out.println("THE GAME IS ENDED!");
    }

    /**
     * interrupts the game
     */
    public void cancel(){
        running.set(false);
        result = Result.CANCELED;
    }

    /**
     * @return result of the game
     */
    public Result getResult() {
        return result;
    }
}
