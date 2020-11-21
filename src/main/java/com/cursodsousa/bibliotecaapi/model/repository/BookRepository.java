package com.cursodsousa.bibliotecaapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursodsousa.bibliotecaapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	boolean existsByIsbn(String isbn);

	Optional<Book> getById(Long id);
}
