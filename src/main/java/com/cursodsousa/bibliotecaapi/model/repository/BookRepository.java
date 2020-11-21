package com.cursodsousa.bibliotecaapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursodsousa.bibliotecaapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	boolean existsByIsbn(String isbn);

}
