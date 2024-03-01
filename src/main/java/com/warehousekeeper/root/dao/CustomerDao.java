package com.warehousekeeper.root.dao;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.util.NotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Component
public class CustomerDao {
    private final EntityManager entityManager;

    public CustomerDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional(readOnly = true)
    public List<Customer> index(){
        Session session = entityManager.unwrap(Session.class);
        List<Customer> customers = session.createQuery("SELECT c from Customer c LEFT JOIN FETCH c.storages",
                Customer.class).getResultList();
        return customers;

    }

    @Transactional(readOnly = true)
    public Customer findById(int id){
        Session session = entityManager.unwrap(Session.class);
        try {
           Customer customer = session.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.storages WHERE c.id="+ id,
                    Customer.class).getSingleResult();
           return customer;
        }catch (NoResultException e){
            throw new NotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<Customer> findByFirstTwoLettersIgnoreCase(String letters){
        Session session = entityManager.unwrap(Session.class);
        Query<Customer> query = session.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.storages WHERE lower(c.fullName) LIKE lower(:letters)", Customer.class);
        query.setParameter("letters", letters + "%");
        List<Customer> customers = query.getResultList();
        return customers;
    }



}
