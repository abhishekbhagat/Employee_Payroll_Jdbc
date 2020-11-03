package com.bridgelabz.jdbc;

import java.util.List;

public class EmployeePayrollService {
	private List<EmployeePayrollData> employeePayrollList;

	public List<EmployeePayrollData> readEmployeePayroll() {
		return this.employeePayrollList = new EmployeePayrollDBService().readData();
	}

	public Boolean updateEmployeeSalary(String name, double salary) {
		int result = new EmployeePayrollDBService().updateEmployeeSalary(name, salary);
		if (result == 0)
			return false;
		else
			return true;
	}
}
