package model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import service.SessionService;

import javax.persistence.PersistenceException;

public abstract class GenericDAO<T> {

    public void save(final T object) throws PersistenceException {
        final Session session = SessionService.getSession();
        final Transaction tx = session.beginTransaction();
        session.save(object);
        session.merge(object);
        tx.commit();
    }

    public void update(final T object) throws PersistenceException {
        final Session session = SessionService.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(object);
        session.merge(object);
        tx.commit();
    }

    public Session currentSession() {
        return SessionService.getSession();
    }
}
