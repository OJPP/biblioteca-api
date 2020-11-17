package com.cursodsousa.bibliotecaapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cursodsousa.bibliotecaapi.api.service.BookService;
import com.cursodsousa.bibliotecaapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService bookService;

	@Test
	@DisplayName("Deve salvar um livro.")
	public void saveBookTest() {
		
		// cenario
		Book book = Book.builder().title("As aventuras").author("Fulano").isbn("123").build();
		
		// accao
		Book savedBook = bookService.save(book);
		
		// verificação
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulabo");
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		
	}

}
