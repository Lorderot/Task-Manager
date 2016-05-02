package DAO;

import model.PrimaryTask;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class PrimaryTaskDAO {
    private Session session;

    public PrimaryTaskDAO(Session session) {
        this.session = session;
    }

    public List<PrimaryTask> findAll() {
        Query query = session.createSQLQuery("select * from primary_tasks;")
                .addEntity(PrimaryTask.class);
        List list = query.list();
        if (list == null) {
            throw new NullPointerException("DB returns null list.");
        }
        return list;
    }

    public PrimaryTask findPrimaryTaskByName(PrimaryTask primaryTask) {
        throw new UnsupportedOperationException();
    }

    public boolean add(PrimaryTask primaryTask) {
        session.beginTransaction();
        Query query = session.createSQLQuery("insert into primary_tasks(" +
                "cost, description, short_name, time_to_complete)" +
                " values(:cost,:description,:name,:time);");
        query.setParameter("cost", primaryTask.getCost());
        query.setParameter("description", primaryTask.getDescription());
        query.setParameter("name", primaryTask.getName());
        query.setParameter("time",primaryTask.getTimeToComplete().getTime());
        try {
            query.executeUpdate();
        } catch (ConstraintViolationException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.getTransaction().commit();
            session.flush();
        }
        return true;
    }
}
