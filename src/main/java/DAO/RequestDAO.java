package DAO;

import model.Request;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RequestDAO {
    private Session session;

    public RequestDAO(Session session) {
        this.session = session;
    }

    public List<Request> findRequestsByProblem(Integer problemIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from request_person where task_id IN (" +
                "select task_id from tasks where problem_id = "
                + problemIdentifier + ");";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Request.class);
        List<Request> requests = query.list();
        if (requests == null) {
            throw new NullPointerException("DB returns null list");
        }
        transaction.commit();
        session.flush();
        return requests;
    }

    public void update(Request request) {
        Transaction transaction = session.beginTransaction();
        session.update(request);
        transaction.commit();
        session.flush();
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
