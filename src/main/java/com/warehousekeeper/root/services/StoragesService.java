package com.warehousekeeper.root.services;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.models.Storage;
import com.warehousekeeper.root.repositories.StoragesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StoragesService {

    private final StoragesRepository storagesRepository;
    @Autowired
    public StoragesService(StoragesRepository storagesRepository) {
        this.storagesRepository = storagesRepository;
    }



    public Page<Storage> findAllWithPages(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber - 1,10, Sort.by("size"));
        return storagesRepository.findAll(pageable);
    }

    public List<Storage> findAll() {
        return storagesRepository.findAll(Sort.by("size"));
    }


    public List<Storage> findStoragesWithPagination(Integer page, Integer storagesPerPage, boolean sortBySize){
        if(sortBySize){
            return storagesRepository.findAll(PageRequest.of(page, storagesPerPage, Sort.by("size")))
                    .getContent();
        }
        return storagesRepository.findAll(PageRequest.of(page,storagesPerPage)).getContent();
    }

    public Storage findStorage(int id){
        Optional<Storage> foundStorage = storagesRepository.findById(id);
        return foundStorage.orElse(null);
    }
    @Transactional
    public void createNewStorage(Storage storage) {
        storagesRepository.save(storage);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMINE')")
    public void update(int id, Storage storageToUpdate){
        Storage storageToBeUpdated = storagesRepository.findById(id).get();

        storageToUpdate.setOwner(storageToBeUpdated.getOwner());
        storageToUpdate.setTakenAt(storageToBeUpdated.getTakenAt());
        storageToUpdate.setId(id);
        storagesRepository.save(storageToUpdate);
    }
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(int id) {
        storagesRepository.deleteById(id);
    }

    public Customer getStorageOwner(int id){
        return storagesRepository.findById(id).map(Storage::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id){
        storagesRepository.findById(id).ifPresent(storage -> {
                                                storage.setOwner(null);
                                                storage.setTakenAt(null);
        });

    }
    @Transactional
    public void assign(int id, Customer selectCustomer){
        storagesRepository.findById(id).ifPresent(storage -> {
                                                  storage.setOwner(selectCustomer);
                                                  storage.setTakenAt(new Date());
        });
    }


}
