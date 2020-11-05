package com.bridgelabz.jdbc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public Double calculateSum() throws SQLException, EmployeePayrollServiceException {
		return EmployeePayrollDBService.getInstance().calculateSum();
	}

	public Map<String, Double> readAverageSalaryByGender() throws EmployeePayrollServiceException, SQLException {
		return EmployeePayrollDBService.getInstance().getAverageSalaryByGender();
	}

	public void addNewEmployeeToPayroll(int id, String name, double salary, LocalDate startDate, String gender)
			throws EmployeePayrollServiceException, SQLException {
		employeePayrollList
				.add(EmployeePayrollDBService.getInstance().addEmployeeToPayroll(id, name, salary, startDate, gender));

	}

	public void addNewEmployeeToPayrollUc8(int id, String name, double salary, LocalDate startDate, String gender)
			throws EmployeePayrollServiceException, SQLException {
		employeePayrollList.add(
				EmployeePayrollDBService.getInstance().addEmployeeToPayrollUc8(id, name, salary, startDate, gender));

	}

	public boolean checkEmployeePayrollInSyncWithDb(String name) {
		List<EmployeePayrollData> employee = employeePayrollList.stream()
				.filter(employeepayroll -> employeepayroll.getName().equals(name)).collect(Collectors.toList());
		if (employee.isEmpty())
			return false;
		else
			return true;
	}
}
