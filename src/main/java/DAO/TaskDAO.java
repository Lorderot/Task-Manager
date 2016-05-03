package DAO;

import model.Problem;
import model.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TaskDAO {
    private Session session;

    public TaskDAO(Session session) {
        this.session = session;
    }

    public List<Task> findTasks(Problem problem) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from tasks where problem_id = "
                + problem.getIdentifier() + ";";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Task.class);
        List<Task> result = query.list();
        if (result == null) {
            throw new NullPointerException("DB returns null");
        }
        transaction.commit();
        session.flush();
        return result;
    }

    public void changeSession(Session session) {
        this.session = session;
    }
}
