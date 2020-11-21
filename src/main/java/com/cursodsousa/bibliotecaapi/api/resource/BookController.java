package com.cursodsousa.bibliotecaapi.api.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cursodsousa.bibliotecaapi.api.dto.BookDTO;
import com.cursodsousa.bibliotecaapi.api.exceptions.ApiErros;
import com.cursodsousa.bibliotecaapi.api.exceptions.BusinessException;
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
	public BookDTO salvar(@RequestBody @Valid BookDTO bookDTO) {

		Book book = modelMapper.map(bookDTO, Book.class);
		Book bookSaved = bookService.save(book);

		return modelMapper.map(bookSaved, BookDTO.class);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public BookDTO obter(@PathVariable Long id) {

		return bookService.getById(id)
				.map(book -> modelMapper.map(book, BookDTO.class))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {

		Book book = bookService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		bookService.delete(book);

	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public BookDTO actualizar(@RequestBody @Valid BookDTO bookDTO, @PathVariable Long id) {

		return bookService.getById(id).map( book -> {

			book.setAuthor(bookDTO.getAuthor());
			book.setTitle(bookDTO.getTitle());
			book = bookService.update(book);
			return modelMapper.map(book, BookDTO.class);

		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErros handleValidationExceptions(MethodArgumentNotValidException exception) {

		BindingResult bindingResult = exception.getBindingResult();
		return new ApiErros(bindingResult);
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErros handleBusinessException(BusinessException exception) {

		return new ApiErros(exception);
	}

}
