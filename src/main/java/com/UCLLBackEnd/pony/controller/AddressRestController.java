package com.UCLLBackEnd.pony.controller;

import com.UCLLBackEnd.pony.model.Address;
import com.UCLLBackEnd.pony.model.DomainException;
import com.UCLLBackEnd.pony.model.Stable;
import com.UCLLBackEnd.pony.service.AddressService;
import com.UCLLBackEnd.pony.service.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addresses")
public class AddressRestController {

    private AddressService addressService;

    @Autowired
    public AddressRestController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public Address postAddress(@Valid @RequestBody Address address) {
        return addressService.addAddress(address);
    }

    @PostMapping("/stable")
    public Address postNewStableAtNewAddress(@Valid @RequestBody Stable stable) {
        return addressService.addNewStableToNewAddress(stable);
    }

    @PostMapping("/{addressId}/{stableid}")
    public Address postStableAtAddress(@PathVariable Long addressId, @PathVariable Long stableid) {
        return addressService.addStableToAddress(addressId, stableid);
    }

    @GetMapping("/stable/3Animals")
    public List<Address> getAddressesOfStableWithMoreThan3Animals() {
        return addressService.getAddressesOfStableWithMoreThan3Animals();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public Map<String, String> handleDomainExceptions(DomainException domainException) {
        Map<String, String> errors = new HashMap<>();
        errors.put(domainException.getField(), domainException.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public Map<String, String> handleServiceExceptions(ServiceException serviceException) {
        Map<String, String> errors = new HashMap<>();
        errors.put(serviceException.getField(), serviceException.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
