package com.cursodsousa.bibliotecaapi.api.resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cursodsousa.bibliotecaapi.api.dto.BookDTO;
import com.cursodsousa.bibliotecaapi.api.service.BookService;
import com.cursodsousa.bibliotecaapi.model.entity.Book;

@RestController
@RequestMapping(value = "/api/books")
public class BookController {

	private BookService bookService;

	@Autowired
	private ModelMapper modelMapper;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO salvar(@RequestBody BookDTO bookDTO) {

		Book book = modelMapper.map(bookDTO, Book.class);
		Book bookSaved = bookService.save(book);

		return modelMapper.map(bookSaved, BookDTO.class);
	}

}
