package model.data;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;

@Entity
@Table(name = PlayerInfo.TABLENAME)
public class PlayerInfo {

    static final String TABLENAME = "player";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.NAME, nullable = false, length = 50)
    private String name;

    @Column(name = Columns.SURNAME, length = 50)
    private String surname;

    @Column(name = Columns.EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = Columns.COUNTRY)
    private String country;

    @Column(name = Columns.PASSWORD, nullable = false)
    private String password;

    @Column(name = Columns.NICK, nullable = false, unique = true)
    private String nickName;

    @OneToMany(mappedBy = "player")
    private Collection<GameResult> games = new LinkedList<>();



    public PlayerInfo(String name, String surname, String nickName, String email, String country, String password) {
        this.name = name;
        this.surname = surname;
        this.nickName = nickName;
        this.email = email;
        this.country = country;
        this.password = password;
    }

    public PlayerInfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public Collection<GameResult> getGames() {
        return games;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setGames(Collection<GameResult> games) {
        this.games = games;
    }

    public void addGame(GameResult game){
        this.games.add(game);
    }

    /**
     * @return players overall statistics from all games
     */
    public PlayerStatistics getStatistics(){
    return null;
    }

    class Columns {
        static final String ID= "id";
        static final String NAME = "name";
        static final String SURNAME = "surname";
        static final String EMAIL = "email";
        static final String COUNTRY = "country";
        static final String PASSWORD = "password";
        static final String NICK = "nick";
    }

    @Override
    public String toString(){
        return this.getNickName();
    }

}
