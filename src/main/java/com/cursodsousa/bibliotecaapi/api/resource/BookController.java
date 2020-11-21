package com.cursodsousa.bibliotecaapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cursodsousa.bibliotecaapi.api.dto.BookDTO;

@RestController
@RequestMapping(value = "/api/books")
public class BookController {

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO salvar() {

		BookDTO bookDTO = new BookDTO();
		bookDTO.setId(1l);
		bookDTO.setTitle("Meu livro");
		bookDTO.setAuthor("Autor");
		bookDTO.setIsbn("1213212");

		return bookDTO;

	}

}
