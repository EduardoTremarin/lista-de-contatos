package io.uex.listadecontatos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.uex.listadecontatos.entity.Address;
import io.uex.listadecontatos.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDTO {

	private long id;
	private String name;
	private String cpf;
	private String telephone;
	private User user;
	private Address address;
	
}