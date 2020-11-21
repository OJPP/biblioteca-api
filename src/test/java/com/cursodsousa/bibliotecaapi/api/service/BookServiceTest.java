package com.cursodsousa.bibliotecaapi.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cursodsousa.bibliotecaapi.api.exceptions.BusinessException;
import com.cursodsousa.bibliotecaapi.api.services.impl.BookServiceImpl;
import com.cursodsousa.bibliotecaapi.model.entity.Book;
import com.cursodsousa.bibliotecaapi.model.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService bookService;

	@MockBean
	BookRepository bookRepository;

	@BeforeEach
	public void setup() {
		this.bookService = new BookServiceImpl(bookRepository);
	}

	@Test
	@DisplayName("Deve salvar um livro.")
	public void saveBookTest() {
		
		// cenario
		Book book = createValidBook();
		Book bookSaved = Book.builder().id(1l).title("As aventuras").author("Fulano").isbn("123").build();

		Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);

		Mockito.when(bookRepository.save(book)).thenReturn(bookSaved);
		
		// accao
		Book savedBook = bookService.save(book);
		
		// verificação
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		
	}

	@Test
	@DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado.")
	public void shouldNotSaveABookWithDuplicatedIsbn() {

		// cenario
		Book book = createValidBook();

		Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

		// accao
		Throwable exception = Assertions.catchThrowable( () -> bookService.save(book));

		// verificação
		assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrado.");

		Mockito.verify(this.bookRepository, Mockito.never()).save(book);
	}

	@Test
	@DisplayName("Deve obter um livro por id.")
	public void getByIdTest() {

		// cenario
		Long id = 1l;
		
		Book book = createValidBook();
		book.setId(id);
		
		Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

		// accao
		Optional<Book> foundBook = bookService.getById(id);

		// verificação
		assertThat(foundBook.isPresent()).isTrue();
		assertThat(foundBook.get().getId()).isEqualTo(id);
		assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
		assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
		assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());

	}

	@Test
	@DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base de dados.")
	public void bookNotFoundByIdTest() {

		// cenario
		Long id = 1l;
		Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

		// accao
		Optional<Book> bookOptional = bookService.getById(id);

		// verificação
		assertThat(bookOptional.isPresent()).isFalse();

	}

	private Book createValidBook() {
		return Book.builder().title("As aventuras").author("Fulano").isbn("123").build();
	}

}
