package com.bridgelabz.jdbc;

import java.util.List;

public class EmployeePayrollService {
	private List<EmployeePayrollData> employeePayrollList;

	public List<EmployeePayrollData> readEmployeePayroll() {
		return this.employeePayrollList = new EmployeePayrollDBService().readData();
	}
}
