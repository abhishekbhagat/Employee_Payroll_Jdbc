package com.bridgelabz.jdbc;

public class EmployeePayrollServiceException extends Exception {
	enum ExceptionType {
		UNABLE_TO_LOAD_DRIVER;
	}

	ExceptionType type;

	public EmployeePayrollServiceException(String message, ExceptionType type) {
		super(message);
		type = this.type;
	}

}
