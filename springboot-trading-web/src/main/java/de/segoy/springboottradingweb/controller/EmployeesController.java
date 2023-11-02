package de.segoy.springboottradingweb.controller;

import de.segoy.springboottradingdata.dao.EmployeesDao;
import de.segoy.springboottradingdata.ds.Employee;
import de.segoy.springboottradingweb.security.annotations.employees.IsEmployeesCreate;
import de.segoy.springboottradingweb.security.annotations.employees.IsEmployeesDelete;
import de.segoy.springboottradingweb.security.annotations.employees.IsEmployeesRead;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeesController {

    private final EmployeesDao employeesDao;

    public EmployeesController(EmployeesDao employeesDao) {
        this.employeesDao = employeesDao;
    }

    @IsEmployeesRead
    @GetMapping("/employees")
    public ModelAndView index() {
        return new ModelAndView("employees", "employees", employeesDao.findAll());
    }

    @IsEmployeesCreate
    @GetMapping("/employees/create")
    public ModelAndView create() {
        return new ModelAndView("employee-create", "employee", new Employee());
    }

    @IsEmployeesCreate
    @PostMapping("/employees/create")
    public String create(@ModelAttribute @Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employee-create";
        } else {
            employeesDao.save(employee);

            return "redirect:/employees";
        }
    }

    @IsEmployeesDelete
    @GetMapping("/employees/delete/{id}")
    public String delete(@PathVariable Integer id) {
        employeesDao.deleteById(id);

        return "redirect:/employees";
    }
}
