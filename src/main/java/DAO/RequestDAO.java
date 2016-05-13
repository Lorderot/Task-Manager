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
                + problemIdentifier + ");";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Request.class);
        List<Request> requests = query.list();
        transaction.commit();
        session.close();
        if (requests == null) {
            throw new NullPointerException("DB returns null list");
        }
        return requests;
    }

    public void update(Request request) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(request);
        transaction.commit();
        session.close();
    }
}
