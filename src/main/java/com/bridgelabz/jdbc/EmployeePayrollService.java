package com.bridgelabz.jdbc;

import java.sql.SQLException;
import java.util.List;

public class EmployeePayrollService {
	private List<EmployeePayrollData> employeePayrollList;

	public List<EmployeePayrollData> readEmployeePayroll() throws EmployeePayrollServiceException {
		return this.employeePayrollList = EmployeePayrollDBService.getInstance().readData();
	}

	public Boolean updateEmployeeSalary(String name, double salary) throws EmployeePayrollServiceException {
		int result = EmployeePayrollDBService.getInstance().updateEmployeeSalary(name, salary);
		if (result == 0)
			return false;
		else
			return true;
	}

	public Boolean updateEmployeeSalaryByPreparedStatement(String name, double salary)
			throws EmployeePayrollServiceException, SQLException {
		int result = EmployeePayrollDBService.getInstance().updateEmployeeSalaryByPreparedStatement(name, salary);
		if (result == 0)
			return false;
		else
			return true;
	}

	public List<EmployeePayrollData> readEmployeePayrollDataByPreparedStatement(String name)
			throws EmployeePayrollServiceException, SQLException {
		return this.employeePayrollList = EmployeePayrollDBService.getInstance().retrievePayrollDataByName(name);
	}

	public List<EmployeePayrollData> readEmployeePayrollDataByDateRange(String startDate, String endDate)
			throws EmployeePayrollServiceException, SQLException {
		return this.employeePayrollList = EmployeePayrollDBService.getInstance()
				.retrievePayrollDataByDateRange(startDate, endDate);
	}
}
