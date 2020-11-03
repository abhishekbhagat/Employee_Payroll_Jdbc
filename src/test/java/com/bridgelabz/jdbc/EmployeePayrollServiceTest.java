package com.bridgelabz.jdbc;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollServiceTest {

	@Test
	public void givenDatabase_WhenRetrievePayroll_ShouldMatchEmployeeCount() throws EmployeePayrollServiceException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayroll();

		Assert.assertEquals(4, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDb() throws EmployeePayrollServiceException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayroll();
		Assert.assertEquals(true, employeePayrollService.updateEmployeeSalary("Terisa", 3000000.0));
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDb()
			throws EmployeePayrollServiceException, SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayroll();
		Assert.assertEquals(true, employeePayrollService.updateEmployeeSalaryByPreparedStatement("Terisa", 3000000.0));
	}
}
