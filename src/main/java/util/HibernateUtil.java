package util;

import model.UserType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static void buildSessionFactory(UserType user) {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");
        configuration.setProperty("connection.username", user.getUserName());
        configuration.setProperty("connection.password", user.getPassword());
        configuration.setProperty("hibernate.connection.password", user.getPassword());
        configuration.setProperty("hibernate.connection.username", user.getUserName());
        StandardServiceRegistryBuilder serviceBuilder =
                new StandardServiceRegistryBuilder().applySettings(
                        configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(
                serviceBuilder.build());
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutDown() {
        sessionFactory.close();
    }
}
