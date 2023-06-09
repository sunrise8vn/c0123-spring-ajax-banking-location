package com.cg.api;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.model.Customer;
import com.cg.model.dto.customer.*;
import com.cg.service.customer.ICustomerService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ValidateUtils validateUtils;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.findAll();

        List<CustomerDTO> customerDTOS = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDTO customerDTO = customer.toCustomerDTO();
            customerDTOS.add(customerDTO);
        }

        return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CustomerDTO customerDTO = customerOptional.get().toCustomerDTO();

        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerCreateReqDTO customerCreateReqDTO, BindingResult bindingResult) {

//        new CustomerCreateReqDTO().validate(customerCreateReqDTO, bindingResult);

//        if (bindingResult.hasFieldErrors()) {
//            return appUtils.mapErrorToResponse(bindingResult);
//        }

        Boolean existEmail = customerService.existsByEmail(customerCreateReqDTO.getEmail());

        if (existEmail) {
            throw new EmailExistsException("Email đã tồn tại");
        }

        Customer customer = customerCreateReqDTO.toCustomer(null, BigDecimal.ZERO);

        customer = customerService.create(customer);

        CustomerCreateResDTO customerCreateResDTO = customer.toCustomerCreateResDTO();

        return new ResponseEntity<>(customerCreateResDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CustomerUpdateReqDTO customerUpdateReqDTO, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(id)) {
            throw new DataInputException("Mã khách hàng không hợp lệ");
        }

        new CustomerUpdateReqDTO().validate(customerUpdateReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long customerId = Long.parseLong(id);

        Optional<Customer> customerOptional = customerService.findById(customerId);


        if (!customerOptional.isPresent()) {
            throw new DataInputException("Mã khách hàng không tồn tại");
        }

        Boolean existEmail = customerService.existsByEmailAndIdNot(customerUpdateReqDTO.getEmail(), customerId);

        if (existEmail) {
            throw new EmailExistsException("Email đã tồn tại");
        }

        Customer customer = customerUpdateReqDTO.toCustomer();
        Customer updateCustomer = customerOptional.get();

        customer.setId(updateCustomer.getId());

        customerService.save(customer);

        customer.setBalance(customerOptional.get().getBalance());

        CustomerUpdateResDTO customerUpdateResDTO = customer.toCustomerUpdateResDTO();

        return new ResponseEntity<>(customerUpdateResDTO, HttpStatus.OK);
    }

}
