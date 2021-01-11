package model.data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PlayerRanking {
    private final Collection<PlayerInfo> players = new LinkedList<>();

    /**
     * adds a new player to ranking
     * @param player new player
     */
    public void addPlayer(PlayerInfo player){
        players.add(player);
    }

    /**
     * @return statistics of all players
     */
    public Collection<PlayerStatistics> getRanking(){
        return players.stream().map(PlayerInfo::getStatistics).collect(Collectors.toList());
    }
}
