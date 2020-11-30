package players;

import game.Game;

import java.util.Collection;
import java.util.LinkedList;

public class PlayerInfo {
    private final String nickName;
    private final String email;
    private final String country;
    private final Collection<Game> games = new LinkedList<>();


    public PlayerInfo(String nickName, String email, String country) {
        this.nickName = nickName;
        this.email = email;
        this.country = country;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    /**
     * @return players overall statistics from all games
     */
    public PlayerStatistics getStatistics(){
        // Todo
        return null;
    }
}
