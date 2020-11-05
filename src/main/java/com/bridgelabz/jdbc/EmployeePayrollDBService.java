package com.bridgelabz.jdbc;

import java.sql.Connection;
import java.sql.Date;
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
		String sql = "select * from employee";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			EmployeePayrollData employeeData = null;
			while (result.next()) {
				int empId = result.getInt("emp_id");
				String name = result.getString("name");
				String address = result.getString("address");
				String gender= result.getString("gender");
				double salary= result.getDouble("salary");
				int company_Id=result.getInt("company_id");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeeData = new EmployeePayrollData(empId, name,address,gender,salary,company_Id, startDate);
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
		String query = String.format("update payroll set net_pay = '%s' where emp_id = (select emp_id from employee where name = '%s')", salary, name);
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
					.prepareStatement("update payroll set net_pay = ? where emp_id = (select emp_id from employee where name = ?");
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
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM payroll where emp_id = (select emp_id from employee where name = ?");
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
					.prepareStatement("select * from employee where start between ? and ? ");
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
			int empId = result.getInt("emp_id");
			String name = result.getString("name");
			String address = result.getString("address");
			String gender= result.getString("gender");
			double salary= result.getDouble("salary");
			int company_Id=result.getInt("company_id");
			LocalDate startDate = result.getDate("start").toLocalDate();
			employeeData = new EmployeePayrollData(empId, name,address,gender,salary,company_Id, startDate);
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
	public EmployeePayrollData addEmployeeToPayroll(int emp_Id,String companyName,int company_Id,int dept_id,String dept_Name,String name, double salary, String address, LocalDate startDate,
			String gender) throws EmployeePayrollServiceException, SQLException {
		EmployeePayrollData employeePayrollData = null;
		Connection connection = null;
		connection = this.getConnection();

		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO employee(emp_id,name,address,gender,start,start,company_id) " + " VALUES('%s','%s','%s','%s','%s','%s')",
					emp_Id, name,address,gender, Date.valueOf(startDate),salary);
			int rowAffected = statement.executeUpdate(sql);
			List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
			employeePayrollData =new EmployeePayrollData(emp_Id, name,address,gender,salary,company_Id, startDate);
			if (rowAffected == 1) {
			}
		} catch (SQLException e) {
	
			throw new EmployeePayrollServiceException("unable to update",
					EmployeePayrollServiceException.ExceptionType.UNABE_TO_CALCULATE_AVG);
		}
		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"INSERT INTO payroll(basic_pay,taxable_pay,income_tax,deductions,emp_id) "
							+ " VALUES('%s','%s','%s','%s','%s')",
					salary,taxablePay,tax, deductions,emp_Id);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
		
			}

		} catch (SQLException e) {
	
			throw new EmployeePayrollServiceException("unable to add new employee",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_ADD_NEW_EMPLOYEE);
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO company(company_id,company_name) "+ " VALUES('%s','%s')",company_Id,companyName);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
			}
		}
		catch (SQLException e) {
		
			throw new EmployeePayrollServiceException("unable to add new employee",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_ADD_NEW_EMPLOYEE);
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO department(dept_id,dept_name) "+ " VALUES('%s','%s')",dept_id,dept_Name);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to add new employee",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_ADD_NEW_EMPLOYEE);
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO  employee_department(emp_id,dept_id) "+ " VALUES('%s','%s')",emp_Id,dept_id);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollServiceException("unable to add new employee",
					EmployeePayrollServiceException.ExceptionType.UNABLE_TO_ADD_NEW_EMPLOYEE);
		}
		
		finally {
			if (connection != null)
				connection.close();
		}
		return employeePayrollData;

	}
}
