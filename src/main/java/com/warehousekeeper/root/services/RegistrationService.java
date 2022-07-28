package com.warehousekeeper.root.services;

import com.warehousekeeper.root.models.Person;
import com.warehousekeeper.root.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person newPerson){
        String encodedPassword = passwordEncoder.encode(newPerson.getPassword());
        newPerson.setPassword(encodedPassword);
        newPerson.setRole("ROLE_USER");
        peopleRepository.save(newPerson);
    }
}
