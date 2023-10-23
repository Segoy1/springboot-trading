package de.segoy.springboottradingdata.dao;

import de.segoy.springboottradingdata.ds.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeesDao extends CrudRepository<Employee, Integer> {
}
