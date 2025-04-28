package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.management.Query;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public void create(User user) {
        try (Session session = HibernateFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = HibernateFactoryUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = HibernateFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = HibernateFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) session.delete(user);
            tx.commit();
        }
    }
}
