package com.cursodsousa.bibliotecaapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cursodsousa.bibliotecaapi.api.service.BookService;
import com.cursodsousa.bibliotecaapi.model.entity.Book;
import com.cursodsousa.bibliotecaapi.model.repository.BookRepository;
import com.cursodsousa.bibliotecaapi.services.impl.BookServiceImpl;

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
		Book book = Book.builder().title("As aventuras").author("Fulano").isbn("123").build();
		Book bookSaved = Book.builder().id(1l).title("As aventuras").author("Fulano").isbn("123").build();
		
		Mockito.when(bookRepository.save(book)).thenReturn(bookSaved);
		
		// accao
		Book savedBook = bookService.save(book);
		
		// verificação
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		
	}

}
