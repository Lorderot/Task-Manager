package DAO;

import model.Person;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class PersonDAO {

    public Person findPersonByLogin(String login) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("select * from persons where login = :login")
                .addEntity(Person.class);
        query.setParameter("login", login);
        List queryList = query.list();
        transaction.commit();
        session.close();
        if (queryList == null) {
            throw new NullPointerException("DB returns null list.");
        }
        if (!queryList.isEmpty()) {
            return (Person) queryList.get(0);
        } else {
            return null;
        }
    }

    public List<Person> findAvailableWorkers() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from current_workers;";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(Person.class);
        List<Person> persons = query.list();
        transaction.commit();
        session.close();
        if (persons == null) {
            throw new NullPointerException("DB returns null list");
        }
        return persons;
    }

    public void updatePersonPassword(Person person) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "alter user " + person.getLogin().toLowerCase()
                + " with password '" + person.getPassword() + "';" ;
        Query query = session.createSQLQuery(sqlQuery);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }
}
