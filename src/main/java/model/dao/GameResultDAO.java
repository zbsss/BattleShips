package model.dao;

import model.game.GameResult;
import model.players.PlayerInfo;
import model.statuses.Difficulty;
import model.statuses.Result;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Optional;

public class GameResultDAO extends GenericDAO<GameResult>{
    public Optional<GameResult> create(LocalDateTime beginTime, LocalDateTime endTime, Result result, Difficulty difficulty, int playerId, PlayerInfo player) {
        try{
            save(new GameResult(beginTime, endTime, result, difficulty, playerId, player));
            Optional<PlayerInfo> playerInfo = findPlayerById(playerId);
            Optional<GameResult> gameResult = findGameResult(beginTime, player);
            playerInfo.get().addGame(gameResult.get());
            return findGameResult(beginTime, player);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PlayerInfo> findPlayerById(final int id) {
        try {
            PlayerInfo playerInfo = currentSession().createQuery("SELECT p FROM PlayerInfo p WHERE p.id = :id", PlayerInfo.class)
                    .setParameter("id", id).getSingleResult();
            return Optional.of(playerInfo);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<GameResult> findGameResult(final LocalDateTime beginTime, final PlayerInfo player) {
        try {
            GameResult gameResult = currentSession().createQuery("SELECT g FROM GameResult g WHERE g.beginTime = :begin_time AND g.player = :player", GameResult.class)
                    .setParameter("begin_time", beginTime)
                    .setParameter("player", player).getSingleResult();
            return Optional.of(gameResult);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
