package com.warehousekeeper.root.dao;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.models.Storage;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class StorageDao {
    private final EntityManager entityManager;

    @Autowired
    public StorageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Customer findCustomerByStorageId(int id) {
        Session session = entityManager.unwrap(Session.class);
        Storage storage = session.get(Storage.class, id);
            if (storage != null) {
                Customer customer = storage.getOwner();
                if (customer != null) {
                    Hibernate.initialize(customer.getStorages());
                    return customer;
                }
            }
            return null;
        }

}
