package com.bridgelabz.jdbc;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollData {
	private int emp_id;
	private String name;
	private double salary;
	private LocalDate start;
	private String gender;
	private int company_Id;
    private String address;
    private List<String> deptName;
    public void setAddress(String address) {
    	this.address=address;
    }
	public int getCompany_Id() {
		return company_Id;
	}

	public void setCompany_Id(int company_Id) {
		this.company_Id = company_Id;
	}

	public List<String> getDeptName() {
		return deptName;
	}

	public void setDeptName(List<String> deptName) {
		this.deptName = deptName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public EmployeePayrollData(int emp_id, String name, String address, String gender, double salary, int company_Id,
			LocalDate start) {
		this.address=address;
		this.emp_id = emp_id;
		this.name = name;
		this.salary = salary;
		this.start = start;
		this.gender=gender;
	}

}
