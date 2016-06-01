package DAO;

import model.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class TaskDAO {
    public List<Task> findTasksByProblem(Integer problemIdentifier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from tasks where problem_id = "
                + problemIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Task.class);
        List<Task> result = query.list();
        transaction.commit();
        session.close();
        if (result == null) {
            throw new NullPointerException("DB returns null");
        }
        return result;
    }

    public void addTask(Task task) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "insert into tasks(problem_id, primary_task_id, "
                + "date_create, date_deadline, amount_of_primary_task,"
                + "priority) values(:problem, :primaryTask, "
                + ":creation, :deadline, :amount, :priority);";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Task.class);
        query.setParameter("problem", task.getProblem().getIdentifier());
        query.setParameter("primaryTask", task.getPrimaryTask().getIdentifier());
        query.setParameter("creation", task.getCreateDate());
        query.setParameter("deadline", task.getDeadline());
        query.setParameter("amount", task.getAmount());
        query.setParameter("priority", task.getPriority());
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    public Task findTaskByIdentifier(Integer taskIdentifier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from tasks where task_id = "
                + taskIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(Task.class);
        List<Task> result = query.list();
        transaction.commit();
        session.close();
        if (result == null) {
            throw new NullPointerException("DB returns null list");
        }
        if (result.size() > 1) {
            System.err.println("DB returns "
                    + result.size() + " tasks by task_id = " + taskIdentifier);
        }

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
}
