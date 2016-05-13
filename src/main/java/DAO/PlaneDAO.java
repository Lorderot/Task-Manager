package DAO;

import model.Plane;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class PlaneDAO {
    public List<Plane> findAll() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from planes;";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Plane.class);
        List<Plane> list = query.list();
        transaction.commit();
        session.close();
        if (list == null) {
            throw new NullPointerException("DB returns null list.");
        }
        return list;
    }

    public void update(Plane plane) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(plane);
        transaction.commit();
        session.close();
    }

}
