package com.warehousekeeper.root.util;

import com.warehousekeeper.root.models.Person;
import com.warehousekeeper.root.repositories.PeopleRepository;
import com.warehousekeeper.root.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PersonValidator implements Validator {

    private final PeopleRepository peopleRepository;
    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleRepository.findByUsername(person.getUsername()).isPresent())
            errors.rejectValue("username","","This username is exist");
    }
}
