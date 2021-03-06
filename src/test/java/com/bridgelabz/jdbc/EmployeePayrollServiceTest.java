package com.bridgelabz.jdbc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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

	@Test
	public void givenDatabase_WhenAverageSalaryRetrievedByGender_ShouldReturn_CorrectValue()
			throws EmployeePayrollServiceException, SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayroll();
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender();

		Assert.assertTrue(
				averageSalaryByGender.get("M").equals(20000.00) && averageSalaryByGender.get("F").equals(93039.00));
	}

	public void givenDatabase_WhenAddedNewEmployeeAlsoInPayrollDetails_ShouldSyncWith()
			throws EmployeePayrollServiceException, SQLException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayroll();
		employeePayrollService.addNewEmployeeToPayroll(7, "Capgemini", 410, 1001, "cse", "rahul", 30200, "katras",
				LocalDate.now(), "M");

		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDb("rahul");
		Assert.assertTrue(result);
	}

}
