package DAO;

import model.Problem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProblemDAO {
    private Session session;

    public ProblemDAO(Session session) {
        this.session = session;
    }

    public List<Problem> findProblemsByAssignedPerson(Integer personIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from problems where assign_to_person_id = "
                + personIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Problem.class);
        List<Problem> result = query.list();
        if (result == null) {
            throw new NullPointerException("DB returns null");
        }
        transaction.commit();
        session.flush();
        return result;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
