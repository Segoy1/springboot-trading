package de.segoy.springboottradingdata.dao;

import de.segoy.springboottradingdata.ds.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentsDao extends CrudRepository<Department, Integer> {
}
