package com.warehousekeeper.root.util;

import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.services.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class CustomerValidator implements Validator {

    private final CustomersService customersService;
    @Autowired
    public CustomerValidator(CustomersService customersService) {
        this.customersService = customersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;

        if(customersService.findCustomerByFullName(customer.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "This customer is exist");
    }
}
