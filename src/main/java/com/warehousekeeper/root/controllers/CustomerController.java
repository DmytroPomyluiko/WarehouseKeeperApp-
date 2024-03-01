package com.warehousekeeper.root.controllers;

import com.warehousekeeper.root.dao.CustomerDao;
import com.warehousekeeper.root.dto.CustomerDto;
import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.services.CustomersService;
import com.warehousekeeper.root.services.StoragesService;
import com.warehousekeeper.root.util.CustomerValidator;
import com.warehousekeeper.root.util.ErrorResponse;
import com.warehousekeeper.root.util.NotCreatedException;
import com.warehousekeeper.root.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomersService customersService;
    private final CustomerValidator customerValidator;
    private final StoragesService storagesService;
    private final CustomerDao customerDao;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomersService customersService, CustomerValidator customerValidator,
                              StoragesService storagesService, CustomerDao customerDao, ModelMapper modelMapper) {
        this.customersService = customersService;
        this.customerValidator = customerValidator;
        this.storagesService = storagesService;
        this.customerDao = customerDao;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public List<Customer> showAllCustomer() {
        return customerDao.index();
    }


    @GetMapping("/{id}")
    public Customer findCustomerById(@PathVariable("id") int id) {
        return customerDao.findById(id);
    }


    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CustomerDto customerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = getExceptionMessage(bindingResult);
            throw new NotCreatedException(stringBuilder.toString());
        }
        customersService.createNewCustomer(convertToPerson(customerDto));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid CustomerDto customerDto, BindingResult bindingResult,
                                             @PathVariable("id") int id) {
        customerValidator.validate(convertToPerson(customerDto), bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = getExceptionMessage(bindingResult);
            throw new NotCreatedException(stringBuilder.toString());
        }

        customersService.update(id,convertToPerson(customerDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        customersService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerExceptionNotCreatedCustomer(NotCreatedException e) {
        ErrorResponse personErrorResponse =
                new ErrorResponse(e.getMessage(), new Date());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerExceptionNotFoundCustomer(NotFoundException e) {
        ErrorResponse personErrorResponse =
                new ErrorResponse("Customer with this id/name was not found", new Date());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }

    @PostMapping ("/search")
    public ResponseEntity<List<Customer>> makeSearch(@RequestParam("query") String query) {
        List<Customer> customers = customerDao.findByFirstTwoLettersIgnoreCase(query);
        if (customers.size() == 0)
            throw new NotFoundException();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    protected static StringBuilder getExceptionMessage(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError fr : errors) {
        stringBuilder.append(fr.getField()).append(" - ").append(fr.getDefaultMessage())
                .append(";");
    }
        return stringBuilder;
}
    private Customer convertToPerson(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }
}
