package DAO;

import model.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class TaskDAO {
    private Session session;

    public TaskDAO(Session session) {
        this.session = session;
    }

    public List<Task> findTasksByProblem(Integer problemIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from tasks where problem_id = "
                + problemIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Task.class);
        List<Task> result = query.list();
        if (result == null) {
            throw new NullPointerException("DB returns null");
        }
        transaction.commit();
        session.flush();
        return result;
    }

    public Task findTaskByIdentifier(Integer taskIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from tasks where task_id = "
                + taskIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(Task.class);
        List<Task> result = query.list();
        if (result == null) {
            throw new NullPointerException("DB returns null list");
        }
        if (result.size() > 1) {
            System.err.println("DB returns "
                    + result.size() + " tasks by task_id = " + taskIdentifier);
        }
        transaction.commit();
        session.flush();
        if (result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    public void updateTask(Task task) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(task);
        transaction.commit();
        session.close();
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
