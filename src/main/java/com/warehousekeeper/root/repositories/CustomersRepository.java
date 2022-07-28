package com.warehousekeeper.root.repositories;

import com.warehousekeeper.root.models.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByFullName(String name);

    List<Customer> findByFullNameStartingWith(String origin);


}
