package DAO;

import model.Problem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ProblemDAO {

    public List<Problem> findProblemsByAssignedPerson(Integer personIdentifier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from problems where assign_to_person_id = "
                + personIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Problem.class);
        List<Problem> result = query.list();
        transaction.commit();
        session.close();
        if (result == null) {
            throw new NullPointerException("DB returns null");
        }
        return result;
    }
}
