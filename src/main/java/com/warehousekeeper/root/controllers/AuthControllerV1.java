package com.warehousekeeper.root.controllers;



import com.warehousekeeper.root.dto.AuthenticationDTO;
import com.warehousekeeper.root.dto.PersonDTO;
import com.warehousekeeper.root.models.Person;
import com.warehousekeeper.root.security.JWTUtil;
import com.warehousekeeper.root.services.RegistrationService;
import com.warehousekeeper.root.util.ErrorResponse;
import com.warehousekeeper.root.util.NotCreatedException;
import com.warehousekeeper.root.util.PersonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final JWTUtil jwtUtil;
    private  final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthControllerV1(RegistrationService registrationService, PersonValidator personValidator, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager1;
    }

/*    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("message", "Error!");
        }

        registrationService.register(person);

        String getToken = jwtUtil.generateToken(person.getUsername());

        return Map.of("jwt-token", getToken);
    }*/

    @PostMapping("registration")
    public ResponseEntity<HttpStatus> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                          BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError fe : errors) {
                stringBuilder.append(fe.getField()).append(" - ")
                        .append(fe.getDefaultMessage())
                        .append("; ");
            }
            throw new NotCreatedException(stringBuilder.toString());
        }

        registrationService.register(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
        //return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(NotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage()
                ,new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400

    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
