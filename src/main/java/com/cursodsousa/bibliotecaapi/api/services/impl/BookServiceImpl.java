package com.cursodsousa.bibliotecaapi.api.services.impl;

import org.springframework.stereotype.Service;

import com.cursodsousa.bibliotecaapi.api.exceptions.BusinessException;
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
		
		if(bookRepository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn já cadastrado.");
		}

		return bookRepository.save(book);
	}

}
