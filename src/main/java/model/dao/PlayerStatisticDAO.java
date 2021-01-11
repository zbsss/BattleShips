package model.dao;

import model.data.PlayerInfo;
import model.data.PlayerStatistics;
import model.statuses.Result;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerStatisticDAO extends GenericDAO{

//    public Optional<PlayerStatistics> getStatisticsForPlayer(PlayerInfo player){
//        try {
//            long wins = currentSession().createQuery("SELECT count(g.id) from GameResult g WHERE g.player.id = :player_id AND g.result = :result", Long.class)
//                    .setParameter("player_id", player.getId())
//                    .setParameter("result", Result.WON).getSingleResult();
//
//            long loses = currentSession().createQuery("SELECT count(g.id) from GameResult g WHERE g.player.id = :player_id AND g.result = :result", Long.class)
//                    .setParameter("player_id", player.getId())
//                    .setParameter("result", Result.LOST).getSingleResult();
//
//            PlayerStatistics playerStatistics = new PlayerStatistics(player, wins, loses);
//            return Optional.of(playerStatistics);
//        } catch (PersistenceException e){
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }

    class SortByWins implements Comparator<PlayerStatistics>{
        public int compare(PlayerStatistics a, PlayerStatistics b){
            return (int) (b.getWins() - a.getWins());
        }
    }

    public Optional<List<PlayerStatistics>> getStatisticsAllPlayers(){
        try {
             List<Object[]> playerStatisticsParams = currentSession().createQuery("SELECT (select count(g) from p.games g where g.result = :win) as wins," +
            "(select count(g) from p.games g where g.result = :lose) as loses, p as player FROM PlayerInfo p")
                    .setParameter("win", Result.WON)
                    .setParameter("lose", Result.LOST).getResultList();
             List<PlayerStatistics> playerStatisticsList = playerStatisticsParams.stream()
                     .map(t -> new PlayerStatistics((PlayerInfo) t[2], ((Long) t[0]).intValue(), ((Long) t[1]).intValue()))
                     .sorted(new SortByWins())
                     .collect(Collectors.toList());

            int size = playerStatisticsList.size();
            if (size < 10)
                return Optional.of(playerStatisticsList);
            List<PlayerStatistics> firstTenPlayers = new ArrayList<>();
            for (int i = 0; i < 10; i++){
                firstTenPlayers.add(playerStatisticsList.get(i));
            }
            return Optional.of(firstTenPlayers);
        } catch (PersistenceException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public int getPlayerPlace(PlayerInfo player){
        List<Object[]> playerStatisticsParams = currentSession().createQuery("SELECT (select count(g) from p.games g where g.result = :win) as wins," +
                "(select count(g) from p.games g where g.result = :lose) as loses, p as player FROM PlayerInfo p")
                .setParameter("win", Result.WON)
                .setParameter("lose", Result.LOST).getResultList();
        List<PlayerStatistics> playerStatisticsList = playerStatisticsParams.stream()
                .map(t -> new PlayerStatistics((PlayerInfo) t[2], ((Long) t[0]).intValue(), ((Long) t[1]).intValue()))
                .sorted(new SortByWins())
                .collect(Collectors.toList());

        return playerStatisticsList.stream().map(PlayerStatistics::getPlayer).collect(Collectors.toList()).indexOf(player);
    }
}
