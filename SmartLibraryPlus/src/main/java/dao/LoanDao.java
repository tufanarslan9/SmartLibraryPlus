package dao;

import entity.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class LoanDao {

    public void save(Loan loan) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.save(loan);

        tx.commit();
        session.close();
    }

    public void update(Loan loan) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.update(loan);

        tx.commit();
        session.close();
    }

    public void delete(Loan loan) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.delete(loan);

        tx.commit();
        session.close();
    }

    public Loan getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Loan loan = session.get(Loan.class, id);
        session.close();
        return loan;
    }

    public List<Loan> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Loan> list = session.createQuery("from Loan", Loan.class).list();
        session.close();
        return list;
    }
}
