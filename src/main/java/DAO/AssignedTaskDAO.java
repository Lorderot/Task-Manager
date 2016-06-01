package DAO;

import model.AssignedTask;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class AssignedTaskDAO {

    public void addAssignedTask(AssignedTask assignedTask) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "insert into assigned_tasks(person_id, task_id, " +
                "date_of_assignment, finish, amount, description) " +
                "values(:person_id, :task_id, :date_of_assignment, " +
                ":finish, :amount, :description);";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(AssignedTask.class);
        query.setParameter("person_id", assignedTask.getPerson().getIdentifier());
        query.setParameter("task_id", assignedTask.getTask().getIdentifier());
        query.setParameter("date_of_assignment", assignedTask.getAssignmentDate());
        query.setParameter("finish", assignedTask.getFinished());
        query.setParameter("amount", assignedTask.getAmount());
        query.setParameter("description", assignedTask.getDescription());
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    public List<AssignedTask> findAssignedTasks(Integer taskIdentifier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select *  from assigned_tasks where " +
                "(date_finish IS NULL OR finish = true) AND " +
                "task_id = " + taskIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(AssignedTask.class);
        List<AssignedTask> list = query.list();
        transaction.commit();
        session.close();
        if (list == null) {
            throw new NullPointerException("DB returns null list");
        }
        return list;
    }

    public void updateAssignedTask(AssignedTask assignedTask) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(assignedTask);
        transaction.commit();
        session.close();
    }
}
