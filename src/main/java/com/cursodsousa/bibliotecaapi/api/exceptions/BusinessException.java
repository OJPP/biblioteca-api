package com.cursodsousa.bibliotecaapi.api.exceptions;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 7248464133987638004L;

	public BusinessException(String message) {
		super(message);
	}

}
