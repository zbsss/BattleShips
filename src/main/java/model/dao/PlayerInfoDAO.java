package model.dao;

import model.data.PlayerInfo;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public class PlayerInfoDAO extends GenericDAO<PlayerInfo> {
    public Optional<PlayerInfo> create(String name, String surname, String nickName, String email, String country, String password) {
        try {
            save(new PlayerInfo(name, surname, nickName, email, country, password));
            return findByNick(nickName);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PlayerInfo> findById(final int id) {
        try {
            PlayerInfo playerInfo = currentSession().createQuery("SELECT p FROM PlayerInfo p WHERE p.id = :id", PlayerInfo.class)
                    .setParameter("id", id).getSingleResult();
            return Optional.of(playerInfo);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PlayerInfo> findByNick(final String nick) {
        try {
            PlayerInfo playerInfo = currentSession().createQuery("SELECT p FROM PlayerInfo p WHERE p.nickName = :nick", PlayerInfo.class)
                    .setParameter("nick", nick).getSingleResult();
            return Optional.of(playerInfo);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<PlayerInfo> findAllPlayers() {
        try {
            List<PlayerInfo> playerInfos = currentSession().createQuery("SELECT p FROM PlayerInfo p", PlayerInfo.class)
                    .getResultList();
            return playerInfos;
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
