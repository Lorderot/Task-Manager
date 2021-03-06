package DAO;

import model.PrimaryTask;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import util.HibernateUtil;

import java.util.List;

public class PrimaryTaskDAO {

    public List<PrimaryTask> findAll() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("select * from primary_tasks;")
                .addEntity(PrimaryTask.class);
        List list = query.list();
        transaction.commit();
        session.close();
        if (list == null) {
            throw new NullPointerException("DB returns null list.");
        }
        return list;
    }

    public PrimaryTask findPrimaryTaskByName(String primaryTask) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from primary_tasks where short_name = '"
                + primaryTask + "';";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(PrimaryTask.class);
        List<PrimaryTask> list = query.list();
        transaction.commit();
        session.close();
        if (list == null) {
            throw new NullPointerException("DB return null list" );
        }
        if (list.size() > 1) {
            System.err.println("DB return list with size " + list.size() + "!!!");
        }
        return list.get(0);
    }

    public boolean add(PrimaryTask primaryTask)
            throws ConstraintViolationException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "insert into primary_tasks("
                + "cost, description, short_name, time_to_complete)"
                + " values(:cost,:description,:name,:time);";
        Query query = session.createSQLQuery(sqlQuery);
        query.setParameter("cost", primaryTask.getCost());
        query.setParameter("description", primaryTask.getDescription());
        query.setParameter("name", primaryTask.getName());
        query.setParameter("time",primaryTask.getTimeToComplete());
        int result = 0;
        try {
            result = query.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
        if (result > 0) {
            return true;
        }
        return false;
    }
}
