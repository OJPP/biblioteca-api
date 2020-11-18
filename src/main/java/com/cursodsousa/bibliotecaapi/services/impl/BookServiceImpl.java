package com.cursodsousa.bibliotecaapi.services.impl;

import org.springframework.stereotype.Service;

import com.cursodsousa.bibliotecaapi.api.service.BookService;
import com.cursodsousa.bibliotecaapi.model.entity.Book;
import com.cursodsousa.bibliotecaapi.model.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

}
