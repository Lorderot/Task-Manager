package DAO;

import model.Request;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class RequestDAO {

    public List<Request> findRequestsByProblem(Integer problemIdentifier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from request_person where task_id IN (" +
                "select task_id from tasks where problem_id = "
                + problemIdentifier + " AND canceled = false);";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Request.class);
        List<Request> requests = query.list();
        transaction.commit();
        session.close();
        if (requests == null) {
            throw new NullPointerException("DB returns null list");
        }
        return requests;
    }

    public Request findCurrentRequestByTaskID(Integer taskID) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from request_person where canceled = false " +
                "AND task_id = " + taskID + ";";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(Request.class);
        List<Request> list = query.list();
        transaction.commit();
        session.close();
        if (list == null) {
            throw new NullPointerException("DB return null list");
        }
        if(list.size() > 1) {
            System.err.println("DB RETURN + " + list.size() + " REQUESTS!!!");
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public void addRequest(Request request) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "insert into request_person(task_id, date_of_request, " +
                "description, canceled) values(:task, :date, :description, false);";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(Request.class);
        query.setParameter("task", request.getTask().getIdentifier());
        query.setParameter("date", request.getDateOfRequest());
        query.setParameter("description", request.getDescription());
        query.executeUpdate();
        transaction.commit();;
        session.close();
    }

    public void update(Request request) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(request);
        transaction.commit();
        session.close();
    }
}
