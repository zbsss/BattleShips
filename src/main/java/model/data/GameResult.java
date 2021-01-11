package model.data;

import model.statuses.Difficulty;
import model.statuses.Result;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = GameResult.TABLENAME)
public class GameResult {
    static final String TABLENAME = "game_result";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.BEGINTIME, nullable = false)
    private LocalDateTime beginTime;

    @Column(name = Columns.ENDTIME, nullable = false)
    private LocalDateTime endTime;

    @Column(name = Columns.RESULT, nullable = false)
    private Result result;

    @Column(name = Columns.DIFFICULTY, nullable = false)
    private Difficulty difficulty;

//    @Column(name = Columns.PLAYERID, nullable = false)
//    private int playerId;

    @ManyToOne
    private PlayerInfo player;

    public GameResult(LocalDateTime beginTime, LocalDateTime endTime, Result result, Difficulty difficulty, int playerId, PlayerInfo player) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.result = result;
        this.difficulty = difficulty;
//        this.playerId = playerId;
        this.player = player;
    }

    public GameResult() {
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

//    public int getPlayerId() {
//        return playerId;
//    }

//    public void setPlayerId(int playerId) {
//        this.playerId = playerId;
//    }

    static class Columns{
        static final String ID = "id";
        static final String BEGINTIME = "begin_time";
        static final String ENDTIME = "end_time";
        static final String RESULT = "result";
        static final String DIFFICULTY = "difficulty";
        static final String PLAYERID = "player_id";
    }
}
