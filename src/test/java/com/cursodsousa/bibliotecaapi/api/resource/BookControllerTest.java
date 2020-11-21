package com.cursodsousa.bibliotecaapi.api.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cursodsousa.bibliotecaapi.api.dto.BookDTO;
import com.cursodsousa.bibliotecaapi.api.service.BookService;
import com.cursodsousa.bibliotecaapi.model.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

	static String BOOK_API = "/api/books";

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BookService bookService;
	
	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception {

		BookDTO bookDTO = BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();

		// Simular o comportamento do método save da classe de serviço bookService
		Book savedBook = Book.builder().id(10l).author("Artur").title("As aventuras").isbn("001").build();
		BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(bookDTO);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("title").value(bookDTO.getTitle()))
				.andExpect(MockMvcResultMatchers.jsonPath("author").value(bookDTO.getAuthor()))
				.andExpect(MockMvcResultMatchers.jsonPath("isbn").value(bookDTO.getIsbn()));
	}

	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação de um livro.")
	public void createInvalidBookTest() {
	}

}
