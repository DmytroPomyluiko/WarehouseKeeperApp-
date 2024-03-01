package com.warehousekeeper.root.controllers;

import com.warehousekeeper.root.dao.StorageDao;
import com.warehousekeeper.root.dto.StorageDto;
import com.warehousekeeper.root.models.Customer;
import com.warehousekeeper.root.models.Storage;
import com.warehousekeeper.root.services.CustomersService;
import com.warehousekeeper.root.services.StoragesService;
import com.warehousekeeper.root.util.ErrorResponse;
import com.warehousekeeper.root.util.NotCreatedException;
import com.warehousekeeper.root.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.warehousekeeper.root.controllers.CustomerController.getExceptionMessage;

@RestController
@RequestMapping("/api/v1/storages")
public class StorageController {
    private final StoragesService storagesService;
    private final StorageDao storageDao;
    private final ModelMapper modelMapper;
    private final CustomersService customersService;

    @Autowired
    public StorageController(StoragesService storagesService, StorageDao storageDao, ModelMapper modelMapper, CustomersService customersService) {
        this.storagesService = storagesService;
        this.storageDao = storageDao;
        this.modelMapper = modelMapper;
        this.customersService = customersService;
    }

    @GetMapping("")
    public ResponseEntity<List<Storage>> index() {
        return new ResponseEntity<>(storagesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findStorageById(@PathVariable("id") int id) {
        Storage storage = storagesService.findStorage(id);
        Customer customer = storageDao.findCustomerByStorageId(id);
        if (customer != null) {
            return ResponseEntity.ok(Map.of(storage, customer));
        } else {
            return ResponseEntity.ok(List.of(storage));
        }
    }


    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid StorageDto storageDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = getExceptionMessage(bindingResult);
            throw new NotCreatedException(stringBuilder.toString());
        }
        storagesService.createNewStorage(convertToStorage(storageDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid StorageDto storageDto, BindingResult bindingResult,
                                             @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = getExceptionMessage(bindingResult);
            throw new NotCreatedException(stringBuilder.toString());
        }
        storagesService.update(id, convertToStorage(storageDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        storagesService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    // ex - http://localhost:7070/api/storages/8/assign?id_customer=3
    @PatchMapping("/{id}/assign")
    public ResponseEntity<HttpStatus> assign(@PathVariable ("id") int id, @RequestParam ("id_customer") int id_customer){
        Customer customer = customersService.findById(id_customer);
        storagesService.assign(id, customer);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}/release")
    public ResponseEntity<HttpStatus> release(@PathVariable ("id") int id){
        storagesService.release(id);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerExceptionNotStorage(NotFoundException e) {
        ErrorResponse storageErrorResponse =
                new ErrorResponse("Storage with this id was not found", new Date());
        return new ResponseEntity<>(storageErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerExceptionNotCreatedStorage(NotCreatedException e) {
        ErrorResponse storageErrorResponse =
                new ErrorResponse(e.getMessage(), new Date());
        return new ResponseEntity<>(storageErrorResponse, HttpStatus.BAD_REQUEST);
    }
    private Storage convertToStorage(StorageDto storageDto) {
        return modelMapper.map(storageDto, Storage.class);
    }

}
