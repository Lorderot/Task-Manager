package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

import java.sql.Connection;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static void buildSessionFactory(String user, String password) {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");
        configuration.setProperty("connection.username", user);
        configuration.setProperty("connection.password", password);
        configuration.setProperty("hibernate.connection.username", user);
        configuration.setProperty("hibernate.connection.password", password);
        StandardServiceRegistryBuilder serviceBuilder =
                new StandardServiceRegistryBuilder().applySettings(
                        configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(
                serviceBuilder.build());
    }

    public static Connection getConnection(Session session) {
        return ((SessionImpl)session).connection();
    }

    public static boolean isFactoryClosed() {
        return sessionFactory == null;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
