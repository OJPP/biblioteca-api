package com.cursodsousa.bibliotecaapi.api.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

	private Long id;
	
	@NotEmpty(message = "O Título é obrigatório.")
	private String title;
	
	@NotEmpty(message = "O Autor é obrigatório.")
	private String author;
	
	@NotEmpty(message = "O Isbn é obrigatório.")
	private String isbn;
	
}
