package DAO;

import model.AssignedTask;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class AssignedTaskDAO {
    private Session session;

    public AssignedTaskDAO(Session session) {
        this.session = session;
    }

    public void addAssignedTask(AssignedTask assignedTask) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "insert into assigned_tasks(person_id, task_id, " +
                "date_of_assignment, progress, finish) " +
                "values(:person_id, :task_id, :date_of_assignment, " +
                ":progress, :finish);";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(AssignedTask.class);
        query.setParameter("person_id", assignedTask.getPerson().getIdentifier());
        query.setParameter("task_id", assignedTask.getTask().getIdentifier());
        query.setParameter("date_of_assignment", assignedTask.getAssignmentDate());
        query.setParameter("progress", assignedTask.getProgress());
        query.setParameter("finish", assignedTask.getFinished());
        query.executeUpdate();
        transaction.commit();
        session.flush();
    }

    public AssignedTask findAssignedTaskByPersonAndTask(
            Integer personIdentifier, Integer taskIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from assigned_tasks where task_id = "
                + taskIdentifier + " AND person_id = " + personIdentifier
                + " ;";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(AssignedTask.class);
        List<AssignedTask> list = query.list();
        if (list == null) {
            throw new NullPointerException("DB returns null list");
        }
        if (list.size() > 1) {
            System.err.print("DB return list with " + list.size() + " elements!");
        }
        transaction.commit();
        session.flush();
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public AssignedTask findCurrentTaskAssignedToPerson(Integer taskIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select *  from assigned_tasks where " +
                "(date_finish IS NULL OR finish = true) AND " +
                "task_id = " + taskIdentifier + ";";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(AssignedTask.class);
        List<AssignedTask> list = query.list();
        if (list == null) {
            throw new NullPointerException("DB returns null list");
        }
        if (list.size() > 1) {
            System.err.println("DB returns "
                    + list.size() + " assigned tasks");
        }
        transaction.commit();
        session.flush();
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public void updateAssignedTask(AssignedTask assignedTask) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(assignedTask);
        transaction.commit();
        session.close();
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
