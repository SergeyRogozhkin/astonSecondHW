package org.example.dao;

import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
public class UserDAOImplIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("user_service")
            .withUsername("postgres")
            .withPassword("admin");

    private SessionFactory sessionFactory;
    private UserDAO userDAO;

    private Session session;
    private Transaction transaction;

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    @BeforeEach
    void setup() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate-test.cfg.xml");

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());

        sessionFactory = configuration.buildSessionFactory();
        userDAO = new UserDAOImpl();

        startTransaction();
    }

    @AfterEach
    void tearDown() {
        rollbackTransaction();
        sessionFactory.close();
    }

    private void startTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    private void rollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            System.out.println("Rolling back transaction...");
            transaction.rollback();
        }
        if (session != null && session.isOpen()) {
            System.out.println("Closing session...");
            session.close();
        }
    }

    @Test
    void testCreateAndGetById() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAge(25);

        userDAO.create(user);

        User savedUser = userDAO.getById(user.getId());

        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
    }

    @Test
    void testGetAll() {
        User user1 = new User();
        user1.setName("Bob");
        userDAO.create(user1);

        User user2 = new User();
        user2.setName("Charlie");
        userDAO.create(user2);

        List<User> users = userDAO.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setName("Eve");
        userDAO.create(user);

        user.setName("Updated Eve");
        userDAO.update(user);

        User updatedUser = userDAO.getById(user.getId());

        assertEquals("Updated Eve", updatedUser.getName());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("Deleted User");
        userDAO.create(user);

        userDAO.delete(user.getId());

        User deletedUser = userDAO.getById(user.getId());

        assertNull(deletedUser);
    }
}
