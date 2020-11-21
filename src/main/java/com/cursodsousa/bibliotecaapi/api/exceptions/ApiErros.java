package com.cursodsousa.bibliotecaapi.api.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;

public class ApiErros {

	private List<String> errors = new ArrayList<String>();

	public ApiErros(BindingResult bindingResult) {

		bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

	}

	public List<String> getErrors() {
		return errors;
	}

}
