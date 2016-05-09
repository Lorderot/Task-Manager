package DAO;

import model.Plane;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PlaneDAO {
    private Session session;

    public PlaneDAO(Session session) {
        this.session = session;
    }

    public List<Plane> findAll() {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from planes;";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Plane.class);
        List<Plane> list = query.list();
        if (list == null) {
            throw new NullPointerException("DB returns null list.");
        }
        transaction.commit();
        session.flush();
        return list;
    }

    public void update(Plane plane) {
        Transaction transaction = session.beginTransaction();
        session.update(plane);
        transaction.commit();
        session.flush();
    }

    public void changeSession(Session session) {
        this.session = session;
    }
}
