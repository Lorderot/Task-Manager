package DAO;

import model.Person;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PersonDAO {
    private Session session;

    public PersonDAO(Session session) {
        this.session = session;
    }

    public Person findPersonByLogin(String login) {
        Query query = session.createSQLQuery("select * from persons where login = :login")
                .addEntity(Person.class);
        query.setParameter("login", login);
        List queryList = query.list();
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
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from current_workers;";
        Query query = session.createSQLQuery(sqlQuery)
                .addEntity(Person.class);
        List<Person> persons = query.list();
        if (persons == null) {
            throw new NullPointerException("DB returns null list");
        }
        transaction.commit();
        session.flush();
        return persons;
    }

    public void updatePersonPassword(Person person) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "update persons set password = :password where person_id = :id";
        Query query = session.createSQLQuery(sqlQuery);
        query.setParameter("password", person.getPassword());
        query.setParameter("id", person.getIdentifier());
        query.executeUpdate();
        transaction.commit();
        session.flush();
    }
}
