package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import com.cg.service.IGeneralService;


public interface ICustomerService extends IGeneralService<Customer, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdNot(String email, Long id);

    Customer create(Customer customer);

    Customer update(Customer customer, LocationRegion oldLocationRegion);
}
