package by.htc.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
* Конфигурация Hibernate через hibernate.cfg.xml и создание соединения для работы с БД
*/
public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

