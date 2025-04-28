package org.example.util;

import org.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateFactoryUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties props = new Properties();
            props.load(HibernateFactoryUtil.class.getClassLoader().getResourceAsStream("hibernate.properties"));
            Configuration configuration = new Configuration();
            configuration.setProperties(props);
            configuration.addAnnotatedClass(User.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}