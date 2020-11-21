package com.cursodsousa.bibliotecaapi.api.service;

import java.util.Optional;

import com.cursodsousa.bibliotecaapi.model.entity.Book;

public interface BookService {

	Book save(Book book);

	Optional<Book> getById(Long id);

	void delete(Book book);

	Book update(Book book);

}
