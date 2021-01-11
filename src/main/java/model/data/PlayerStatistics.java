package model.data;

public class PlayerStatistics {
    private PlayerInfo player;
    private long wins;
    private long loses;

    public PlayerStatistics(PlayerInfo player, long wins, long loses) {
        this.player = player;
        this.wins = wins;
        this.loses = loses;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public long getWins() {
        return wins;
    }

    public long getLoses() {
        return loses;
    }
}
