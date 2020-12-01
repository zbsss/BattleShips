package model.players;

public class PlayerStatistics {
    private String nickName;
    private int wins;
    private int loses;
    // Todo, what else?

    public PlayerStatistics(String nickName, int wins, int loses) {
        this.nickName = nickName;
        this.wins = wins;
        this.loses = loses;
    }
}
