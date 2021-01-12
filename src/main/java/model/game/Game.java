package model.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.dao.GameResultDAO;
import model.dao.PlayerStatisticDAO;
import model.data.PlayerStatistics;
import model.game.bot.BotFactory;
import model.data.PlayerInfo;
import model.statuses.CellStatus;
import model.statuses.Difficulty;
import model.statuses.Result;
import service.EmailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Game implements Runnable{
    private final PlayerInfo playerInfo;
    private final HumanPlayer player;
    private final AbstractPlayer bot;

    private LocalDateTime beginning;
    private LocalDateTime end;
    private Result result;
    private AtomicBoolean running;
    private BooleanProperty runningProperty = new SimpleBooleanProperty(false);
    private final Difficulty difficulty;

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


    public BooleanProperty runningPropertyProperty() {
        return runningProperty;
    }

    /**
     * plays a single turn of the active player
     * @param active player that makes a move
     * @param passive other player
     */
    private void playTurn(AbstractPlayer active, AbstractPlayer passive) throws InterruptedException {
        Position position = active.makeTurn();
        CellStatus status = passive.enemyTurn(position);
        active.updateEnemyBoard(position, status);
    }

    /**
     * plays a single round of the game
     */
    private void playRound(){
        try {
            playTurn(player, bot);
            playTurn(bot, player);
        } catch (InterruptedException ex) {
            if (!running.get()) {
                System.out.println("The game has been canceled");
            } else {
                ex.printStackTrace();
            }
        }

    }


    /**
     * main loop of the game, after the game if finished sets results
     */
    @Override
    public void run(){
        beginning = LocalDateTime.now();
        running = new AtomicBoolean(true);
        runningProperty.set(true);

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
     * saves the result of the game, sends ranking email notifications
     */
    private void setResult(){
        end = LocalDateTime.now();
        running.set(false);
        runningProperty.set(false);

        if (result != Result.CANCELED) {
            PlayerStatisticDAO ranking = new PlayerStatisticDAO();
            int prev = ranking.getPlayerPlace(playerInfo);

            result = player.hasNotLost() ? Result.WON : Result.LOST;
            GameResultDAO gameResultDAO = new GameResultDAO();
            gameResultDAO.create(beginning, end, result, difficulty, playerInfo);

            int curr = ranking.getPlayerPlace(playerInfo);
            if(curr < prev) {
                List<PlayerInfo> players = ranking
                        .getStatisticsAllPlayers().get()
                        .stream().map(PlayerStatistics::getPlayer)
                        .collect(Collectors.toList());
                EmailService email = new EmailService();
                for (int i = curr + 1; i <= prev; i++) {
                    email.rankingNotification(players.get(i));
                }
            }
        }

        System.out.println("THE GAME IS ENDED!");
    }

    /**
     * interrupts the game
     */
    public void cancel(){
        running.set(false);
        runningProperty.set(false);
        result = Result.CANCELED;
    }

    /**
     * @return result of the game
     */
    public Result getResult() {
        return result;
    }
}
