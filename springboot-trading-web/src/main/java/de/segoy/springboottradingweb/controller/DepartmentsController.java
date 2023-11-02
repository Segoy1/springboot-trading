package de.segoy.springboottradingweb.controller;


import de.segoy.springboottradingdata.dao.DepartmentsDao;
import de.segoy.springboottradingdata.ds.Department;
import de.segoy.springboottradingweb.security.annotations.departments.IsDepartmentsCreate;
import de.segoy.springboottradingweb.security.annotations.departments.IsDepartmentsDelete;
import de.segoy.springboottradingweb.security.annotations.departments.IsDepartmentsRead;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DepartmentsController {

    private final DepartmentsDao departmentsDao;

    public DepartmentsController(DepartmentsDao departmentsDao) {
        this.departmentsDao = departmentsDao;
    }

    @IsDepartmentsRead
    @GetMapping("/departments")
    public ModelAndView index() {
        return new ModelAndView("departments", "departments", departmentsDao.findAll());
    }

    @IsDepartmentsCreate
    @GetMapping("/departments/create")
    public ModelAndView create() {
        return new ModelAndView("department-create", "department", new Department());
    }

    @IsDepartmentsCreate
    @PostMapping("/departments/create")
    public String create(@ModelAttribute @Valid Department department, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "department-create";
        } else {
            departmentsDao.save(department);

            return "redirect:/departments";
        }
    }

    @IsDepartmentsDelete
    @GetMapping("/departments/delete/{id}")
    public String delete(@PathVariable Integer id) {
        departmentsDao.deleteById(id);

        return "redirect:/departments";
    }
}
