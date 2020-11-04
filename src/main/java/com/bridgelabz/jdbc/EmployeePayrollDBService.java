package com.bridgelabz.jdbc;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;

	private EmployeePayrollDBService() {
	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	public List<EmployeePayrollData> readData() throws EmployeePayrollServiceException {
		String sql = "SELECT * FROM employee_payroll;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			EmployeePayrollData employeeData = null;
			while (result.next()) {
				int empId = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeeData = new EmployeePayrollData(empId, name, salary, startDate);
				employeePayrollList.add(employeeData);
			}
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("Not able to read data from employee_payroll",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_READ);
		}
		return employeePayrollList;
	}

	private Connection getConnection() throws EmployeePayrollServiceException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Abhi@1234";
		Connection connection = null;
		try {
			System.out.println("Connecting database" + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("Connection is established!!!!!!!" + connection);
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to create connection",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_LOAD_DRIVER);
		}
		return connection;
	}

	public int updateEmployeeSalary(String name, double salary) throws EmployeePayrollServiceException {
		String query = String.format("UPDATE  employee_payroll set salary ='%.2f' WHERE name='%s';", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(query);
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to update",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_UPDATE);

		}

	}

	public int updateEmployeeSalaryByPreparedStatement(String name, double salary)
			throws EmployeePayrollServiceException, SQLException {
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection
					.prepareStatement("UPDATE  employee_payroll set salary =? WHERE name=?");
			statement.setDouble(1, salary);
			statement.setString(2, name);
			return statement.executeUpdate();
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to update",
					EmployeePayrollServiceException.ExceptionType.UNABE_TO_UPDATE_USING_PREPARED_STATEMENT);
		}
	}

	public List<EmployeePayrollData> retrievePayrollDataByName(String name)
			throws EmployeePayrollServiceException, SQLException {
		try (Connection connection = this.getConnection()) {
			List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee_payroll WHERE name=?");
			statement.setString(1, name);
			ResultSet result = statement.executeQuery();
			employeePayrollList = getEmployeePayrollData(result);
			return employeePayrollList;
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to update",
					EmployeePayrollServiceException.ExceptionType.UNABE_TO_UPDATE_USING_PREPARED_STATEMENT);
		}
	}

	public List<EmployeePayrollData> retrievePayrollDataByDateRange(String startDate, String endDate)
			throws EmployeePayrollServiceException, SQLException {
		try (Connection connection = this.getConnection()) {
			List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
			PreparedStatement statement = connection
					.prepareStatement("select * from employee_payroll where start between ? and date ? ");
			statement.setString(1, startDate);
			statement.setString(2, endDate);
			ResultSet result = statement.executeQuery();
			employeePayrollList = getEmployeePayrollData(result);
			return employeePayrollList;
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to update",
					EmployeePayrollServiceException.ExceptionType.UNABE_TO_UPDATE_USING_PREPARED_STATEMENT);
		}
	}

	public List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) throws SQLException {
		EmployeePayrollData employeeData = null;
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		while (result.next()) {
			int empId = result.getInt("id");
			double salary = result.getDouble("salary");
			LocalDate startDate = result.getDate("start").toLocalDate();
			String name = result.getString("name");
			employeeData = new EmployeePayrollData(empId, name, salary, startDate);
			employeePayrollList.add(employeeData);
		}
		return employeePayrollList;
	}

	public Double calculateSum() throws SQLException, EmployeePayrollServiceException {
		try (Connection connection = this.getConnection()) {
			Double sum = 0d;
			List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
			PreparedStatement statement = connection.prepareStatement(
					"SELECT sum(salary) as sum  FROM  employee_payroll WHERE gender ='M' GROUP BY gender");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				sum = result.getDouble("sum");
			}
			return sum;
		}
	}

	public Map<String, Double> getAverageSalaryByGender() throws EmployeePayrollServiceException {
		String sql = "SELECT gender,avg(salary) as avg  FROM  employee_payroll  GROUP BY gender;";
		Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
		try (Connection connection = this.getConnection()) {
			List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String gender = result.getString("gender");
				double salary = result.getDouble("avg");
				genderToAverageSalaryMap.put(gender, salary);
			}
			return genderToAverageSalaryMap;
		} catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to update",
					EmployeePayrollServiceException.ExceptionType.UNABE_TO_CALCULATE_AVG);
		}
	}
}
