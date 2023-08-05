package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private EmployeeService employeeService;

	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	// add mapping for "/list"
	@GetMapping("/list")
	public String listEmployees(Model theModel) {

		// add to the spring model
		theModel.addAttribute("employees", employeeService.findAll());

		return "employees/list-employees";
	}

	@GetMapping("/showFormForAdd")
	public String showFromForAdd(Model theModel){

		Employee theEmployee=new Employee();

		theModel.addAttribute("employee",theEmployee);
		return "employees/employee-form";

	}

	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Employee theEmployee){
		employeeService.save(theEmployee);
		return "redirect:/employees/list";
	}


	@GetMapping("/showFormForUpdate")
	public String showFromForUpdate(@RequestParam("employeeId") int theId,
												Model theModel){

		Employee theEmployee=employeeService.findById(theId);

		theModel.addAttribute("employee",theEmployee);

		return  "employees/employee-form";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") int theId){

		employeeService.deleteById(theId);

		return "redirect:/employees/list";

	}


}









