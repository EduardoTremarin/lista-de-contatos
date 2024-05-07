package io.uex.listadecontatos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateContactDTO {

	private String name;
	private String cpf;
	private String telephone;
	private Long userId;
	private String road;
	private String neighborhood;
	private String city;
	private String state;
	private String country;
	private String cep;
	private String longitude;
	private String latitude;
	
}