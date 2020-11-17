package com.cursodsousa.bibliotecaapi.api.resource;

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

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO salvar(@RequestBody BookDTO bookDTO) {

		Book book = Book.builder().title(bookDTO.getTitle()).author(bookDTO.getAuthor()).isbn(bookDTO.getIsbn()).build();
		
		Book bookSaved = bookService.save(book);

		BookDTO bookSavedDTO = BookDTO.builder().id(bookSaved.getId()).title(bookSaved.getTitle()).author(bookSaved.getAuthor()).isbn(bookSaved.getIsbn()).build();

		return bookSavedDTO;

	}

}
