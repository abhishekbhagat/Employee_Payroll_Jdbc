package com.bridgelabz.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {

	public static void main(String[] args) throws EmployeePayrollServiceException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Abhi@1234";
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("cannot find driver in the classpath");
		}
		listDrivers();
		try {
			System.out.println("Connecting database" + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("Connection is established!!!!!!!" + connection);
		} catch (Exception e) {
			throw new EmployeePayrollServiceException("unable to load drivers",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_LOAD_DRIVER);
		}

	}

	public static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" " + driverClass.getClass().getName());
		}
	}
}
