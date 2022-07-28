package com.warehousekeeper.root.repositories;

import com.warehousekeeper.root.models.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoragesRepository extends JpaRepository<Storage,Integer> {
}
