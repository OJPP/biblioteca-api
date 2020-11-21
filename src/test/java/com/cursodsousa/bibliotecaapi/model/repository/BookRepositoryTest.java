package com.cursodsousa.bibliotecaapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	BookRepository bookRepository;
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado.")
	public void returnTrueWhenIsbnExists() {

		// cenario
		String isbn = "123";

		// accao
		boolean exists = bookRepository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isTrue();

	}
	
}
