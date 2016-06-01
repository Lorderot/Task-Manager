package DAO;

import model.Problem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.sql.*;
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

    public Double getProgress(Integer problem) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select get_project_progress(?);";
        Connection connection = HibernateUtil.getConnection(session);
        PreparedStatement statement;
        Double result = null;
        ResultSet resultSet;
        try {
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, problem);
            resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return result;
    }

    public Long getTime(Integer problem) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select get_time_to_finish_project(?);";
        Connection connection = HibernateUtil.getConnection(session);
        PreparedStatement statement;
        Long result = null;
        ResultSet resultSet;
        try {
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, problem);
            resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
            session.close();
        }
        return result;
    }
}
