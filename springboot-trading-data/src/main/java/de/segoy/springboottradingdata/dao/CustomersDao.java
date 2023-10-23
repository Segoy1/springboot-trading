package de.segoy.springboottradingdata.dao;

import de.segoy.springboottradingdata.ds.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomersDao extends CrudRepository<Customer, Integer> {
}
