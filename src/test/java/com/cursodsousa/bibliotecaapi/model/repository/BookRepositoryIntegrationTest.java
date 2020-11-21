package com.cursodsousa.bibliotecaapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cursodsousa.bibliotecaapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryIntegrationTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	BookRepository bookRepository;
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado.")
	public void returnTrueWhenIsbnExists() {

		// cenario
		String isbn = "123";

		Book book = createValidBook(isbn);
		entityManager.persist(book);

		// accao
		boolean exists = bookRepository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isTrue();

	}

	@Test
	@DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado.")
	public void returnFalseWhenIsbnDoesNotExists() {

		// cenario
		String isbn = "123";

		// accao
		boolean exists = bookRepository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isFalse();

	}

	@Test
	@DisplayName("Deve obter um livro por id.")
	public void findByIdTest() {

		// cenario
		Book book = createValidBook("123");
		entityManager.persist(book);

		// accao
		Optional<Book> bookOptional = bookRepository.findById(book.getId());

		// verificação
		assertThat(bookOptional.isPresent()).isTrue();
		assertThat(bookOptional.get().getId()).isEqualTo(book.getId());
		assertThat(bookOptional.get().getAuthor()).isEqualTo(book.getAuthor());
		assertThat(bookOptional.get().getTitle()).isEqualTo(book.getTitle());
		assertThat(bookOptional.get().getIsbn()).isEqualTo(book.getIsbn());

	}

	private Book createValidBook(String isbn) {
		return Book.builder().title("As aventuras").author("Fulano").isbn(isbn).build();
	}

}
