package DAO;

import model.Person;
import org.hibernate.Query;
import org.hibernate.Session;
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
}
