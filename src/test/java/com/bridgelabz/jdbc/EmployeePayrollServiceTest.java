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
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWitnhDb() throws EmployeePayrollServiceException {
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

	@Test
	public void givenDatabase_WhenRetrievePayrollByNameUsingPreparedStatement_ShouldMatchEmployeeCount()
			throws EmployeePayrollServiceException, SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollDataByPreparedStatement("Teria");
		System.out.println(employeePayrollData.size());
		Assert.assertEquals(2, employeePayrollData.size());
	}

	@Test
	public void givenDatabase_WhenRetrievePayrollByGivenDateRange_ShouldMatchEmployeeCount()
			throws EmployeePayrollServiceException, SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollDataByDateRange("2018-01-03", "2018-12-20");
		System.out.println(employeePayrollData.size());
		Assert.assertEquals(3, employeePayrollData.size());
	}

}
