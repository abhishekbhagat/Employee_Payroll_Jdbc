package com.bridgelabz.jdbc;

public class EmployeePayrollServiceException extends Exception {
	enum ExceptionType {
		UNABLE_TO_LOAD_DRIVER, UNABE_TO_UPDATE_USING_PREPARED_STATEMENT, UNABLE_TO_UPDATE, UNABLE_TO_READ,
		UNABLE_TO_RETRIEVE_USING_PREPARED_STATEMENT, UNABE_TO_CALCULATE_AVG;
	}

	ExceptionType type;

	public EmployeePayrollServiceException(String message, ExceptionType type) {
		super(message);
		type = this.type;
	}
}
