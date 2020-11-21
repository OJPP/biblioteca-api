package com.cursodsousa.bibliotecaapi.api.resource;

import java.util.Optional;

import org.hamcrest.Matchers;
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
import com.cursodsousa.bibliotecaapi.api.exceptions.BusinessException;
import com.cursodsousa.bibliotecaapi.api.service.BookService;
import com.cursodsousa.bibliotecaapi.model.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

// Necessário para testar um controlador do Spring Boot. O Spring vai criar um mini contexto de injeção de dependências
// para executar os testes.
@ExtendWith(SpringExtension.class)
// Para executar os testes com o perfil de testes. Pode-se definir configurações específicas para quando
// executamos com este perfil
@ActiveProfiles("test")
//
@WebMvcTest
//
@AutoConfigureMockMvc
public class BookControllerTest {

	static final String BOOK_API = "/api/books";

	@Autowired
	MockMvc mockMvc; // Este objecto é que permite mocar as nossas requisicoes http

	@MockBean
	BookService bookService;

	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception {

		// Cenário
		BookDTO bookDTO = createNewBook();

		// Simular o comportamento do método save da classe de serviço bookService
		Book savedBook = Book.builder().id(10l).author("Artur").title("As aventuras").isbn("001").build();
		BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(bookDTO);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		// Acção e Verificação
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
	public void createInvalidBookTest() throws Exception {

		// Cenário
		String json = new ObjectMapper().writeValueAsString(new BookDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		// Acção e Verificação
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));

	}

	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro.")
	public void createBookWithDuplicatedIsbn() throws Exception {

		// Cenário
		BookDTO bookDTO = createNewBook();

		String json = new ObjectMapper().writeValueAsString(bookDTO);
		String mensagemErro = "Isbn já cadastrado.";

		BDDMockito.given(bookService.save(Mockito.any(Book.class)))
				.willThrow(new BusinessException(mensagemErro));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		// Acção e Verificação
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(mensagemErro));

	}

	@Test
	@DisplayName("Deve obter informações de um livro.")
	public void getBookDetailsTest() throws Exception {

		// Cenário
		Long id = 1l;

		Book book = Book.builder()
				.id(id)
				.author(createNewBook().getAuthor())
				.title(createNewBook().getTitle())
				.isbn(createNewBook().getIsbn())
				.build();

		BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(book));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		// Acção e Verificação
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(MockMvcResultMatchers.jsonPath("author").value(createNewBook().getAuthor()))
				.andExpect(MockMvcResultMatchers.jsonPath("isbn").value(createNewBook().getIsbn()));

	}

	@Test
	@DisplayName("Deve retornar resource not found quando o livro procurado não existir.")
	public void bookNotFoundTest() throws Exception {

		// Cenário
		Long id = 1l;

		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		// Acção e Verificação
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	@DisplayName("Deve eliminar um livro.")
	public void deleteBookTest() throws Exception {

		// Cenário
		Long id = 1l;

		BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(Book.builder().id(id).build()));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		// Acção e Verificação 
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isNoContent());

	}

	@Test
	@DisplayName("Deve retornar resource not found quando não encontrar o livro para eliminar.")
	public void deleteInexistentBookTest() throws Exception {

		// Cenário
		Long id = 1l;

		BDDMockito.given(bookService.getById(id)).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		// Acção e Verificação 
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	@DisplayName("Deve actualizar um livro.")
	public void updateBookTest() throws Exception {

		// Cenário
		Long id = 1l;

		String json = new ObjectMapper().writeValueAsString(this.createNewBook());

		Book bookSaved = Book.builder().id(1l).author("Some Author").title("Some Title").isbn("321").build(); 
		BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(bookSaved));

		Book book = Book.builder().id(1l).author("Artur").title("As aventuras").isbn("321").build(); 
		BDDMockito.given(bookService.update(book)).willReturn(book);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		// Acção e Verificação 
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(MockMvcResultMatchers.jsonPath("author").value(createNewBook().getAuthor()))
				.andExpect(MockMvcResultMatchers.jsonPath("isbn").value(bookSaved.getIsbn()));

	}

	@Test
	@DisplayName("Deve lancar excepção ao tentar actualizar um livro inexistente.")
	public void updateInexistentBookTest() throws Exception {

		// Cenário
		Long id = 1l;

		String json = new ObjectMapper().writeValueAsString(this.createNewBook());

		BDDMockito.given(bookService.getById(id)).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		// Acção e Verificação 
		mockMvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	private BookDTO createNewBook() {
		return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
	}

}
